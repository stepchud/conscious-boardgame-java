/*
 * Created on Jul 7, 2004
 */
package cbg.player;
import cbg.boardParts.Card;
import cbg.common.InvalidCardPlayException;

import java.util.HashMap;
import java.util.Set;
import java.beans.*;

/**
 * @author Stephen Chudleigh
 **/
public class EssenceAndPersonality {
		
	private HashMap partsOfBeing = new HashMap();
	private boolean foundSchool, gotSteward, gotMaster, gotDouble, gotNothing, isComplete;
	private LevelOfBeing lob;
	
	private PropertyChangeSupport changes =
            new PropertyChangeSupport(this);
            
    public void addPropertyChangeListener(PropertyChangeListener l) {
	    changes.addPropertyChangeListener(l);
	}
	public void removePropertyChangeListener(PropertyChangeListener l) {
	    changes.removePropertyChangeListener(l);
	}
	
	public EssenceAndPersonality() {
		short zero=0;
		PartOfBeing Joker = new PartOfBeing(Card.JO, zero, null);
		PartOfBeing XJoker = new PartOfBeing(Card.XJ, zero, Joker);
		PartOfBeing AceSpades = new PartOfBeing(Card.AS, zero, XJoker);
		PartOfBeing AceHearts = new PartOfBeing(Card.AH, zero, AceSpades);
		PartOfBeing AceClubs = new PartOfBeing(Card.AC, zero, AceHearts);
		PartOfBeing AceDiamonds = new PartOfBeing(Card.AD, zero, AceClubs);
		PartOfBeing KingSpades = new PartOfBeing(Card.KS, zero, AceDiamonds);
		PartOfBeing QueenSpades = new PartOfBeing(Card.QS, zero, KingSpades);
		PartOfBeing JackSpades = new PartOfBeing(Card.JS, zero, QueenSpades);
		PartOfBeing KingHearts = new PartOfBeing(Card.KH, zero, JackSpades);
		PartOfBeing QueenHearts = new PartOfBeing(Card.QH, zero, KingHearts);
		PartOfBeing JackHearts = new PartOfBeing(Card.JH, zero, QueenHearts);
		PartOfBeing KingClubs = new PartOfBeing(Card.KC, zero, JackHearts);
		PartOfBeing QueenClubs = new PartOfBeing(Card.QC, zero, KingClubs);
		PartOfBeing JackClubs = new PartOfBeing(Card.JC, zero, QueenClubs);
		PartOfBeing KingDiamonds = new PartOfBeing(Card.KD, zero, JackClubs);
		PartOfBeing QueenDiamonds = new PartOfBeing(Card.QD, zero, KingDiamonds);
		PartOfBeing JackDiamonds = new PartOfBeing(Card.JD, zero, QueenDiamonds);
		
		partsOfBeing.put(Joker.getCardValue(), Joker);
		partsOfBeing.put(XJoker.getCardValue(), XJoker);
		partsOfBeing.put(AceSpades.getCardValue(), AceSpades);
		partsOfBeing.put(AceHearts.getCardValue(), AceHearts);
		partsOfBeing.put(AceClubs.getCardValue(), AceClubs);
		partsOfBeing.put(AceDiamonds.getCardValue(), AceDiamonds);
		
		partsOfBeing.put(KingSpades.getCardValue(), KingSpades);
		partsOfBeing.put(QueenSpades.getCardValue(), QueenSpades);
		partsOfBeing.put(JackSpades.getCardValue(), JackSpades);
		partsOfBeing.put(KingHearts.getCardValue(), KingHearts);
		partsOfBeing.put(QueenHearts.getCardValue(), QueenHearts);
		partsOfBeing.put(JackHearts.getCardValue(), JackHearts);
		
		partsOfBeing.put(KingClubs.getCardValue(), KingClubs);
		partsOfBeing.put(QueenClubs.getCardValue(), QueenClubs);
		partsOfBeing.put(JackClubs.getCardValue(), JackClubs);
		partsOfBeing.put(KingDiamonds.getCardValue(), KingDiamonds);
		partsOfBeing.put(QueenDiamonds.getCardValue(), QueenDiamonds);
		partsOfBeing.put(JackDiamonds.getCardValue(), JackDiamonds);
		
		foundSchool = gotSteward = gotMaster = gotDouble = gotNothing = false;
		lob = new Multiplicity();
	}
	
	private class PartOfBeing {
		private Card value;
		private short chips;
		private PartOfBeing nextPart;
		
