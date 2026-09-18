package cbg.player;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import cbg.common.NoSuchNoteException;
import cbg.ui.CBGDlgFactory;

/**
 * @author Stephen Chudleigh
 * CouciousBoardgame
 * Feb 24, 2004
 */
public class FoodDiagram implements cbg.common.UIConsts {
	
    private Player player;
	private int foodAstral, airAstral, impAstral,
				foodMental, airMental, impMental;
				
	private boolean hasChrystallizedAstral,
					hasChrystallizedMental,
					hasMovedMental,
					hasMovedAstral,
					isComplete;

	////////	FOOD	///////
	private Note [] foodOctave = new Note[8];
	//DO768, RE384, MI192, FA96, SO48, LA24, TI12, DO6;
	////////	AIR		///////
	private Note [] airOctave = new Note[6];
	//private Note DO192, RE96, MI48, FA24, SO12, LA6;
	////////	IMP		///////
	private Note [] impOctave = new Note[4];

	private PropertyChangeSupport changes =
        new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener l) {
	    changes.addPropertyChangeListener(l);
	}
	public void removePropertyChangeListener(PropertyChangeListener l) {
	    changes.removePropertyChangeListener(l);
	}
	
	private class Note {
		private int maxChips;
		private int chips;
		private boolean hasAstral;
		private boolean hasMental;
		
		private Note(int max) {
			maxChips=max;
		}
		
		private int getChips() {
			return chips;
		}
		private boolean hasChip() {
			return (chips > 0);
		}
		private boolean hasAstral() {
			return hasAstral;
		}
		private boolean hasMental() {
			return hasMental;
		}
		private void setChips(int i) {
			chips = i;
		}
		private boolean addChip() {
			if (!this.isFull()) {
				chips++;
				return true;
			} else return false;
		}
		private boolean takeChip() {
			if (chips > 0) {
				chips--;
				return true;
			} else return false;
		}
		private void clearChips() {
			chips=0;
		}
		private void setHasAstral(boolean b) {
			hasAstral = b;
		}
		private void setHasMental(boolean b) {
			hasMental = b;
		}
		private boolean isFull() {
			return (chips == maxChips);
		}
		private byte getState() {
			byte state=0;
			if (hasChip())
				state += 1;
			if (hasAstral())
				state += 2;
			if (hasMental())
				state += 4;
			return state;
		}
	}  ///////////// End Note Class /////////

	public FoodDiagram(Player p) {
	    this.player = p;
		//// Create Notes, Fill 3-3-1
		// Food
		foodOctave[0] = new Note(1);
		foodOctave[1] = new Note(1);
		foodOctave[2] = new Note(3);
		foodOctave[3] = new Note(1);
		foodOctave[4] = new Note(1);
		foodOctave[5] = new Note(1);
		foodOctave[6] = new Note(3);
		foodOctave[7] = new Note(1);
		// Air
		airOctave[0] = new Note(1);
		airOctave[1] = new Note(1);
		airOctave[2] = new Note(3);
		airOctave[3] = new Note(1);
		airOctave[4] = new Note(1);
		airOctave[5] = new Note(1);
		// Impressions
		impOctave[0] = new Note(3);
		impOctave[1] = new Note(1);
		impOctave[2] = new Note(3);
		impOctave[3] = new Note(1);
        
        give331();
    }
    
    /**
     *  Put 3-3-1 chips to start off
     **/
    public void give331() {
        foodOctave[0].addChip();
        foodOctave[1].addChip();
        foodOctave[2].addChip();
        airOctave[0].addChip();
        airOctave[1].addChip();
        airOctave[2].addChip();
        impOctave[0].addChip();
    }
    
    public boolean has192() {
		return ((foodOctave[2].hasChip()) || (airOctave[0].hasChip()));
	}
	public boolean has96() {
		return ((foodOctave[3].hasChip()) || (airOctave[1].hasChip()));
	}
	public boolean has48() {
		return ((impOctave[0].hasChip()) || (airOctave[2].hasChip()) || (foodOctave[4].hasChip()));
	}
	public boolean has24() {
		return ((impOctave[1].hasChip()) || (airOctave[3].hasChip()) || (foodOctave[5].hasChip()));
	}
	public boolean has12() {
		return ((impOctave[2].hasChip()) || (airOctave[4].hasChip()) || (foodOctave[6].hasChip()));
	}
	public boolean has6() {
		return ((foodOctave[7].hasChip()) || (airOctave[5].hasChip()) || (impOctave[3].hasChip()));
	}
	public boolean hasTI12() {
		return foodOctave[6].hasChip();
	}
	public boolean hasMI48() {
		return airOctave[2].hasChip();
	}
	public boolean hasMI192() {
		return foodOctave[2].hasChip();
	}
	public void eat() {
		enterChips(new short[]{1,0,0,0,0,0,0,0},
					null,
					null);

	}
	public void breathe() {
	    enterChips(null,
	            new short[]{1,0,0,0,0,0},
				null);
	}
	public void impression() {
	    enterChips(null,
	            null,
	            new short[]{1,0,0,0});
	}
	/**
	 * Method performShocks.
	 * @param shocks
	 */
	public void performShocks(short[] shocks) {
		//System.err.println(shocks[0]+" self rememberings, "
    	//						+shocks[1]+" transform emotions, "
    	//						+shocks[2]+" wild shocks, "+
    	//						+shocks[3]+" all shocks.");
	    RisingChipModel rcm = new RisingChipModel();
		short i=0;
		for (i=0; i<shocks[3]; i++) {
			allShocks(rcm);
		}
		for (i=0; i<shocks[2]; i++) {
			String choice = CBGDlgFactory.giveShockChoice();
			System.out.println("You chose "+choice+" for your wild shock.");
			if (choice.equals(Shocks[0])) {
				shocksFood(rcm);
			} else if (choice.equals(Shocks[1])) {
				selfRemember(rcm);
			} else if (choice.equals(Shocks[2])) {
				transformEmotions(rcm);
			} else {
				System.err.println("You chose an invalid shock "+choice);
			}
		}
		for (i=0; i<shocks[1]; i++) {
			transformEmotions(rcm);
		}
		for (i=0; i<shocks[0]; i++) {
			selfRemember(rcm);
		}
		
		this.enterChips(rcm);
		checkComplete();
	}
	public void shocksFood(RisingChipModel rcm) {
		if (foodOctave[2].hasChip()) {
			foodOctave[2].takeChip();
			rcm.add(FOOD, (short)3);
		}
	}
	public void selfRemember(RisingChipModel rcm) {
		if (impOctave[0].hasChip()) {
			impOctave[0].takeChip();
			rcm.add(IMP, (short)1);
		}
	}
	public void transformEmotions(RisingChipModel rcm) {
		if (foodOctave[6].hasChip()) {
			foodOctave[6].takeChip();
			rcm.add(FOOD, (short)7);
		}
		if (impOctave[2].hasChip()) {
			impOctave[2].takeChip();
			rcm.add(IMP, (short)3);
		}
	}
	
    /**
     * 
     */
    public void allShocks(RisingChipModel rcm) {
        if (foodOctave[2].hasChip()) {
            foodOctave[2].takeChip();
            rcm.add(FOOD, (short)3);
        }
        if (foodOctave[6].hasChip()) {
            foodOctave[6].takeChip();
            rcm.add(FOOD, (short)7);
        }
        if (impOctave[2].hasChip()) {
            impOctave[2].takeChip();
            rcm.add(IMP, (short)3);
        }
        if (impOctave[0].hasChip()) {
            impOctave[0].takeChip();
            rcm.add(IMP, (short)1);
        }
    }
	
	public void enterChips(short[] f, short[] a, short[] i) {
	    RisingChipModel rcm = new RisingChipModel(f, a, i);
	    enterChips(rcm);
	}
	
	public void enterChips(RisingChipModel rcm) {
	    // Get rid of chips that can not rise
	    if (rcm.get(FOOD, (short)1)>0) {
	        foodOctave[1].addChip();
	        rcm.set(FOOD, (short)1, (short)0);
	    }
	    if (rcm.get(FOOD, (short)4)>0) {
	        foodOctave[4].addChip();
	        rcm.set(FOOD,(short)4, (short)0);
	    }
	    if (rcm.get(FOOD, (short)5)>0) {
	        foodOctave[5].addChip();
	        rcm.set(FOOD, (short)5, (short)0);
	    }
	    if (rcm.get(AIR,(short)4)>0) {
	        airOctave[4].addChip();
	        rcm.set(AIR, (short)4, (short)0);
	    }
	    
	    // Handle automatic shocks
	    if (rcm.get(IMP, (short)1)>0) {	// RE-24 shocks air
	        short mi48 = rcm.get(AIR, (short)2);
	        if ((mi48>0) && !airOctave[2].hasChip()) {
	            airOctave[2].addChip();
	            rcm.set(AIR, (short)2, (short)(mi48-1));
	        }
	        if (airOctave[2].takeChip()) {
	            rcm.add(AIR, (short)3);
	        }
	    }
	    if (rcm.get(AIR,(short)1)>0) {	// RE-96 shocks food
	        short mi192 = rcm.get(FOOD, (short)2);
	        if ((mi192>0) && !foodOctave[2].hasChip()) {
	            foodOctave[2].addChip();
	            rcm.set(FOOD, (short)2, (short)(mi192-1));
	        }
	        if (foodOctave[2].takeChip()) {
	            rcm.add(FOOD, (short)3);
	        }
	        airOctave[1].addChip();
	        rcm.set(AIR, (short)1, (short)0);
	    }

	    rcm.process(this);
	    forceChange();
	}
    
    /**
     * 
     */
    void handleNewPieces(RisingChipModel rcm) {
        short [] food = {0,0,0,0,0,0,0,0};
        short [] air = {0,0,0,0,0,0};
        int extraFood, extraAir, extraImp = 0;
        if (!hasChrystallizedAstral) {
            foodAstral += rcm.excessFood();
            airAstral += rcm.excessAir();
            impAstral += rcm.excessImp();
            if ((airAstral >= 3) && (foodAstral >= 3) && (impAstral >=1)) {
				hasChrystallizedAstral = true;
				CBGDlgFactory.displayMessage("You have chrystallized the body Kesdjan!");
				movePieces();
				return;
			}
            if (foodAstral > 3) {
                int drawCards = foodAstral-3;
                foodAstral=3;
                CBGDlgFactory.displayMessage("You drew "+drawCards+" card for extra astral food.");
                for (int i=0; i<drawCards; i++) {
                    player.drawPOCCard(true);
                }
            }
            if (airAstral > 3) {
                extraAir = airAstral-3;
				airAstral=3;
				for (int i=0; i<extraAir; i++) {
					int n = CBGDlgFactory.giveAirChoice();
					if (n == JOptionPane.YES_OPTION)
						food[0]++;
					else {
						player.drawPOCCard(true);
					}
				}
			}
            if (impAstral > 1) {
			    extraImp = impAstral-1;
				impAstral=1;
				for (int i=0; i<extraImp; i++) {
					int n = CBGDlgFactory.giveImpChoice();
					if (n == JOptionPane.YES_OPTION)
						air[0]++;
					else {
						player.drawPOCCard(true);
					}
				}
			}
            enterChips(food, air, null);
            return;
        } else if (!hasChrystallizedMental) {
            foodAstral += rcm.excessFood();
            if (foodAstral>8) {
                foodMental += (foodAstral-8);
                foodAstral=8;
            }
            airAstral += rcm.excessAir();
            if (airAstral>6) {
                airMental += (airAstral-6);
                airAstral=6;
            }
            impAstral += rcm.excessImp();
            if (impAstral>4) {
                impMental += (impAstral-4);
                impAstral=4;
            }
            
            if ((airMental >= 3) && (foodMental >= 3) && (impMental >= 1)) {
				hasChrystallizedMental = true;
				System.out.println("You have chrystallized the mental body!");
                if (player.isDead()) {
                    CBGDlgFactory.displayMessage("You chrystallized your Mental Body after death.\n"
                            +"At the end of this turn, you will discard the \n"
                            +"astral body for the mental.");
                } else {
                    CBGDlgFactory.displayMessage("You have chrystallized your Mental Body.");
                    movePieces();
                }
				return;
			}
            if (foodMental > 3) {
				extraFood = foodMental-3;
				foodMental=3;
				for (int i=0; i<extraFood; i++) {
				    player.drawPOCCard(true);
				}
				CBGDlgFactory.displayMessage("You drew "+extraFood+ " cards for excess food stuff.");
			}
            if (airMental > 3) {
                extraAir = airMental-3;
				airMental=3;
				for (int i=0; i<extraAir; i++) {
					int n = CBGDlgFactory.giveAirChoice();
					if (n == JOptionPane.YES_OPTION)
						food[0]++;
					else {
						player.drawPOCCard(true);
					}
				}
			}
			if (impMental > 1) {
			    extraImp = impMental-1;
				impMental=1;
				for (int i=0; i<extraImp; i++) {
					int n = CBGDlgFactory.giveImpChoice();
					if (n == JOptionPane.YES_OPTION)
						air[0]++;
					else {
						player.drawPOCCard(true);
					}
				}
			}
			enterChips(food, air, null);
			return;
        } else {
            foodMental += rcm.excessFood();
            airMental += rcm.excessAir();
            impMental += rcm.excessImp();
            if (foodMental>8) {
                extraFood = foodMental-8;
                foodMental=8;
                for (int i=0; i<extraFood; i++) {
				    player.drawPOCCard(true);
				}
				CBGDlgFactory.displayMessage("You drew "+extraFood+ " cards for excess food stuff.");
            }
            if (airMental>6) {
                extraAir = airMental-6;
                airMental=6;
                for (int i=0; i<extraAir; i++) {
					int n = CBGDlgFactory.giveAirChoice();
					if (n == JOptionPane.YES_OPTION)
						food[0]++;
					else {
						player.drawPOCCard(true);
					}
				}
            }
            if (impMental>4) {
                extraImp = impMental-4;
                impMental=4;
                for (int i=0; i<extraImp; i++) {
					int n = CBGDlgFactory.giveImpChoice();
					if (n == JOptionPane.YES_OPTION)
						air[0]++;
					else {
						player.drawPOCCard(true);
					}
				}
            }
            enterChips(food, air, null);
            return;
        }
    }

	public void shiftAfterDeath() {
        System.out.println("FD.shiftAfterDeath:");
		if (this.hasChrystallizedMental) {
			if (!this.hasMovedMental) {
                System.out.println("moving mental");
			    /* mental body pieces become chips
				 * discard all pieces, leave first note
				 */
				for (int i=1; i<foodOctave.length; i++) {
					foodOctave[i].setChips(0);
					foodOctave[i].setHasAstral(false);
					foodOctave[i].setHasMental(false);
				}
				for (int i=1; i<airOctave.length; i++) {
					airOctave[i].setChips(0);
					airOctave[i].setHasAstral(false);
					airOctave[i].setHasMental(false);
				}
				for (int i=1; i<impOctave.length; i++) {
					impOctave[i].setChips(0);
					impOctave[i].setHasAstral(false);
					impOctave[i].setHasMental(false);
				}
				for (int i=0; i<foodMental; i++) {
					foodOctave[i].setChips(1);
				}
				for (int i=0; i<airMental; i++) {
					airOctave[i].setChips(1);
				}
				for (int i=0; i<impMental; i++) {
					impOctave[i].setChips(1);
				}
				if (player.isDead()) {
					// fill mental from pieces
					foodMental = 8;
					airMental = 6;
					impMental = 4;
				}
				hasMovedMental = true;
			}
		} else if (this.hasChrystallizedAstral) {
		    if (!this.hasMovedAstral) {
                System.out.println("moving astral");
				// leave the first note in each octave alone
				for (int i=1; i<foodOctave.length; i++) {
					foodOctave[i].setChips(0);
					foodOctave[i].setHasAstral(false);
					foodOctave[i].setHasMental(false);
				}
				for (int i=1; i<airOctave.length; i++) {
					airOctave[i].setChips(0);
					airOctave[i].setHasAstral(false);
					airOctave[i].setHasMental(false);
				}
				for (int i=1; i<impOctave.length; i++) {
					impOctave[i].setChips(0);
					impOctave[i].setHasAstral(false);
					impOctave[i].setHasMental(false);
				}
				for (int i=0; i<foodAstral; i++) {
					foodOctave[i].setChips(1);
				}
				for (int i=0; i<airAstral; i++) {
					airOctave[i].setChips(1);
				}
				for (int i=0; i<impAstral; i++) {
					impOctave[i].setChips(1);
				}
				if (player.isDead()) {
					// fill astral from pieces
					foodAstral = 8;
					airAstral = 6;
					impAstral = 4;
				}
				hasMovedAstral = true;
		    }
		}
		this.forceChange();
	}
	
	public void clear(int type, int noteNumber) {
		if (type == FOOD) {
			foodOctave[noteNumber].clearChips();
		} else if (type == AIR) {
			airOctave[noteNumber].clearChips();
		} else if (type == IMP) {
			impOctave[noteNumber].clearChips();			
		}
	}
	
	/**
	 * Method takeChip.
	 * @param AIR
	 * @param i
	 */
	public boolean takeChip(int octave, int noteNumber) throws NoSuchNoteException {
		if (octave == FOOD) {
			return foodOctave[noteNumber].takeChip();
		} else if (octave == AIR) {
			return airOctave[noteNumber].takeChip();
		} else if (octave == IMP) {
			return impOctave[noteNumber].takeChip();
		} else 
		    throw new NoSuchNoteException(
		        "Octave "+octave+" / Note #"+noteNumber+" not valid."
		    );
	}

    /**
     * @param food
     * @param i
     */
    public boolean putChip(int octave, int noteNumber) throws NoSuchNoteException {
        if (octave == FOOD) {
			return foodOctave[noteNumber].addChip();
		} else if (octave == AIR) {
			return airOctave[noteNumber].addChip();
		} else if (octave == IMP) {
			return impOctave[noteNumber].addChip();
		} else
		    throw new NoSuchNoteException(
			        "Octave "+octave+" / Note #"+noteNumber+" not valid."
			    );
    }
	
	/**
	 * Method clears chips from the 'type' octave.
	 */
	public void clearOctave(int type) {
		if (type == FOOD) {
			for (int i=0; i<foodOctave.length; i++)
				foodOctave[i].clearChips();
		} else if (type == AIR) {
			for (int i=0; i<airOctave.length; i++)
				airOctave[i].clearChips();
		} else if (type == IMP) {
			for (int i=0; i<impOctave.length; i++)
				impOctave[i].clearChips();			
		}
	}
	
	/**
	 * Method clearNextImressionAfter6.
	 */
	public void clearNextImressionAfter6() {
		if (impOctave[2].hasChip())
			impOctave[2].clearChips();
		else if (impOctave[1].hasChip())
			impOctave[1].clearChips();
		else if (impOctave[0].hasChip())
			impOctave[0].clearChips();
	}
	
	/**
	 * Method clearNextFoodAfter6.
	 */
	public void clearNextFoodAfter6() {
		if (foodOctave[6].hasChip())
			foodOctave[6].clearChips();
		else if (foodOctave[5].hasChip())
			foodOctave[5].clearChips();
		else if (foodOctave[4].hasChip())
			foodOctave[4].clearChips();
		else if (foodOctave[3].hasChip())
			foodOctave[3].clearChips();
		else if (foodOctave[2].hasChip())
			foodOctave[2].clearChips();
		else if (foodOctave[1].hasChip())
			foodOctave[1].clearChips();
		else if (foodOctave[0].hasChip())
			foodOctave[0].clearChips();
	}

	/**
	 * Method getCurrentState returns an array of bytes
	 * of the state of each note.
	 * Array Positions are:
	 * 0-7 = FOOD OCTAVE
	 * 8-13 = AIR OCTAVE
	 * 14-17 = IMPRESSIONS OCTAVE
	 * @return byte[]
	 */
	public byte[] getCurrentState() {
		byte [] status = new byte[18];
		for (int i=0; i<foodOctave.length; i++)
			status[i] = foodOctave[i].getState();
		for (int i=0; i<airOctave.length; i++)
			status[i+8] = airOctave[i].getState();
		for (int i=0; i<impOctave.length; i++)
			status[i+14] = impOctave[i].getState();
		return status;
	}
	/**
	 * Method getAccrualStates returns number of accum chips
	 * in the following order:
	 * MI192, MI48, DO48, TI12, MI12
	 * @return byte[]
	 */
	public byte[] getAccrualStates() {
		byte[] accrues = new byte[5];
		accrues[0] = (byte)((foodOctave[2].getChips()-1 < 0) ? 0 : foodOctave[2].getChips()-1);
		accrues[1] = (byte)((airOctave[2].getChips()-1 < 0) ? 0 : airOctave[2].getChips()-1);
		accrues[2] = (byte)((impOctave[0].getChips()-1 < 0) ? 0 : impOctave[0].getChips()-1);
		accrues[3] = (byte)((foodOctave[6].getChips()-1 < 0) ? 0 : foodOctave[6].getChips()-1);
		accrues[4] = (byte)((impOctave[2].getChips()-1 < 0) ? 0 : impOctave[2].getChips()-1);
		//System.out.println("accrue states:");
		//for (int i=0; i<accrues; i++)
		//	System.out.print("accrues["+i+"]="+accrues[i]+"  ");
		return accrues;
	}
	
	/**
	 * Method getAccumStates.
	 * @return byte[]
	 */
	public byte[] getAccumStates() {
		byte[] accums = new byte[15];

		if (!hasChrystallizedAstral || (hasChrystallizedAstral && !foodOctave[0].hasAstral())) {
			// Astral Accumulation
			for (int i=0; i<foodAstral; i++)
				accums[i] = ASTRAL;
			for (int i=0; i<airAstral; i++)
				accums[5+i] = ASTRAL;
			for (int i=0; i<impAstral; i++)
				accums[11+i] = ASTRAL;
		} else if (!hasChrystallizedMental || (hasChrystallizedMental && !foodOctave[0].hasMental())) {
			// Mental Accumulation
			for (int i=0; i<foodMental; i++)
				accums[i] = MENTAL;
			for (int i=0; i<airMental; i++)
				accums[5+i] = MENTAL;
			for (int i=0; i<impMental; i++)
				accums[11+i] = MENTAL;
		}
		return accums;
	}

	public String toString() {
		StringBuffer fdbuf = new StringBuffer("Food : DO768("+foodOctave[0].getChips()+")");
		fdbuf.append(" RE384("+foodOctave[1].getChips()+")");
		fdbuf.append(" MI192("+foodOctave[2].getChips()+")");
		fdbuf.append(" FA96("+foodOctave[3].getChips()+")");
		fdbuf.append(" SO48("+foodOctave[4].getChips()+")");
		fdbuf.append(" LA24("+foodOctave[5].getChips()+")");
		fdbuf.append(" TI12("+foodOctave[6].getChips()+")");
		fdbuf.append(" DO6("+foodOctave[7].getChips()+")\n");
		fdbuf.append("Air :\t\t\t\t\t DO192("+airOctave[0].getChips()+")");
		fdbuf.append(" RE96("+airOctave[1].getChips()+")");
		fdbuf.append(" MI48("+airOctave[2].getChips()+")");
		fdbuf.append(" FA24("+airOctave[3].getChips()+")");
		fdbuf.append(" SO12("+airOctave[4].getChips()+")");
		fdbuf.append(" LA6("+airOctave[5].getChips()+")\n");
		fdbuf.append("Impressions :\t\t\t\t\t\t\t  DO48("+impOctave[0].getChips()+")");
		fdbuf.append(" RE24("+impOctave[1].getChips()+")");
		fdbuf.append(" MI12("+impOctave[2].getChips()+")");
		fdbuf.append(" FA6("+impOctave[3].getChips()+")\n");		
		return fdbuf.toString();
	}
	/**
	 * Method leaveMI192.
	 */
	public boolean leaveMI192() {
		return foodOctave[2].addChip();
	}

	/**
	 * Method leaveMI48.
	 */
	public boolean leaveMI48() {
		return airOctave[2].addChip();
	}

	/**
	 * Method leaveDO48.
	 */
	public boolean leaveDO48() {
		return impOctave[0].addChip();
	}

	/**
	 * Method movePieces will take any body and place pieces at notes.
	 */
	public void movePieces() {
        System.out.println("FD.movePieces:");
		if (hasChrystallizedAstral && !foodOctave[0].hasAstral) {
            System.out.println("astral ("+foodAstral+" "+airAstral+" "+impAstral+")");
			for (int i=0; i<foodAstral; i++)
				foodOctave[i].setHasAstral(true);
			for (int i=0; i<airAstral; i++)
				airOctave[i].setHasAstral(true);
			for (int i=0; i<impAstral; i++)
				impOctave[i].setHasAstral(true);
            forceChange();
			return;
		}
		if (hasChrystallizedMental && !foodOctave[0].hasMental) {
            System.out.println("mental ("+foodMental+" "+airMental+" "+impMental+")");
			for (int i=0; i<foodMental; i++)
				foodOctave[i].setHasMental(true);
			for (int i=0; i<airMental; i++)
				airOctave[i].setHasMental(true);
			for (int i=0; i<impMental; i++)
				impOctave[i].setHasMental(true);
			if (player.isDead()) {
				shiftAfterDeath();
			}
            forceChange();
			return;
		}
	}

	/**
	 * Method moveNextHighestToTI12.
	 */
	public void moveNextHighestToTI12() {
		int tichips = 1;
		if (foodOctave[5].hasChip()) {
			foodOctave[5].clearChips();
		} else if (foodOctave[4].hasChip()) {
			foodOctave[4].clearChips();
		} else if (foodOctave[3].hasChip()) {
			foodOctave[3].clearChips();
		} else if (foodOctave[2].hasChip()) {
			tichips = foodOctave[2].getChips();
			foodOctave[2].clearChips();
		} else if (foodOctave[1].hasChip()) {
			foodOctave[1].clearChips();
		} else if (foodOctave[0].hasChip()) {
			foodOctave[0].clearChips();
		} else {
			tichips = 0;
		}
		foodOctave[6].setChips(tichips);
	}

	/**
	 * Returns the hasChrystallizedAstral.
	 * @return boolean
	 */
	public boolean hasChrystallizedAstral() {
		return hasChrystallizedAstral;
	}

	/**
	 * Returns the hasChrystallizedMental.
	 * @return boolean
	 */
	public boolean hasChrystallizedMental() {
		return hasChrystallizedMental;
	}

	public boolean hasMovedMental() {
		return hasMovedMental;
	}
	public boolean hasMovedAstral() {
		return hasMovedAstral;
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	public void checkComplete() {
		if (!isComplete) {
			// check if food diagram is complete now
			if (player.isDead()) {
			    setComplete(hasChrystallizedMental
									&& allNotesHaveChips());
			} else {
			    setComplete((foodMental == 8)
								&& (airMental == 6)
								&& (impMental == 4));
			}
		} else {
			// check if food diagram has become incomplete again
			// don't care if player is alive because pieces of stuff
			// are never removed before death
			if (player.isDead()) {
				// dead and the FD was complete, just check for chips
				setComplete(hasChrystallizedMental
									&& allNotesHaveChips());
			}
		}
	}
	public void setComplete(boolean complete) {
		boolean prevVal = isComplete;
		isComplete = complete;
		changes.firePropertyChange("FoodDiagComplete", prevVal, isComplete);
	}
	private boolean allNotesHaveChips() {
		boolean allFull = true;
		for (int i=0; i<foodOctave.length; i++) {
			if (!foodOctave[i].hasChip()) allFull = false;
		}
		for (int i=0; i<airOctave.length; i++) {
			if (!airOctave[i].hasChip()) allFull = false;
		}
		for (int i=0; i<impOctave.length; i++) {
			if (!impOctave[i].hasChip()) allFull = false;
		}
		return allFull;
	}
	public Object[] getFoodNotes() {
		Object [] foods;
		ArrayList flist = new ArrayList(8);
		for (int i=0; i<foodOctave.length; i++) {
			if (foodOctave[i].hasChip()) flist.add(FoodNotes[i]);
		}
		if (flist.size() == 0) {
			foods = new Object[] {"(none)"};
		} else {
			foods = flist.toArray();
		}
		return foods;
	}
	public Object[] getAirNotes() {
		Object [] airs;
		ArrayList alist = new ArrayList(6);
		for (int i=0; i<airOctave.length; i++) {
			if (airOctave[i].hasChip()) alist.add(AirNotes[i]);
		}
		if (alist.size() == 0) {
			airs = new Object[] {"(none)"};
		} else {
			airs = alist.toArray();
		}
		return airs;
	}
	public Object[] getImpNotes() {
		Object [] imps;
		ArrayList ilist = new ArrayList(4);
		for (int i=0; i<impOctave.length; i++) {
			if (impOctave[i].hasChip()) ilist.add(ImpNotes[i]);
		}
		if (ilist.size() == 0) {
			imps = new Object[] {"(none)"};
		} else {
			imps = ilist.toArray();
		}
		return imps;
	}
	
	/// Following are three special methods for laws.
    /**
     * 
     */
    public void transformAll12() {
        short f[] = new short[]{0,0,0,0,0,0,0,(short)foodOctave[6].getChips()};
        short i[] = new short[]{0,0,0,(short)impOctave[2].getChips()};
        foodOctave[6].clearChips();
        impOctave[2].clearChips();
        enterChips(f,null,i);
    }
    /**
     * 
     */
    public void shockAllMI48() {
        short mi48 = (short)airOctave[2].getChips();
        if (mi48>0) {
            airOctave[2].clearChips();
            enterChips(null,
                    	new short[]{0,0,0,0,0,mi48},
                    	null);
		} else {
			airOctave[2].addChip();
		}
    }
    /**
     * 
     */
    public void moveMI192toTI12() {
        short mi192 = (short)foodOctave[2].getChips();
        if (mi192>0) {
			foodOctave[2].clearChips();
			enterChips(new short[]{0,0,0,0,0,0,mi192,0},
			        	null,
			        	null);
		} else {
			foodOctave[2].addChip();
		}
    }
    /**
     * @param food
     * @param i
     * @return
     */
    public boolean hasChip(int octave, int i) {
       if (octave == FOOD) {
           return foodOctave[i].hasChip();
       } else if (octave == AIR) {
           return airOctave[i].hasChip();
       } else if (octave == IMP) {
           return impOctave[i].hasChip();
       }
       
        return false;
    }
    /**
     * @return Returns the player.
     */
    protected Player getPlayer() {
        return player;
    }
    /**
     * 
     */
    public void forceChange() {
        changes.firePropertyChange("FDChange", null, this);
    }
    
    /**
     * This method will update the chips for each note, called every time
     * "FDChange" property change event is fired.
     */
    public void updateState() {
        if (!player.isDead()) {
            if (hasChrystallizedAstral) {
                for (int i=0; i<foodAstral; i++) {
                    foodOctave[i].setHasAstral(true);
                }
                for (int i=0; i<airAstral; i++) {
                    airOctave[i].setHasAstral(true);
                }
                for (int i=0; i<impAstral; i++) {
                    impOctave[i].setHasAstral(true);
                }
            }
            if (hasChrystallizedMental && hasMovedMental) {
                for (int i=0; i<foodMental; i++) {
                    foodOctave[i].setHasMental(true);
                }
                for (int i=0; i<airMental; i++) {
                    airOctave[i].setHasMental(true);
                }
                for (int i=0; i<impMental; i++) {
                    impOctave[i].setHasMental(true);
                }
            }
        }
    }
}
