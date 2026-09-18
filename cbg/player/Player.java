/*
 * Created on Feb 25, 2004
 *
 * Author: Stephen Chudleigh
 */
package cbg.player;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import cbg.boardParts.*;
import cbg.common.UIConsts;
import cbg.ui.CBGDlgFactory;
import cbg.ui.ConsciousBoardgameGUI;

public class Player implements cbg.common.UIConsts {

	protected String name = "Player 1";
	protected int deathSpace = Board.getNumSpaces()-1;
	protected int boardTrips = 0;
	protected int sleepTilSpace = -1, noSkillsTil = -1, noPowersTil = -1;
	protected int rollMultiple=1, type, age, boardPos;
	protected int cardPlaysLeft, lawPlaysThis;
	protected boolean isDead, asleep, bwe, ewb, selfRemember;
	protected boolean rollAgain = false, oppositeRoll = false;
	protected ArrayList pocHand, lawHand, lawsInPlay, activeLaws;
	protected FoodDiagram fd;
	protected EssenceAndPersonality ep;
    private PlayerStrategy strategy;

	public Player() {
		age=0;
		boardPos=0;
		lawPlaysThis=0;
		asleep = bwe = ewb = selfRemember = isDead = false;
		pocHand = new ArrayList(20);
		lawHand = new ArrayList(10);
		lawsInPlay = new ArrayList(8);
		activeLaws = new ArrayList(8);
		fd = new FoodDiagram(this);
		ep = new EssenceAndPersonality();
        strategy = new ThreeBrainStrategy();
        // set after ep is created.
        setCardPlays(ep.getLob().getPlaysPerTurn());
    }
    
    public void initGame() {
        pocHand.clear();
        for (int i=0; i<7; i++) 
            drawPOCCard(true);
        for (int i=0; i<3; i++)
            drawLawCard();
        setLawPlaysThis(2);
        giveMCMoment();
        Dice.setNumSides(6);
        Dice.setHasZero(false);
        setType(Dice.roll());
        Dice.init();  // set die back to original
        getFd().forceChange(); 
    }
	
	private PropertyChangeSupport changes =
        new PropertyChangeSupport(this);
        
	public void addPropertyChangeListener(PropertyChangeListener l) {
	    changes.addPropertyChangeListener(l);
	}
	public void removePropertyChangeListener(PropertyChangeListener l) {
	    changes.removePropertyChangeListener(l);
	}

	//////// 	METHODS		/////////	
	public void drawPOCCard(boolean forced) {
		if (asleep && !forced) {
			System.out.println("You're asleep; No card for you.");
			CBGDlgFactory.showSleepMessage("card");
		} else {
			//System.out.println("Drawing a card.");
			Card newCard = Decks.drawPOCCard();
			CBGDlgFactory.displayMessage("You drew a "+newCard);
			pocHand.add(newCard);
			Collections.sort(pocHand);
			changes.firePropertyChange("POCHandChange", null, pocHand);
		}
	}
	
	public void drawPOCCard(Card c) {
		pocHand.add(c);
		Collections.sort(pocHand);
		changes.firePropertyChange("POCHandChange", null, pocHand);
	}