		private PartOfBeing(Card c, short cnt, PartOfBeing nxt) {
			value = c;
			chips = cnt;
			nextPart = nxt;
		}
		
		private boolean addChip() {
			if (value.getRank()==Card.JOKR) {
				chips++;
				return true;
			}
			
			if (chips<2) {
				chips++;
				return true;
			} else return false;
		}
		private boolean addTwoChips() {
			if (value.getRank()==Card.JOKR) {
				chips+=2;
				return true;
			}
			
			if (chips==0) {
				chips+=2;
				return true;
			} else return false;
		}
		
		private boolean takeChip() {
			if (chips>0)
				chips--;
			else 
				return false;
			return true;
		}
        
        private void clearChips() {
            chips=0;
        }
		
		/**
         * 
         */
		private void putChip() {
            chips++;
        }

		/**
		 * Returns the chips.
		 * @return short
		 */
		private short getChips() {
			return chips;
		}

		/**
		 * Returns the nextPart.
		 * @return PartOfBeing
		 */
		private PartOfBeing getNextPart() {
			return nextPart;
		}

		/**
		 * Returns the value.
		 * @return Card
		 */
		private Card getCardValue() {
			return value;
		}
	}
	
	public boolean hasPiece(Card c) {
		return ((PartOfBeing)partsOfBeing.get(c)).getChips()>0;
	}
	
	public void takePiece(Card c) {
		PartOfBeing taken = (PartOfBeing)partsOfBeing.get(c);
		taken.takeChip();
		//System.out.println("piece taken from "+taken.getCardValue());
	}

	/**
	 * Method createPiece handles all bumping and returns the shocks required.
	 * @param c the card to add
	 * @return int[] number of: self remembers, transforms, wildshocks, allshocks
	 */
	public short[] createPiece(Card c) {
		short [] shocks = { 0, 0, 0, 0 };
		PartOfBeing newPart = (PartOfBeing)partsOfBeing.get(c);
		boolean added=newPart.addChip();
		//System.out.println("Creating a piece of "+c+" which has "+newPart.getChips()+" chips.");
		if (c.getRank()==Card.JACK || c.getRank()==Card.QUEEN || c.getRank()==Card.KING)
			shocks[0]++;
		else if (c.getRank()==Card.ACE)
			shocks[1]++;
		else if (c.getRank()==Card.X_J)
			shocks[2]++;
		else if (c.getRank()==Card.JOKR)
			shocks[3]++;
			
		if (!added) { // too many chips causes a bumping effect
			//System.out.println("Bumping overflow from "+c);
			shocks = bumpChipsFrom(newPart, shocks);
		}
		updateLevelOfBeing();
		return shocks;
	}
	
    /**
     * This gives a player two pieces, handles the bumping that occurs, and returns
     * the shocks that were caused.
     * @param giveTwo 
     * @return int[] number of: self remembers, transforms, wildshocks, allshocks
     */
    public short[] createTwoPieces(Card c) {
        short [] shocks = { 0, 0, 0, 0 };
		PartOfBeing newPart = (PartOfBeing)partsOfBeing.get(c);
		boolean added = newPart.addTwoChips();
		//System.out.println("Creating a piece of "+c+" which has "+newPart.getChips()+" chips.");
		if (c.getRank()==Card.JACK || c.getRank()==Card.QUEEN || c.getRank()==Card.KING)
			shocks[0]++;
		else if (c.getRank()==Card.ACE)
			shocks[1]++;
		else if (c.getRank()==Card.X_J)
			shocks[2]++;
		else if (c.getRank()==Card.JOKR)
			shocks[3]++;
			
		if (!added) { // too many chips causes a bumping effect
			//System.out.println("Bumping overflow from "+c);
		    newPart.putChip();
			shocks = bumpChipsFrom(newPart, shocks);
		}
		updateLevelOfBeing();
		return shocks;
    }

	/**
	 * Method bumpChipsFrom is a recursive method that hopefully works.
	 * @param part card to bump from
	 * @param shocks
	 * @return int[] number of: self remembers, transforms, wildshocks, allshocks
	 */
	private short [] bumpChipsFrom(PartOfBeing part, short [] shocks) {
		//System.out.println("bumping from part "+part.getCardValue());
		part.takeChip();
		part = part.getNextPart();
		Card c = part.getCardValue();
		
		if (c.getRank()==Card.JACK || c.getRank()==Card.QUEEN || c.getRank()==Card.KING)
			shocks[0]++;
		else if (c.getRank()==Card.ACE)
			shocks[1]++;
		else if (c.getRank()==Card.X_J)
			shocks[2]++;
		else if (c.getRank()==Card.JOKR)
			shocks[3]++;

		if (!part.addChip())
			shocks = bumpChipsFrom(part,shocks);
		else {
			//System.out.println("added chip to "+part.getCardValue());
		}
		//System.out.println("self remembers="+sr);
		return shocks;
	}

