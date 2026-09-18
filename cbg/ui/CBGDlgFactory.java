package cbg.ui;

import javax.swing.*;

import cbg.boardParts.*;
import cbg.common.NoSuchNoteException;
import cbg.common.UIConsts;
import cbg.player.Player;


/**
 * @author Stephen Chudleigh
 * Created on Jan 1, 2006
 */
public class CBGDlgFactory implements UIConsts {
    
	/**
	 * Method giveAirChoice.
	 */
	public static int giveAirChoice() {
		return JOptionPane.showOptionDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
					"There is an extra piece of air, You can:",
            		"Extra Air",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					ExtraAir,
					ExtraAir[0]);
	}
	
	/**
	 * Method giveImpChoice.
	 */
	public static int giveImpChoice() {
		return JOptionPane.showOptionDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
					"There is an extra piece of impression, You can:",
            		"Extra Impression",
            		JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					ExtraImp,
					ExtraImp[0]);
	}
	
	/**
	 * Method giveBWEChoice.
	 */
	public static boolean giveBWEChoice(Player plyr) {
		if (plyr.getEp().foundSchool() && plyr.hasBwe()) {
			int n = JOptionPane.showConfirmDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
						"Breathe when you eat?",
						"Skill",
						JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				plyr.setHasBwe(false);
				return true;
			}
		}
		return false;			
	}
	/**
	 * Method giveEWBChoice.
	 */
	public static boolean giveEWBChoice(Player plyr) {
		if (plyr.getEp().hasAprilFools() && plyr.hasEwb()) {
			int n = JOptionPane.showConfirmDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
						"Eat when you breathe?",
                		"Skill",
						JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				plyr.setHasEwb(false);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method giveSelfRemChoice.
	 */
	public static boolean giveSelfRemChoice(Player plyr) {
		if (plyr.getEp().has1001Words() && plyr.hasSelfRemember()) {
			int n = JOptionPane.showConfirmDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
						"Carbon 12?",
						"Skill",
						JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				plyr.setHasSelfRemember(false);
				return true;
			}
		}
		return false;
	}

    public static void showCausalDeathMessage(Player plyr, String planetName) {
        JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                "As a Hasnamuss with a Causal body, you die.\n"+
                "The Planet of your Hasnamuss is: "+planetName+".\n"+
                "You will move "+plyr.getRollMultiple()+" times your dice roll.\n"+
                "Start death with seven cards.\n",
                "Causal Hasnamuss Death",
                JOptionPane.PLAIN_MESSAGE
            );
    }
    public static void showReincarnationMessage(int numBrains) {
        StringBuffer msg = new StringBuffer("As a Hasnamuss with a Kesdjan body, you do not die.\n"+
        "You reincarnated as a "+numBrains+"-brained being.\n");
        if (numBrains < 3) {
            msg.append("One and two-brained beings can only escape reincarnation by cleansing\n"+
                "and do not escape if they crystallize a Mental (or Causal) body.\n");
        } else {
            msg.append("A three-brained Hasnamuss who dies with a Mental body can move\n"+
                "on to one of the divine planets to cleanse themselves.\n");
        }
        msg.append("You start over as you did at the beginning of the game.");
        JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                msg.toString(),
                "Astral Hasnamuss Death",
                JOptionPane.PLAIN_MESSAGE
            );
    }
	/**
	 * Method giveShockChoice.
	 */
	public static String giveShockChoice() {
		return (String)JOptionPane.showInputDialog(ConsciousBoardgameGUI.getInstance(),
					"Which shock would you like for your Wild Shock?",
            		"Wild Shock",
					JOptionPane.QUESTION_MESSAGE,
					null,
					Shocks,
					null);
	}

	/**
	 * Method giveWildChoice.
	 * @param currentPlayer
	 */
	public static void giveWildChoice(Player plyr) {
	    Object [] sp;
	    if (plyr.isDead()) {
	        sp = UIConsts.SpacesAfterDeath;
	    } else {
	        sp = UIConsts.Spaces;
	    }
		BoardSpace bs = null;
		String n = (String)JOptionPane.showInputDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
					"You landed on a wild space, which space do you choose?",
            		"Wild Space",
					JOptionPane.QUESTION_MESSAGE,
					null,
					sp,
					null);
		if (n.equals(Spaces[0])) {
			bs = new FoodSpace();
		} else if (n.equals(Spaces[1])) {
			bs = new AirSpace();
		} else if (n.equals(Spaces[2])) {
			bs = new ImpSpace();
		} else if (n.equals(Spaces[3])) {
			bs = new CardSpace();
		} else if (n.equals(Spaces[4])) {
			bs = new LawSpace();
		}
		if (bs != null)
			bs.landOn(plyr);
		else System.err.println("Invalid Wild Selection, board space null");
	}

	public static void giveDecayChoice(Player plyr) {
		int decay = Dice.roll();
		if (decay == 0) {
			JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
					"You landed on a decay space, but rolled\n"+
					"a zero, decaying nothing.",
					"No Decay",
					JOptionPane.PLAIN_MESSAGE);
		} else {
            System.out.println("dice has "+Dice.getNumSides()+" sides, sides/3="+(Dice.getNumSides()/3));
		    try {
				if (decay <= 3) {
					// either 1, 2, or 3: decay food
					Object [] foodnotes = plyr.getFd().getFoodNotes();
					
					String n = (String)JOptionPane.showInputDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
							"You decay a food piece,\n"+
							"Which food note do you choose to lose?",
		            		"Decay Food",
							JOptionPane.QUESTION_MESSAGE,
							null,
							foodnotes,
							null);
                    if (n == null && !foodnotes[0].equals("(none)")) {
                        // this means they hit cancel and have at least one chip on a note.
                        CBGDlgFactory.displayMessage("You cannot cancel the decay choice.\n" +
                                                     "You lost the first filled food note.");
                       for (int i=0; i<FoodNotes.length; i++) {
                           if (plyr.getFd().takeChip(FOOD,i))
                               break;
                       }
                    } else if (n.equals(FoodNotes[0])) {
						plyr.getFd().takeChip(FOOD, 0);
					} else if (n.equals(FoodNotes[1])) {
						plyr.getFd().takeChip(FOOD, 1);
					} else if (n.equals(FoodNotes[2])) {
						plyr.getFd().takeChip(FOOD, 2);
					} else if (n.equals(FoodNotes[3])) {
						plyr.getFd().takeChip(FOOD, 3);
					} else if (n.equals(FoodNotes[4])) {
						plyr.getFd().takeChip(FOOD, 4);
					} else if (n.equals(FoodNotes[5])) {
						plyr.getFd().takeChip(FOOD, 5);
					} else if (n.equals(FoodNotes[6])) {
						plyr.getFd().takeChip(FOOD, 6);
					} else if (n.equals(FoodNotes[7])) {
						plyr.getFd().takeChip(FOOD, 7);
					} else if (n.equals("(none)")) {
						//System.out.println("No food to decay.");
					} else {
						System.err.println("Error: could not find food decay selection: "+n);
					}
				} else if (decay >= 7) {
					// either 7, 8, or 9: decay impression
					Object [] impnotes = plyr.getFd().getImpNotes();
					String n = (String)JOptionPane.showInputDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
							"You decay a impression piece,\n"+
							"Which impression note do you choose to lose?",
		            		"Decay Impression",
							JOptionPane.QUESTION_MESSAGE,
							null,
							impnotes,
							null);
                    if (n == null && !impnotes[0].equals("(none)")) {
                        // this means they hit cancel and have at least one chip on a note.
                        CBGDlgFactory.displayMessage("You cannot cancel the decay choice.\n" +
                                                     "You lost the first filled impression.");
                       for (int i=0; i<ImpNotes.length; i++) {
                           if (plyr.getFd().takeChip(IMP,i))
                               break;
                       }
                    } else if (n.equals(ImpNotes[0])) {
						plyr.getFd().takeChip(IMP, 0);
					} else if (n.equals(ImpNotes[1])) {
						plyr.getFd().takeChip(IMP, 1);
					} else if (n.equals(ImpNotes[2])) {
						plyr.getFd().takeChip(IMP, 2);
					} else if (n.equals(ImpNotes[3])) {
						plyr.getFd().takeChip(IMP, 3);
					} else if (n.equals("(none)")) {
						//System.out.println("No impressions to decay.");
					} else {
						System.err.println("Error: could not find impression decay selection: "+n);
					}
				} else {
					// either 4, 5, or 6: decay air
					Object [] airnotes = plyr.getFd().getAirNotes();
					String n = (String)JOptionPane.showInputDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
							"You decay a air piece,\n"+
							"Which air note do you choose to lose?",
		            		"Decay Air",
							JOptionPane.QUESTION_MESSAGE,
							null,
							airnotes,
							null);
                    if (n == null && !airnotes[0].equals("(none)")) {
                        // this means they hit cancel and have at least one chip on a note.
                        CBGDlgFactory.displayMessage("You cannot cancel the decay choice.\n" +
                                                     "You lost the first filled air note.");
                       for (int i=0; i<AirNotes.length; i++) {
                           if (plyr.getFd().takeChip(AIR,i))
                               break;
                       }
                    } else if (n.equals(AirNotes[0])) {
						plyr.getFd().takeChip(AIR, 0);
					} else if (n.equals(AirNotes[1])) {
						plyr.getFd().takeChip(AIR, 1);
					} else if (n.equals(AirNotes[2])) {
						plyr.getFd().takeChip(AIR, 2);
					} else if (n.equals(AirNotes[3])) {
						plyr.getFd().takeChip(AIR, 3);
					} else if (n.equals(AirNotes[4])) {
						plyr.getFd().takeChip(AIR, 4);
					} else if (n.equals(AirNotes[5])) {
						plyr.getFd().takeChip(AIR, 5);
					} else if (n.equals("(none)")) {
						//System.out.println("No air to decay.");
					} else {
						System.err.println("Error: could not find air decay selection: "+n);
					}
				}
		    } catch (NoSuchNoteException nsc) {
		        System.err.println(nsc.getMessage());
		        nsc.printStackTrace(System.err);
		    }
		    plyr.getFd().forceChange();
		}
	}
	
	/**
	 * Method showDiceOptionDialog.
	 * @param currentPlayer
	 */
	public static int showDiceOptionDialog(Player plyr, int roll) {
	    BoardSpace landedOn = Board.getSpace(plyr.getBoardPos()+roll);
	    if (plyr.isHasnamuss()) {
	        if (plyr.hasRollAgain()) {
	            if (plyr.hasOppositeRoll()) {
	                roll = showOppositeAndRollAgainOption(plyr, landedOn, roll);
	            } else {
	                roll = showRollAgainOption(plyr, landedOn, roll);
	            }
	        } else if (plyr.hasOppositeRoll()) {
	            roll = showOppositeOption(plyr, landedOn, roll);
	        } else {
                displayMessage("You rolled "+roll+" and landed on a "+landedOn+" space.");   
            }
	        return roll;
	    }
		if (!plyr.getEp().hasAprilFools()) {
			if (plyr.hasOppositeRoll()) {
				if (plyr.hasRollAgain()) {
                    roll = showOppositeAndRollAgainOption(plyr, landedOn, roll);
				} else {
                    roll = showOppositeOption(plyr, landedOn, roll);
				}
			} else if (plyr.hasRollAgain()) {
                roll = showRollAgainOption(plyr, landedOn, roll);
			} else {
				displayMessage("You rolled "+roll+" and landed on a "+landedOn+" space.");
			}
		} else if (!plyr.getEp().has1001Words()) {
			if (plyr.hasOppositeRoll()) {
                roll = showOppositeAndRollAgainOption(plyr, landedOn, roll);
			} else {
                roll = showRollAgainOption(plyr, landedOn, roll);
			}
		} else {
            roll = showOppositeAndRollAgainOption(plyr, landedOn, roll);
		}
        return roll;
	}
	
	private static int showRollAgainOption(Player plyr, BoardSpace landedOn, int roll) {
		Object[] options = {"Take Roll", "Roll Again"};
        int n = JOptionPane.showOptionDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
                        "You moved "+roll+" and landed on a "+landedOn+" space.",
                        "Roll Option",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
        if (n == JOptionPane.NO_OPTION) { // Player rolled again
            roll = Dice.roll()*plyr.getRollMultiple();
        	if (plyr.getBoardPos()+roll >= Board.getNumSpaces()) {
        		roll = Board.getNumSpaces()-1-plyr.getBoardPos();
        	}
            landedOn = Board.getSpace(plyr.getBoardPos()+roll);
			displayMessage("You moved "+roll+" and landed on a "+landedOn+" space.");
			if (!plyr.getEp().hasAprilFools() || plyr.isHasnamuss()) {
				plyr.setRollAgain(false);
			}
        }
        return roll;
	}
	
	private static int showOppositeAndRollAgainOption(Player plyr, BoardSpace landedOn, int roll) {
		BoardSpace opposite = Board.getSpace( plyr.getBoardPos() + Dice.getOpposite()*plyr.getRollMultiple() );
		Object[] options = {"Take Roll",
							"Roll Again",
							"Take Opposite"};
        int n = JOptionPane.showOptionDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
                        "You rolled "+roll+" which would land on a "+landedOn+" space.\n"+
                        "The opposite roll ("+(Dice.getOpposite()*plyr.getRollMultiple())+") is space is a "+opposite,
                        "Roll Option",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
        if (n == JOptionPane.NO_OPTION) {
            // Player took roll again
            roll = Dice.roll()*plyr.getRollMultiple();
        	if (plyr.getBoardPos()+roll >= Board.getNumSpaces()) {
        		roll = Board.getNumSpaces()-1-plyr.getBoardPos();
        	}
            landedOn = Board.getSpace(plyr.getBoardPos()+roll);
			displayMessage("You rolled "+roll+" and landed on a "+landedOn+" space.");
			if (!plyr.getEp().hasAprilFools() || plyr.isHasnamuss()) {
				plyr.setRollAgain(false);
			}
        } else if (n == JOptionPane.CANCEL_OPTION) {
            // Player took opposite roll
        	if (plyr.getBoardPos() + (Dice.getOpposite()*plyr.getRollMultiple()) > Board.getNumSpaces()-1)
        		roll = Board.getNumSpaces()-1-plyr.getBoardPos();
        	else roll = Dice.getOpposite()*plyr.getRollMultiple();
			if (!plyr.getEp().has1001Words() || plyr.isHasnamuss()) {
				plyr.setOppositeRoll(false);
			}
		}
        return roll;
	}
	
	private static int showOppositeOption(Player plyr, BoardSpace landedOn, int roll) {
		BoardSpace opposite = Board.getSpace( plyr.getBoardPos() + Dice.getOpposite()*plyr.getRollMultiple() );
		Object[] options = {"Take Roll",
							"Take Opposite"};
        int n = JOptionPane.showOptionDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
                        "You rolled "+roll+" which would land on a "+landedOn+" space.\n"+
                        "The opposite roll ("+(Dice.getOpposite()*plyr.getRollMultiple())+") is space is a "+opposite,
                        "Roll Option",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
        if (n == JOptionPane.NO_OPTION) {
            // Player wants opposite side
        	if (plyr.getBoardPos()+(Dice.getOpposite()*plyr.getRollMultiple()) > Board.getNumSpaces()-1)
        		roll = Board.getNumSpaces()-1-plyr.getBoardPos();
        	else roll = (Dice.getOpposite()*plyr.getRollMultiple());
			plyr.setOppositeRoll(false);
		}
        return roll;
	}

	public static void showGameCompleted() {
		Object[] options = {"Now", "Maybe later..."};
		int choice = JOptionPane.showOptionDialog(ConsciousBoardgameGUI.getInstance(),
    		"You have completed all aspects of yourself,\n"+
    		"there is nothing more to do but start over.\n"+
    		"\"I Start Over!\"",
    		"Play Again?",
    		JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
    	if (choice == JOptionPane.YES_OPTION) {
    		ConsciousBoardgameGUI.getInstance().removeAll();
    		ConsciousBoardgameGUI.getInstance().dispose();
			ConsciousBoardgameGUI.createAndShowGUI();
		} else {
			ConsciousBoardgameGUI.getInstance().dispose();
			System.exit(0);
		}		
	}

	public static void displayMessage(String text) {
		if (ConsciousBoardgameGUI.getInstance() != null
			&& ConsciousBoardgameGUI.getInstance().isVisible()) {
			JOptionPane.showMessageDialog(
					ConsciousBoardgameGUI.getInstance().getButtonPanel(),
					text);
		}
	}

    /**
     * @param string
     * @param string2
     */
    public static void displayInformationMessage(String title, String msg) {
        if (ConsciousBoardgameGUI.getInstance() != null
    			&& ConsciousBoardgameGUI.getInstance().isVisible()) {
            JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
					msg,
					title,
					JOptionPane.INFORMATION_MESSAGE);
    	}
    }
    /**
     * @param string
     * @param string2
     */
    public static void displayErrorMessage(String title, String msg) {
        if (ConsciousBoardgameGUI.getInstance() != null
    			&& ConsciousBoardgameGUI.getInstance().isVisible()) {
            JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
					msg,
					title,
					JOptionPane.ERROR_MESSAGE);
    	}
    }

    /**
     * 
     */
    public static void showSleepMessage(String stuff) {
        JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance().getButtonPanel(),
                "You are asleep, no "+stuff+" for you.",
				"Asleep",
				JOptionPane.INFORMATION_MESSAGE);
    }

    public static void endGameMessage(String msg) {
        int choice = JOptionPane.showConfirmDialog(ConsciousBoardgameGUI.getInstance(),
                msg,
                "Play Again?",
                JOptionPane.YES_NO_OPTION
                );
            if (choice == JOptionPane.YES_OPTION) {
                ConsciousBoardgameGUI.getInstance().restart();
            } else {
                ConsciousBoardgameGUI.getInstance().dispose();
                System.exit(0);
            }
    }
}