	public void drawLawCard() {
		lawHand.add(Decks.drawLawCard());
	}
    public void drawLawCard(LawCard card) {
        lawHand.add(card);
    }

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type; 
	}
	protected void endTurn() {
		if (lawPlaysThis > 0) {
			lawPlaysThis = 0;
			lawsInPlay.clear();
			//System.out.println("Laws in play size = "+lawsInPlay.size());
		}
		setCardPlays(ep.getLob().getPlaysPerTurn());
		this.bwe = ep.foundSchool();
		this.ewb = ep.hasAprilFools();
		this.selfRemember = ep.has1001Words();
		if (this.isDead() && fd.hasChrystallizedMental() && !fd.hasMovedMental()) {
		    // In case the player was dead and didn't move mental immediately
		    fd.movePieces();
		}
	}
	public void eat() {
		if (asleep) {
			System.out.println("You're asleep; No food for you.");
			CBGDlgFactory.showSleepMessage("food");
		} else fd.eat();
	}
	public void breathe() {
		if (asleep) {
			System.out.println( "You're asleep; No air for you.");
			CBGDlgFactory.showSleepMessage("air");
		} else fd.breathe();
	}
	public void takeImpression() {
		if (asleep) {
			System.out.println("You're asleep; No impression for you.");
			CBGDlgFactory.showSleepMessage("impression");
		} else fd.impression();
	}

    public void move(int roll) {
        endTurn();
        age++;
        int iCursp = getBoardPos();
        if ((iCursp+roll)>=Board.getNumSpaces()) {
            roll = Board.getNumSpaces()-iCursp-1;
        }
        if (isAsleep() && ((iCursp+roll) > getSleepTilSpace())) {
            asleep = false;
            sleepTilSpace = -1;
            removeActiveLaw(19); // value of sleep law
        }
        if ((iCursp<noSkillsTil)&&((iCursp+roll)>noSkillsTil)) {
            noSkillsTil = -1;
            removeActiveLaw(41); // value of no skill law
        }
        if ((iCursp<noPowersTil)&&((iCursp+roll)>noPowersTil)) {
            noPowersTil = -1;
            removeActiveLaw(59); // value of no power law
        }
        BoardSpace sp = Board.getSpace(iCursp);
        sp.leave(this);
        int lawsPassed = 0;
        for (int i=1; i<roll; i++) {
            sp = Board.getSpace(iCursp+i);
            String s = sp.passOver();
            if (s != null) { // passed a law
                //System.out.println(s);
                lawsPassed++;
            }
        }
        iCursp += roll;
        setBoardPos(iCursp);
        sp = Board.getSpace(getBoardPos());
        //System.out.println("You landed on "+sp.toString());
        sp.landOn(this);
        if (iCursp >= deathSpace) {
            dies();
            System.out.println("You just died on space "+iCursp);
        }
        if (iCursp == Board.getNumSpaces()-1) {
            System.out.println("You made it to the end of the board, reversing direction.\n"+
            " space #"+iCursp);
            Board.reverseBoard();
            setBoardPos(0);
            if (!isHasnamuss()) boardTrips++;
        }
        //System.out.println("You passed "+lawsPassed+" law spaces and resulted in "+ra.getResult());
        setLawPlaysThis(lawsPassed);
        for (int i=0; i<lawsPassed; i++) {
            drawLawCard();
        }
    }
	public void dies() {
        strategy.dies(this);
        ConsciousBoardgameGUI.getInstance().lastTurn();
	}
    public void startDeathGame() {
        endTurn();
        removeAllLawsButJoker();
        lawHand.clear();
        strategy.startDeath(this);
    }
	public EssenceAndPersonality getEp() {
		return ep;
	}
	public void giveMCMoment() {
		setCardPlays(cardPlaysLeft+1);
	}
	/**
	 * @return card plays the player has left this turn
	 */
	public int getCardPlays() {
		return cardPlaysLeft;
	}	
	public void setCardPlays(int n) {
		int oVal = getCardPlays();
		cardPlaysLeft = n;
		changes.firePropertyChange("CardPlaysLeft", oVal, cardPlaysLeft);
	}
	/**
	 * @return
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @return
	 */
	public int getBoardPos() {
		return boardPos;
	}
	public void setBoardPos(int pos) {
		boardPos = pos;
	}
	/**
	 * @return
	 */
	public FoodDiagram getFd() {
		return fd;
	}
	/**
	 * @return ArrayList containing LawCard(s) in the players hand.
	 */
	public ArrayList getLawHand() {
		return lawHand;
	}
	public String printLawHand() {
		String retString="";
		for (Iterator i = lawHand.iterator(); i.hasNext();) {
			retString+=(LawCard)i.next();
		}
		return retString;
	}

	/**
	 * @return
	 */
	public List getPocHand() {
		return pocHand;
	}
	public void setPocHand(ArrayList a) {
		pocHand = a;
	}
	public String printPocHand() {
		String retString="^";
		int c=0;
		for (Iterator i = pocHand.iterator(); i.hasNext();) {
			retString+="("+(c++)+")"+i.next()+"^";
			if ((c%7) == 0)
				retString += "\n";
			
		}
		return retString;
	}
	public String printFoodDiagram() {
		return fd.toString();
	}
	/**
	 * @return
	 */
	public boolean isAsleep() {
		return asleep;
	}

	public boolean isDead() {
		return isDead;
	}

	public String toString() {
		return "PLAYER "+name;
	}
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * Returns the lawPlaysThis.
	 * @return int
	 */
	public int getLawPlaysThis() {
		return lawPlaysThis;
	}

	/**
	 * Sets the lawPlaysThis.
	 * @param lawPlaysThis The lawPlaysThis to set
	 */
	public void setLawPlaysThis(int lawPlays) {
		lawPlaysThis = lawPlays;
	}

	/**
	 * Method takeCardPlays.
	 * @param i
	 */
	public void takeCardPlays(int i) {
		setCardPlays(cardPlaysLeft - i);
	}

	/**
	 * Returns the lawsInPlay.
	 * @return ArrayList
	 */
	public ArrayList getLawsInPlay() {
		return lawsInPlay;
	}

	/**
	 * Method addLawToPlay.
	 * @param randomLaw
	 */
	public void addLawToPlay(LawCard lc) {
		if (lawsInPlay == null) {
			lawsInPlay = new ArrayList(12);
		}
		lawsInPlay.add(lc);
	}
	
	public void addActiveLaw(LawCard lc) {
		activeLaws.add(lc);
	}
	public void removeActiveLaw(int lawVal) {
		for (int i=0; i<activeLaws.size(); i++) {
			if (lawVal == ((LawCard)activeLaws.get(i)).getLawValue()) {
				activeLaws.remove(i);
				break;
			}
		}
	}    
    public void removeAllLawsButJoker() {
        deathSpace = Board.getNumSpaces()-1;
        sleepTilSpace = -1;
        noSkillsTil = -1;
        noPowersTil = -1;
        ArrayList jlist = new ArrayList();
        jlist.add(LawCard.Joker);
        activeLaws.retainAll(jlist);
    }    
	public ArrayList getActiveLaws() {
		return activeLaws;
	}
	/**
	 * Method hasObeyedLaws.
	 * @return boolean
	 */
	public boolean hasObeyedLaws() {
		for (Iterator it = lawsInPlay.iterator();
			it.hasNext();) {
			if (!((LawCard)it.next()).isObeyed())
				return false;
		}
		return true;
	}

	/**
	 * Returns the bwe.
	 * @return boolean
	 */
	public boolean hasBwe() {
		return (bwe && (getNoSkillsTil() < getBoardPos()));
	}

	/**
	 * Sets the bwe.
	 * @param bwe The bwe to set
	 */
	public void setHasBwe(boolean bwe) {
		this.bwe = bwe;
	}

	/**
	 * Returns the ewb.
	 * @return boolean
	 */
	public boolean hasEwb() {
		return (ewb && (getNoSkillsTil() < getBoardPos()));
	}

	/**
	 * Sets the ewb.
	 * @param ewb The ewb to set
	 */
	public void setHasEwb(boolean ewb) {
		this.ewb = ewb;
	}

	/**
	 * Returns the selfRemember.
	 * @return boolean
	 */
	public boolean hasSelfRemember() {
		return (selfRemember && (getNoSkillsTil() < getBoardPos()));
	}

	/**
	 * Sets the selfRemember.
	 * @param selfRemember The selfRemember to set
	 */
	public void setHasSelfRemember(boolean selfRemember) {
		this.selfRemember = selfRemember;
	}

	/**
	 * Returns the sleepTilSpace.
	 * @return int
	 */
	public int getSleepTilSpace() {
		return sleepTilSpace;
	}

	/**
	 * Sets the sleepTilSpace.
	 * @param sleepTilSpace The sleepTilSpace to set
	 */
	public void setAsleepTil(int sleepTilSpace) {
		asleep = true;
		this.sleepTilSpace = sleepTilSpace;
	}

	/**
	 * Sets the deathSpace.
	 * @param deathSpace The deathSpace to set
	 */
	public void setDeathSpace(int deathSpace) {
		this.deathSpace = deathSpace;
	}

	/**
	 * Method survivesDeath.
	 * @return boolean
	 */
	public boolean survivesDeath() {
        return strategy.survivesDeath(this);
    }
	
	/**
	 * Returns the noPowersTil.
	 * @return int
	 */
	public int getNoPowersTil() {
		return noPowersTil;
	}

	/**
	 * Sets the noPowersTil.
	 * @param noPowersTil The noPowersTil to set
	 */
	public void setNoPowersTil(int noPowersTil) {
		this.noPowersTil = noPowersTil;
	}

	/**
	 * Returns the noSkillsTil.
	 * @return int
	 */
	public int getNoSkillsTil() {
		return noSkillsTil;
	}

	/**
	 * Sets the noSkillsTil.
	 * @param noSkillsTil The noSkillsTil to set
	 */
	public void setNoSkillsTil(int noSkillsTil) {
		this.noSkillsTil = noSkillsTil;
	}
	/**
	 * Returns the rollAgain.
	 * @return boolean
	 */
	public boolean hasRollAgain() {
		return rollAgain;
	}

	/**
	 * Sets the rollAgain.
	 * @param rollAgain The rollAgain to set
	 */
	public void setRollAgain(boolean rollAgain) {
		this.rollAgain = rollAgain;
	}

	/**
	 * Returns the oppositeRoll.
	 * @return boolean
	 */
	public boolean hasOppositeRoll() {
		return oppositeRoll;
	}

	/**
	 * Sets the oppositeRoll.
	 * @param oppositeRoll The oppositeRoll to set
	 */
	public void setOppositeRoll(boolean oppositeRoll) {
		this.oppositeRoll = oppositeRoll;
	}

	public boolean hasAccidentMoon() {
		return activeLaws.contains(LawCard.KingDiamonds);
	}
	public boolean hasCauseEffectMoon() {
		return activeLaws.contains(LawCard.KingClubs);
	}
	public boolean hasFateMoon() {
		return activeLaws.contains(LawCard.KingHearts);
	}
	public boolean hasWillMoon() {
		return activeLaws.contains(LawCard.KingSpades);
	}
    /**
     * @return Returns the isHasnamuss.
     */
    public boolean isHasnamuss() {
        return strategy.isHasnamuss();
    }
    public void makeHasnamuss() {
        strategy = new HasnamussStrategy();
    }
    public void cleanseHasnamuss() {
        strategy = new ThreeBrainStrategy();
        this.removeActiveLaw(86);
    }
    /**
     * 
     */
    public void decays() {
        CBGDlgFactory.giveDecayChoice(this);
    }
    /**
     * 
     */
    public void giveWildChoice() {
        CBGDlgFactory.giveWildChoice(this);
    }
    /**
     * @return
     */
    public boolean hasNonAceLaw() {
		for (Iterator it = getLawHand().iterator(); it.hasNext();) {
			if (((LawCard)it.next()).getCard().getRank() != Card.ACE)
				return true;
		}
		return false;
    }

    public void reincarate() {
        int rolls[] = HasnamussStrategy.rollForType();
        short numBrains;
        if (rolls[0]<4) {
            setRollMultiple(3);
            numBrains = 1;
        } else if (rolls[0]<6) {
            setRollMultiple(2);
            numBrains = 2;
        } else {
            setRollMultiple(1);
            numBrains = 3;
        }
        setRollMultiple(1);
        numBrains = 3;
        CBGDlgFactory.showReincarnationMessage(numBrains);
        initGame();
        setType(numBrains);
        getFd().clearOctave(UIConsts.FOOD);
        getFd().clearOctave(UIConsts.AIR);
        getFd().clearOctave(UIConsts.IMP);
        getFd().give331();
        getFd().forceChange();
        getEp().clearAllButJoker();
        ConsciousBoardgameGUI.getInstance().showLawsDialog(false);
    }

    public int getRollMultiple() {
        return rollMultiple;
    }

    /**
     * @param rollMultiple the rollMultiple to set
     */
    public void setRollMultiple(int rollMultiple) {
        this.rollMultiple = rollMultiple;
    }
}