	/**
	 * Method createAce.
	 * @param card0
	 * @param card1
	 * @return int[] number of: self remembers, transforms, wildshocks, allshocks
	 * @throws InvalidCardPlayException
	 */
	public short [] createAce(Card card0, Card card1) throws InvalidCardPlayException {
		if (card0.getSuit() != card1.getSuit())
			throw new InvalidCardPlayException("Suits must match");
		else if (card0.getRank() == card1.getRank())
			throw new InvalidCardPlayException("Card rank can not match");
		else if ( card0.getRank()<Card.JACK ||
					card0.getRank()>Card.KING ||
					card1.getRank()<Card.JACK ||
					card1.getRank()>Card.KING )
			throw new InvalidCardPlayException("Rank must be between JACK and KING");
		else if (!hasPiece(card0))
			throw new InvalidCardPlayException("You don't have any "+card0+" pieces to play");
		else if (!hasPiece(card1))
			throw new InvalidCardPlayException("You don't have any "+card1+" pieces to play");
		else {
			takePiece(card0);
			takePiece(card1);
			return createPiece(new Card(Card.ACE, card0.getSuit()));
		}
	}

	/**
	 * Method createXJ.
	 * @param card0
	 * @param card1
	 * @return int
	 */
	public short [] createXJ(Card card0, Card card1) throws InvalidCardPlayException {
		if (card0.getRank()!=Card.ACE || card1.getRank()!=Card.ACE)
			throw new InvalidCardPlayException("You must play two aces to create an extra joker.");
		else if (card0.getSuit()==Card.SPADES || card1.getSuit()==Card.SPADES)
			throw new InvalidCardPlayException("You can't create an extra joker with a spade ace.");
		else if (card0.equals(card1))
			throw new InvalidCardPlayException("You must play two different aces to create an extra joker.");
		else if (!hasPiece(card0))
			throw new InvalidCardPlayException("You don't have any "+card0+" pieces to play");
		else if (!hasPiece(card1))
			throw new InvalidCardPlayException("You don't have any "+card1+" pieces to play");
		else {
			takePiece(card0);
			takePiece(card1);
			return createPiece(Card.XJ);
		}	
	}
	
    /**
     * @return
     */
    public void createJoker() throws InvalidCardPlayException {
        if (!hasPiece(Card.AS)) {
			throw new InvalidCardPlayException("You do not have an Ace of Spades to make a Joker.");
		} else if (!hasPiece(Card.XJ)) {
			throw new InvalidCardPlayException("You do not have an Extra Joker to make a Joker.");
		}
		takePiece(Card.XJ);
		takePiece(Card.AS);
		((PartOfBeing)partsOfBeing.get(Card.JO)).addChip();
		updateLevelOfBeing();
    }

	/**
	 * Method getChipsFor.
	 * @param card
	 * @return int
	 */
	public int getChipsFor(Card card) {
		try {
			return ((PartOfBeing)partsOfBeing.get(card)).getChips();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("Could not getChipsFor("+card+")\t"
							+" r="
							+card.getRank()
							+"|s="+card.getSuit());
			return -1;
		}
	}
	
	public Set getPartsOfBeing() {
		return partsOfBeing.keySet();
	}

