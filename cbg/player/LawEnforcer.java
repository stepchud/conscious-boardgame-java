package cbg.player;


import javax.swing.JOptionPane;

import cbg.boardParts.*;
import cbg.common.NoSuchNoteException;
import cbg.common.UIConsts;
import cbg.ui.CBGDlgFactory;
import cbg.ui.ConsciousBoardgameGUI;

/**
 * @author Stephen Chudleigh
 * Created on Dec 25, 2005
 */
public class LawEnforcer implements UIConsts {
	
	public static void enforceLawOnPlayer(LawCard c, Player p, boolean forced) {
		c.setObeyed(true);
		if (((p.hasAccidentMoon() && (c.getCard().getSuit()==Card.DIAMONDS))
			|| (p.hasCauseEffectMoon() && (c.getCard().getSuit()==Card.CLUBS))
			|| (p.hasFateMoon() && (c.getCard().getSuit()==Card.HEARTS))
			|| (p.hasWillMoon() && (c.getCard().getSuit()==Card.SPADES))) 
        && (!forced)) {
            String msg =  "You have a King of";
            switch (c.getCard().getSuit()) {
                case Card.SPADES: msg+=" Spades";
                break;
                case Card.HEARTS: msg+=" Hearts";
                break;
                case Card.CLUBS: msg+=" Clubs";
                break;
                case Card.DIAMONDS: msg+=" Diamonds";
                break;
            }
            
			CBGDlgFactory.displayMessage(
               msg+" law that frees you\n"+
                "from the effects of this law."
            );
    			return;
		}
        
		CBGDlgFactory.displayMessage(c.getLawDesc());
		LawCard lc = null;
		switch (c.getLawValue()) {
			default:
				System.err.println("Don't know how to obey law card # "+c.getLawValue()+", the "+c.getCard());
				CBGDlgFactory.displayInformationMessage("Eternal Forgiveness by Ignorance",
                        "Can't obey law card # "+c.getLawValue()+", the "+c.getCard());
				break;
			case 1:
			    p.getFd().enterChips(new short[]{0,0,1,0,0,0,0,0},
			            			 null,
			            			 null);
				break;
			case 2:
				createAllFood(p.getFd());
				break;
			case 3:
				p.getFd().clear(FOOD, 6);
				break;
			case 4:
				createFirst3Food(p.getFd());
				break;
			case 5:
				p.getFd().clear(IMP, 1);
				break;
			case 6:
			    p.getFd().enterChips(new short[]{0,0,0,1,0,0,0,0},
			            			 new short[]{0,1,0,0,0,0},
			            			 null);
				break;
			case 7:
			    p.getFd().enterChips(new short[]{0,0,0,1,1,0,0,0},
			            			 null,
			            			 null);
				break;
			case 8:
				p.getFd().clear(FOOD, 0);
				break;
			case 9:
				p.getFd().clear(FOOD, 3);
				p.getFd().clear(FOOD, 4);
				break;
			case 10:
				p.getFd().enterChips(new short[]{1,0,0,0,0,0,0,0},
				        			 null,
				        			 null);
				break;
			case 11:
				p.getFd().clear(FOOD, 2);
				p.getFd().clear(AIR, 0);
				break;
			case 12:
				p.getFd().enterChips(new short[]{0,0,0,0,0,1,1,0},
				        			 null,
				        			 null);
				break;
			case 13:
				p.getFd().enterChips(null,
				        			 new short[]{0,0,0,0,0,1},
				        			 null);
				break;
			case 14:
				p.getFd().clear(IMP, 3);
				p.getFd().clearNextImressionAfter6();
				break;
			case 15:
				p.getFd().enterChips(new short[]{0,0,0,0,0,0,0,1},
				        			 null,
				        			 null);
				break;
			case 16:
				p.getFd().clear(AIR, 1);
				p.getFd().clear(FOOD, 3);
				break;
			case 17:
				p.getFd().clearOctave(AIR);
				break;
			case 18:
				p.getFd().enterChips(new short[]{0,0,0,0,0,1,0,0},
				        			 new short[]{0,0,0,1,0,0},
				        			 new short[]{0,1,0,0});
				break;
			case 19:
				p.setAsleepTil(p.getBoardPos()+21);
				p.addActiveLaw(c);
				break;
			case 20:
			    //Transform Emotions
				p.getFd().performShocks(new short[]{0,1,0,0});
				break;
			case 21:
				p.addActiveLaw(c);
				break;
			case 22:
				ConsciousBoardgameGUI.instantDeath(p);
				break;
			case 23:
				for (int i=1; i<p.getType(); i++) {
					Decks.drawLawCard();
				}
				lc = Decks.drawLawCard();
				//CBGDlgFactory.displayMessage(lc.getLawDesc());
				enforceLawOnPlayer(lc, p, true);
				ConsciousBoardgameGUI.getInstance().addLawToPlay(lc);
				break;
			case 24:
			    p.getFd().enterChips(new short[]{0,0,0,0,0,0,1,0},
			            			 new short[]{0,0,0,0,1,0},
			            			 new short[]{0,0,1,0});
				break;
			case 25:
				createAllImpressions(p);
				break;
			case 26:
				p.getFd().clear(IMP, 3);
				break;
			case 27:
				p.giveMCMoment();
				break;
			case 28:
				p.getFd().clear(AIR, 0);
				p.getFd().clear(AIR, 1);
				p.getFd().clear(AIR, 2);
				break;
			case 29:
				p.getFd().clear(IMP, 0);
				break;
			case 30:
				if (!p.getFd().hasTI12())
					p.getFd().moveNextHighestToTI12();
				p.getFd().transformAll12();
				break;
			case 31:
			    p.getFd().enterChips(null,
			            			 new short[]{1,0,0,0,0,0},
			            			 null);
				break;
			case 32:
				p.getFd().clear(IMP, 0);
				p.getFd().clear(AIR, 0);
				p.getFd().clear(AIR, 1);
				p.getFd().clear(AIR, 2);
				p.getFd().clear(FOOD, 0);
				p.getFd().clear(FOOD, 1);
				p.getFd().clear(FOOD, 2);
				break;
			case 33:
				p.getFd().clear(AIR, 2);
				break;
			case 34:
				p.getFd().clear(FOOD, 6);
				p.getFd().clear(FOOD, 5);
				break;
			case 35:
			    p.getFd().enterChips(new short[]{0,0,0,0,1,0,0,0},
			            			 new short[]{0,0,1,0,0,0},
			            			 new short[]{1,0,0,0});
				break;
			case 36:
				p.getFd().clear(FOOD, 7);
				p.getFd().clearNextFoodAfter6();
				break;
			case 37:
				p.getFd().clear(FOOD, 4);
				p.getFd().clear(AIR, 2);
				p.getFd().clear(IMP, 0);
				break;
			case 38:
				createAll6(p);
				break;
			case 39:
			    RisingChipModel rcm = new RisingChipModel();
				p.getFd().shocksFood(rcm);
				p.getFd().enterChips(rcm);
				break;
			case 40:
				p.getFd().clear(IMP, 3);
				p.getFd().clear(IMP, 2);
				p.getFd().clear(IMP, 1);
				p.getFd().clear(IMP, 0);
				break;
			case 41:
				if (Board.getNumSpaces() <= p.getBoardPos()+37)
					p.setNoSkillsTil(Board.getNumSpaces()-1);
				else p.setNoSkillsTil(p.getBoardPos()+37);
				p.addActiveLaw(c);
				break;
			case 42:
				p.getFd().performShocks(new short[]{1,0,0,0});
				break;
			case 43:
				p.addActiveLaw(c);
				break;
			case 44:
				p.addActiveLaw(c);
				JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
					"Death comes in 41 spaces.",
					"Cancer",
					JOptionPane.WARNING_MESSAGE);
				if ((p.getBoardPos()+41)<(Board.getNumSpaces()-1))
					p.setDeathSpace(p.getBoardPos()+41);
				break;
			case 45:
				p.getFd().clear(FOOD, 7);
				break;
			case 46:
				p.getFd().clear(AIR, 0);
				break;
			case 47:
				p.drawPOCCard(true);
				break;
			case 48:
				p.getFd().clear(AIR, 5);
				break;
			case 49:
				p.getFd().clear(IMP, 2);
				p.getFd().clear(IMP, 1);
				break;
			case 50:
				if (p.getLawHand().size()>0)
					ConsciousBoardgameGUI.getInstance().showLawsDialog(true);
				else
					JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
							"There were no law cards in your law hand.\n"
							+"You get no law by choice.",
							"No Law Cards",
							JOptionPane.INFORMATION_MESSAGE);
				break;
			case 51:
			    p.getFd().enterChips(null,
			            			 new short[]{0,0,0,1,1,0},
			            			 null);
				break;
			case 52:
				p.getFd().clear(FOOD, 2);
				break;
			case 53:
				p.drawLawCard();
				p.drawLawCard();
				p.drawLawCard();
				break;
			case 54:
				for (int i=0; i<5; i++)
					p.drawPOCCard(true);
				break;
			case 55:
				p.getFd().shockAllMI48();
				break;
			case 56:
				p.getFd().clear(IMP, 2);
				p.getFd().clear(AIR, 4);
				p.getFd().clear(FOOD, 6);
				break;
			case 57:
			    p.getFd().enterChips(new short[]{0,0,1,0,0,0,0,0},
			            			 new short[]{1,0,0,0,0,0},
			            			 null);
				break;
			case 58:
				p.getFd().clearOctave(FOOD);
				break;
			case 59:
				p.setNoPowersTil(p.getBoardPos()+33);
				p.addActiveLaw(c);
				ConsciousBoardgameGUI.getInstance().disablePowerButtons();
				break;
			case 60:
				p.setOppositeRoll(true);
				break;
			case 61:
				p.addActiveLaw(c);
				break;
			case 62:
				p.addActiveLaw(c);
				JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
					"Death comes in 27 spaces.",
					"Family Disease",
					JOptionPane.WARNING_MESSAGE);
				if ((p.getBoardPos()+27)<Board.getNumSpaces())
					p.setDeathSpace(p.getBoardPos()+27);
				break;
			case 63:
				break;
			case 64:
				lc = Decks.drawLawCard();
				//CBGDlgFactory.displayMessage(lc.getLawDesc());
				enforceLawOnPlayer(lc, p, true);
				ConsciousBoardgameGUI.getInstance().addLawToPlay(lc);
				break;
			case 65:
			    p.getFd().enterChips(null,
			            			 null,
			            			 new short[]{1,0,0,0});
				break;
			case 66:
				try {
                    if (p.getFd().takeChip(IMP, 0)) {
                    	p.getFd().clear(IMP, 0);
                    } else {
                    	if (!p.getFd().takeChip(IMP, 1)) {
                    		if (p.getFd().takeChip(IMP, 2)) {
                    			p.getFd().clear(IMP, 2);
                    		} else {
                    			p.getFd().clear(IMP, 3);
                    		}
                    	}
                    }
                } catch (NoSuchNoteException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace(System.err);
                }
				break;
			case 67:
			    p.getFd().moveMI192toTI12();
				break;
			case 68:
				p.getFd().clear(FOOD, 0);
				p.getFd().clear(FOOD, 1);
				p.getFd().clear(FOOD, 2);
				break;
			case 69:
			    p.getFd().enterChips(null,
			            			 null,
			            			 new short[]{0,1,0,0});
				break;
			case 70:
				p.getFd().clear(FOOD, 5);
				p.getFd().clear(AIR, 3);
				p.getFd().clear(IMP, 1);
				break;
			case 71:
				p.getFd().clear(AIR, 3);
				p.getFd().clear(AIR, 4);
				break;
			case 72:
				p.setLawPlaysThis(1);
				ConsciousBoardgameGUI.getInstance().showLawsDialog(false);
				break;
			case 73:
			    p.getFd().enterChips(null,
			            			 null,
			            			 new short[]{0,0,1,0});
				break;
			case 74:
				p.getFd().clear(IMP, 2);
				break;
			case 75:
				p.getLawHand().clear();
				break;
			case 76:
				p.getFd().clear(AIR, 5);
				try {
                    if (!p.getFd().takeChip(AIR, 4)) {
                    	if (!p.getFd().takeChip(AIR, 3)) {
                    		if (p.getFd().takeChip(AIR, 2)) {
                    			p.getFd().clear(AIR, 2);
                    		} else {
                    			if (!p.getFd().takeChip(AIR, 1))
                    				p.getFd().takeChip(AIR, 0);
                    		}
                    	}
                    }
                } catch (NoSuchNoteException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace(System.err);
                }
				break;
			case 77:
			    p.getFd().enterChips(null,new short[]{0,0,0,0,0,1},null);
				break;
			case 78:
				java.util.Random rand = new java.util.Random(System.currentTimeMillis());
				int index = rand.nextInt(p.getPocHand().size());
				p.getPocHand().remove(index);
				break;
			case 79:
				p.setRollAgain(true);
				break;
			case 80:
				int half = p.getPocHand().size() / 2;
				java.util.Random r1 = new java.util.Random(System.currentTimeMillis());
				for (int i=0; i<half; i++) {
					p.getPocHand().remove(r1.nextInt(p.getPocHand().size()));
				}
				break;
			case 81:
				p.getFd().clear(FOOD, 7);
				p.getFd().clear(AIR, 5);
				p.getFd().clear(IMP, 3);
				break;
			case 82:
			    p.getFd().performShocks(new short[]{0,0,0,1});
				break;
			case 83:
				p.addActiveLaw(c);
				break;
			case 84:
                ConsciousBoardgameGUI.instantDeath(p);
				break;
			case 85:
				p.removeAllLawsButJoker();
                if (p.isHasnamuss()) p.cleanseHasnamuss();
				break;
			case 86:
			    p.makeHasnamuss();
                p.addActiveLaw(c);
			    break;
		}
		p.getFd().checkComplete();
		p.getFd().forceChange();
	}




	/**
	 * Method createAll6.
	 * @param p
	 */
	private static void createAll6(Player p) {
		p.getFd().enterChips(new short[] {0,0,0,0,0,0,0,1},
		        		 	 new short[] {0,0,0,0,0,1},
		        		 	 new short[] {0,0,0,1});
	}


	/**
	 * Method createAllImpressions.
	 * @param p
	 */
	private static void createAllImpressions(Player p) {
		p.getFd().enterChips(null,
		        			 null,
		        			 new short[] {1,1,1,1});
	}


	/**
	 * Method createFirst3Food.
	 * @param p
	 */
	private static void createFirst3Food(FoodDiagram fd) {
		fd.enterChips(new short[] {1,1,1,0,0,0,0,0},
		        	  null,
		        	  null);
	}

	/**
	 * Method createAllFood.
	 * @return ResultAction
	 */
	private static void createAllFood(FoodDiagram fd) {
		fd.enterChips(new short[] {1,1,1,1,1,1,1,1},
		        	  null,
		        	  null);
	}
	
}
