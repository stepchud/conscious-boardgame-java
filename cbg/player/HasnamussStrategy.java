/*
 * Created on Oct 26, 2006
 */
package cbg.player;

import javax.swing.JOptionPane;

import cbg.boardParts.Board;
import cbg.boardParts.Dice;
import cbg.boardParts.LawCard;
import cbg.ui.CBGDlgFactory;
import cbg.ui.ConsciousBoardgameGUI;

/**
 * @author Stephen
 */
public class HasnamussStrategy extends PlayerStrategy {
    
    public static final short NOT_DEAD=0, ASTRAL_DEAD=1, CAUSAL_DEAD=2;
    
    private String planetName = "";
    private short deathScenario=NOT_DEAD;
    
    public boolean survivesDeath(Player thePlayer) {
		if (deathScenario == NOT_DEAD) {
			return thePlayer.getFd().hasChrystallizedAstral();
		} else {
			return true;
		}
	}
    public void dies(Player thePlayer) {
        switch (deathScenario) {
            case NOT_DEAD:
                if (survivesDeath(thePlayer)) {
                    if (thePlayer.getFd().hasChrystallizedMental()) {
                        JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                                "You died as a Hasnamuss with a Causal (Mental) body.\n"+
                                "At the end of this turn, you will move to the Planet of your Hasnamuss.",
                                "Mental Death",
                                JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                                "You died as a Hasnamuss with an Astral body.\n"+
                                "In order to escape the endless cycle of reincarnation,\n"+
                                "you must complete your Mental body before the end of this turn.\n" +
                                "NOTE: Astral Hasnamuss discards all pieces of\n" +
                                "\tEssence and Personality except the Joker.",
                                "Astral Death",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                            "You just died. As a Hasnamuss with no higher body chrystallized,\n"+
                            "you have until the end of this turn to complete an Astral body -\n"+
                            "Or your game will be over.\n",
                            "Simple Death",
                            JOptionPane.WARNING_MESSAGE);
                }
            break;
            case ASTRAL_DEAD:
                if (thePlayer.getRollMultiple()==1 && thePlayer.getFd().hasChrystallizedMental()) { 
                    // three brained being with Mental body
                    JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                            "Sorry you just died. As a three brained Hasnamuss with a Mental body,\n"+
                            "you will move to the Planet of your Hasnamuss at the end of this turn.",
                            "Mental Death",
                            JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                            "You died as a "+(thePlayer.rollMultiple==2 ? 2 : 1)+"-brained Hasnamuss.\n"+
                            "You will reincarnate at the end of this turn, losing your cards\n" +
                            "and all Essence and Personality pieces except the Joker.",
                            "Astral Death",
                            JOptionPane.PLAIN_MESSAGE);
                }
            break;
            case CAUSAL_DEAD:
                JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                        "You died as a Hasnamuss with a Causal (Mental) body.\n"+
                        "At the end of this turn, you will move to the Planet of your Hasnamuss.",
                        "Mental Death",
                        JOptionPane.PLAIN_MESSAGE);
            break;
        }
	}
    
    public void startDeath(Player thePlayer) {
        System.out.println("Hasnamuss::startDeath() start: "+this.toString());
        // add Hasnamuss law back, no escaping!
        if (!thePlayer.getActiveLaws().contains(LawCard.Joker))
            thePlayer.addActiveLaw(LawCard.Joker);
        
        if (deathScenario==NOT_DEAD) {
            if (thePlayer.getFd().hasChrystallizedMental()) {
                deathScenario = CAUSAL_DEAD;
                this.startCausalDeath(thePlayer);
            } else if (thePlayer.getFd().hasChrystallizedAstral()) {
                deathScenario = ASTRAL_DEAD;
                this.startAstralDeath(thePlayer);
            }
        } else if (deathScenario==ASTRAL_DEAD) {
            this.startAstralDeath(thePlayer);
        } else if (deathScenario==CAUSAL_DEAD) {
            this.startCausalDeath(thePlayer);
        }
        System.out.println("Hasnamuss::startDeath() end: "+this.toString());
    }
    private void startAstralDeath(Player thePlayer) {
        System.out.println("start Astral Muss Death for "+thePlayer.getName());
        if (thePlayer.rollMultiple==1 && thePlayer.getFd().hasChrystallizedMental()) {
            System.out.println("Player has 3 brains and a Mental body, i.e. Causal Hasnamuss");
            deathScenario = CAUSAL_DEAD;
            this.startCausalDeath(thePlayer);
            return;
        }
        thePlayer.reincarate();
    }    
    private void startCausalDeath(Player thePlayer) {
        thePlayer.isDead = true;
        System.out.println("startCausalDeath for "+thePlayer.getName());
        int rolls[] = rollForType();
        if (rolls[0] <4) {
            if (rolls[0]==rolls[1]) {
                CBGDlgFactory.displayInformationMessage("Redemption",
                    "You're quite fortunate. When rolling to determine the Planet of your Hasnamuss,\n"+
                    "you rolled "+rolls[1]+" twice in a row and are automatically cleansed.");
                thePlayer.cleanseHasnamuss();
                return;
            }
            planetName = "Remorse of Conscience";
            thePlayer.setRollMultiple(1);
        } else if (rolls[0]>5) {
            if (rolls[0]==rolls[1]) {
                CBGDlgFactory.endGameMessage(
                    "You are very unfortunate. When rolling to determine the Planet of your Hasnamuss,\n"+
                    "you rolled "+rolls[1]+" twice in a row. The Planet of your Hasnamuss is Eternal Retribution.\n" +
                    "There is no escape from this loathesome place; you are out of the game backwards.\n" +
                    "You must have done something very bad indeed. Want to try again?");
                return;
            } else {
                planetName = "Self Reproach";
                thePlayer.setRollMultiple(3);
            }
        } else {
            if (rolls[0]==rolls[1]) {
                planetName = "Remorse of Conscience";
                thePlayer.setRollMultiple(1);
            } else {
                planetName = "Repentance";
                thePlayer.setRollMultiple(2);
            }
        }
        CBGDlgFactory.showCausalDeathMessage(thePlayer, planetName);
        Board.initDeathBoard();
        thePlayer.getFd().shiftAfterDeath();
        if (thePlayer.getPocHand().size()>7) {
            ConsciousBoardgameGUI.getInstance().keepSelectedCardsConfig();
        } else if (thePlayer.getPocHand().size()<7) {
            int added = 7 - thePlayer.getPocHand().size();
            for (int i=0; i<added; i++) {
                thePlayer.drawPOCCard(true);
            }
            CBGDlgFactory.displayInformationMessage("Draw up to 7",
                    "You drew "+added+" cards to start death.\n");
        }
    }

    
    public static int[] rollForType() {
        Dice.setNumSides(6);
        Dice.setHasZero(false);
        int [] rolls = new int[]{Dice.roll(), Dice.roll()};
        Dice.init();
        System.out.println("Hasnamuss type rolls: "+rolls[0]+", "+rolls[1]);
        return rolls;
    }
    
    public boolean isHasnamuss() {
        return true;
    }
    /**
     * @return the planetName
     */
    public String getPlanetName() {
        return planetName;
    }
    
    public String toString() {
        return "deathScenario="+deathScenario+", planetName="+planetName;
    }
}