	public void updateLevelOfBeing() {
		while (lob.hasAttainedNewLevelOfBeing(this)) {
			System.out.println("You have attained a new place on the stick!");
			setLob(lob.increaseLevelOfBeing());
			System.out.println("You are now a "+lob.toString());
        }
	}
	public boolean foundSchool() {
		if (foundSchool == true) return foundSchool;
		
		boolean hasJS = hasPiece(Card.JS);
		boolean hasQS = hasPiece(Card.QS);
		boolean hasKS = hasPiece(Card.KS);
		boolean hasJH = hasPiece(Card.JH);
		boolean hasQH = hasPiece(Card.QH);
		boolean hasKH = hasPiece(Card.KH);
		boolean hasJC = hasPiece(Card.JC);
		boolean hasQC = hasPiece(Card.QC);
		boolean hasKC = hasPiece(Card.KC);
		boolean hasJD = hasPiece(Card.JD);
		boolean hasQD = hasPiece(Card.QD);
		boolean hasKD = hasPiece(Card.KD);
		boolean hasAD = hasPiece(Card.AD);
		boolean hasAC = hasPiece(Card.AC);
		boolean hasAH = hasPiece(Card.AH);
		boolean hasAS = hasPiece(Card.AS);
		boolean hasXJ = hasPiece(Card.XJ);
		boolean hasJO = hasPiece(Card.JO);
		
		if (hasJS && hasQS && hasKS)
			foundSchool = true;
		else
		if (hasJH && hasQH && hasKH)
			foundSchool = true;
		else
		if (hasJC && hasQC && hasKC)
			foundSchool = true;
		else
		if (hasJD && hasQD && hasKD)
			foundSchool = true;
		else
		if (hasAD && (hasJD||hasQD||hasKD))
			foundSchool = true;
		else
		if (hasAC && (hasJC||hasQC||hasKD))
			foundSchool = true;
		else
		if (hasAH && (hasJH||hasQH||hasKH))
			foundSchool = true;
		else
		if (hasAS && (hasJS||hasQH||hasKH))
			foundSchool = true;
		else
		if (hasJO)
			foundSchool = true;
		else {	
			short queen_or_king=0;
			if (hasQD || hasKD)
				queen_or_king++;
			if (hasQC || hasKC)
				queen_or_king++;
			if (hasQH || hasKH)
				queen_or_king++;
			if (hasQS || hasKS)
				queen_or_king++;
			if (hasAD)
				queen_or_king++;
			if (hasAC)
				queen_or_king++;
			if (hasAH)
				queen_or_king++;
			if (hasAS)
				queen_or_king++;
			if (hasXJ)
				queen_or_king += 2;
			
			foundSchool = (queen_or_king>=3);
		}
		return foundSchool;
	}
	/**
	 * @return true iff player satisfies April Fools req.
	 */
	public boolean hasAprilFools() {
		if (gotSteward) return true;
		boolean hasAS = hasPiece(Card.AS);
		boolean hasAH = hasPiece(Card.AH);
		boolean hasAC = hasPiece(Card.AC);
		boolean hasAD = hasPiece(Card.AD);
		boolean hasXJ = hasPiece(Card.XJ);
		boolean hasJO = hasPiece(Card.JO);
		if (hasAD && hasAC && hasAH && hasAS)
			gotSteward = true;
		else if (hasXJ && hasAS && (hasAD || hasAC || hasAH))
			gotSteward = true;
		else if (hasJO && (hasAD || hasAC || hasAH ))
			gotSteward = true;
		return gotSteward;
	}
	public boolean has1001Words() {
		if (gotMaster) return true;
		if (hasPiece(Card.JO))
			gotMaster = true;
		return gotMaster;
	}
	public boolean hasDoubled() {
		if (gotDouble) return true;
		if (getChipsFor(Card.JO) >= 4)
			gotDouble = true;
		return gotDouble;
	}
	public boolean hasNothing() {
		if (gotNothing) return true;
		if (getChipsFor(Card.JO) >= 5)
			gotNothing = true;
		return gotNothing;
	}
	
	public boolean isComplete(boolean isHasnamuss) {
	    if (!isHasnamuss) return (getChipsFor(Card.JO) >= 3);
            else return cleansedHasnamuss();
	}
	
    public void setComplete(boolean complete) {
		boolean oldval = isComplete;
		isComplete = complete;
		changes.firePropertyChange("EPComplete", oldval, isComplete);
	}

	/**
	 * Returns the me.
	 * @return LevelOfBeing
	 */
	public LevelOfBeing getLob() {
		return lob;
	}

	/**
	 * Sets the me.
	 * @param me The me to set
	 */
	public void setLob(LevelOfBeing newLevel) {
		LevelOfBeing oldLevel = lob;
		lob = newLevel;
		changes.firePropertyChange("LevelofBeing", oldLevel, newLevel);
	}
    public void clearAllButJoker() {
        PartOfBeing pob = (PartOfBeing)partsOfBeing.get(Card.JD);
        while (pob != null) {
            if (pob.getCardValue() != Card.JO) pob.clearChips();
            pob = pob.getNextPart();
        }
    }
    public void removeJoker() {
        this.takePiece(Card.JO);
    }
    /**
     * 
     */
    public boolean cleansedHasnamuss() {
        return (getChipsFor(Card.JO) > 3);
    }
}