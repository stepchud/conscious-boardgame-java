package cbg.ui;

import java.awt.*;
import java.beans.PropertyChangeEvent;

import javax.swing.*;

import cbg.common.UIConsts;
import cbg.player.*;

/**
 * @author Stephen Chudleigh
 */
public class FoodDiagramPanel extends JLayeredPane
								implements UIConsts,
										java.beans.PropertyChangeListener {

    public static final int Width = 286, Height = 264;
    
    private JLabel [] noteLabels;
	private JLabel [] accumLabels;
	private JLabel accrueMI192,
					accrueMI48,
					accrueDO48,
					accrueTI12,
					accrueMI12;

	private JLabel diagramLabel;

	/**
	 * Constructor for FoodDiagramPanel.
	 * @throws HeadlessException
	 */
	public FoodDiagramPanel() throws HeadlessException {
		super();

		noteLabels = new JLabel[18];
		for (int i=0; i<noteLabels.length; i++)
			noteLabels[i] = new JLabel(IconFactory.noChipIcon);
		
		accumLabels = new JLabel[15];
		for (int i=0; i<accumLabels.length; i++)
			accumLabels[i] = new JLabel(IconFactory.noChipIcon);

		// init accrue labels
		accrueMI192 = new JLabel(IconFactory.noChipIcon);
		accrueMI48 = new JLabel(IconFactory.noChipIcon);
		accrueDO48 = new JLabel(IconFactory.noChipIcon);
		accrueTI12 = new JLabel(IconFactory.noChipIcon);
		accrueMI12 = new JLabel(IconFactory.noChipIcon);
		
		Dimension size = new Dimension(FoodDiagramPanel.Width,FoodDiagramPanel.Height);

		diagramLabel = new JLabel(IconFactory.diagramIcon);
		diagramLabel.setBounds(0,0,size.width,size.height);
		diagramLabel.setBackground(Color.black);
		this.add(diagramLabel, JLayeredPane.DEFAULT_LAYER);
				
		for (int i=0; i<noteLabels.length; i++) {
			Point p = getNoteScreenPos(i);
			if (p==null)
				System.err.println("point is null.");
			noteLabels[i].setBounds(p.x,p.y,PIECE_SIZE,PIECE_SIZE);
			this.add(noteLabels[i], JLayeredPane.PALETTE_LAYER);
		}
		accrueMI192.setBounds(PIECE_SIZE,PIECE_SIZE*9,PIECE_SIZE,PIECE_SIZE);
		accrueMI48.setBounds(PIECE_SIZE*2,PIECE_SIZE*7,PIECE_SIZE,PIECE_SIZE);
		accrueDO48.setBounds(PIECE_SIZE*4,PIECE_SIZE*9,PIECE_SIZE,PIECE_SIZE);
		accrueTI12.setBounds(PIECE_SIZE*2,PIECE_SIZE*4,PIECE_SIZE,PIECE_SIZE);
		accrueMI12.setBounds(PIECE_SIZE*4,PIECE_SIZE*6,PIECE_SIZE,PIECE_SIZE);
		this.add(accrueMI192, JLayeredPane.PALETTE_LAYER);
		this.add(accrueMI48, JLayeredPane.PALETTE_LAYER);
		this.add(accrueDO48, JLayeredPane.PALETTE_LAYER);
		this.add(accrueTI12, JLayeredPane.PALETTE_LAYER);
		this.add(accrueMI12, JLayeredPane.PALETTE_LAYER);
		
		Point p = getNoteScreenPos(7); // last food note DO6
		for (int i=0; i<5; i++) {
			accumLabels[i].setBounds(p.x + PIECE_SIZE*(i+1), p.y, PIECE_SIZE, PIECE_SIZE);
		}
		p = getNoteScreenPos(13); // last air note LA6
		for (int i=0; i<6; i++) {
			accumLabels[5+i].setBounds(p.x + PIECE_SIZE*(i+1), p.y, PIECE_SIZE, PIECE_SIZE);
		}
		p = getNoteScreenPos(17); // last impression note FA6
		for (int i=0; i<4; i++) {
			accumLabels[11+i].setBounds(p.x, p.y - PIECE_SIZE*(i+1), PIECE_SIZE, PIECE_SIZE);
		}
		for (int i=0; i<accumLabels.length; i++)
			this.add(accumLabels[i], JLayeredPane.PALETTE_LAYER);
			
		this.setPreferredSize(size);
		this.setMinimumSize(size);
	}
    
	/** Used for Demo
    //Handle user interaction.
    public void actionPerformed(ActionEvent e) {
    	System.err.println("Performing "+e.getActionCommand());
        String cmd = e.getActionCommand();
        Player plyr = ConsciousBoardgameGUI.getInstance().getCurrentPlayer();
        if (EAT_COMMAND.equals(cmd)) {
        	plyr.getFd().eat();
        } else if (BREATHE_COMMAND.equals(cmd)) {
        	plyr.getFd().breathe();
        } else if (IMP_COMMAND.equals(cmd)) {
        	plyr.getFd().impression();
        } else if (SHOCK_FOOD_COMMAND.equals(cmd)) {
        	//plyr.getFd().shocksFood();
        } else if (SELF_REMEMBER_COMMAND.equals(cmd)) {
        	//plyr.getFd().selfRemember();
		} else if (TRANSFORM_COMMAND.equals(cmd)) {
			//plyr.getFd().transformEmotions();
		} else if (END_TURN_COMMAND.equals(cmd)) {
        	if (plyr.isDead() && plyr.getFd().hasChrystallizedMental() && !plyr.getFd().hasMovedMental()) {
        		plyr.getFd().shiftAfterDeath();
        	}
        } else if (DEATH_COMMAND.equals(cmd)) {
        	plyr.dies();
        	plyr.getFd().shiftAfterDeath();
        }
        updateState(plyr.getFd());
    }*/
    
	private void updateState(FoodDiagram fd) {
        fd.updateState();
		byte [] state = fd.getCurrentState();
		for (int i=0; i<state.length; i++) {
			noteLabels[i].setIcon(getIconForState(state[i]));
		}
		
		// update accrual states
		state = new byte[5];
		state = fd.getAccrualStates();
		accrueMI192.setIcon(getIconForAccrueState(state[0],false));
		accrueMI48.setIcon(getIconForAccrueState(state[1],true));
		accrueDO48.setIcon(getIconForAccrueState(state[2],false));
		accrueTI12.setIcon(getIconForAccrueState(state[3],true));
		accrueMI12.setIcon(getIconForAccrueState(state[4],false));
		
		state = new byte[15];
		state = fd.getAccumStates();
		for (int i=0; i< state.length; i++) {
			accumLabels[i].setIcon(getIconForState(state[i]));		
		}
	}

	/**
	 * Method getIconForAccumState.
	 * @param state
	 * @param vert
	 * @return Icon
	 */
	private Icon getIconForAccrueState(byte state, boolean vert) {
		//System.out.println("Accumulation icon for\n"+
		//					"state="+state+"; vertical?"+vert);
		if (vert) {
			switch (state) {
				case 0:
					return IconFactory.noChipIcon;
				case 1:
					return IconFactory.oneVertAccrueIcon;
				case 2:
					return IconFactory.twoVertAccrueIcon;
			}
		} else {
			switch (state) {
				case 0:
					return IconFactory.noChipIcon;
				case 1:
					return IconFactory.oneAccrueIcon;
				case 2:
					return IconFactory.twoAccrueIcon;
			}
		}
		
		System.err.println("Could not find accumulation icon for\n"+
							"state="+state+"; vertical?"+vert);
							
		return null;
	}


	/**
	 * Method getIconForState.
	 * @param b
	 * @return Icon
	 */
	private Icon getIconForState(byte b) {
		switch (b) {
			case EMPTY:
				return IconFactory.noChipIcon;
			case CHIP:
				return IconFactory.chipIcon;
			case ASTRAL:
				return IconFactory.astralIcon;
			case ASTRAL_CHIP:
				return IconFactory.caIcon;
			case MENTAL:
				return IconFactory.mentalIcon;
			case MENTAL_CHIP:
				return IconFactory.cmIcon;
			case MENTAL_ASTRAL:
				return IconFactory.amIcon;
			case MENTAL_ASTRAL_CHIP:
				return IconFactory.camIcon;
		}
		
		System.err.println("Could not find icon for state="+b+
							" returning null");
		return null;
	}

	/**
	 * Method getNoteScreenPos.
	 * @param i
	 * @return Point
	 */
	private Point getNoteScreenPos(int i) {
		switch (i) {
			//food
			case 0: return new Point(0,PIECE_SIZE*11);
			case 1: return new Point(0,PIECE_SIZE*10);
			case 2: return new Point(0,PIECE_SIZE*9);
			case 3: return new Point(0,PIECE_SIZE*7);
			case 4: return new Point(0,PIECE_SIZE*6);
			case 5: return new Point(PIECE_SIZE,PIECE_SIZE*5);
			case 6: return new Point(PIECE_SIZE*2,PIECE_SIZE*5);
			case 7: return new Point(PIECE_SIZE*4,PIECE_SIZE*5);
			//air
			case 8: return new Point(0,PIECE_SIZE*8);
			case 9: return new Point(PIECE_SIZE,PIECE_SIZE*8);
			case 10: return new Point(PIECE_SIZE*2,PIECE_SIZE*8);
			case 11: return new Point(PIECE_SIZE*4,PIECE_SIZE*8);
			case 12: return new Point(PIECE_SIZE*5,PIECE_SIZE*8);
			case 13: return new Point(PIECE_SIZE*6,PIECE_SIZE*8);
			//impressions
			case 14: return new Point(PIECE_SIZE*3,PIECE_SIZE*9);
			case 15: return new Point(PIECE_SIZE*3,PIECE_SIZE*7);
			case 16: return new Point(PIECE_SIZE*3,PIECE_SIZE*6);
			case 17: return new Point(PIECE_SIZE*3,PIECE_SIZE*4);
		}
		System.err.println("Could not find screen position for note "+i);
		return null;
	}

	/* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent arg0) {
        if (arg0.getPropertyName().equals("FDChange")) {
            System.out.println("FDChange property event");
            this.updateState((FoodDiagram)arg0.getNewValue());
        }
    }
}
