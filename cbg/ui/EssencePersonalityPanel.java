package cbg.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import cbg.boardParts.Card;
import cbg.common.*;
import cbg.player.*;

/**
 * @author Stephen Chudleigh
 */
public class EssencePersonalityPanel extends JPanel
										implements UIConsts {
	
	private CardPane [] epPanes;
	//private JLabel lobLbl;

	/**
	* @author Stephen Chudleigh
	*
	* CardPane has a picture of the card it represents and the chips that card holds.
	*/
	private class CardPane extends Box {
		
		private JToggleButton cardButton;
		private JLabel pieceLabel;
		
		public CardPane(ImageIcon icon) {
			super(BoxLayout.Y_AXIS);
			cardButton = new JToggleButton(icon);
			//cardButton.setBorder(BorderFactory.createMatteBorder(0,3,0,3,Color.BLACK));
			cardButton.setBorderPainted(false);
			cardButton.setContentAreaFilled(true);
			cardButton.setAlignmentX(0.5F);
			cardButton.setAlignmentY(0.5F);			
			pieceLabel = new JLabel(IconFactory.zeroPieceIcon);
			pieceLabel.setAlignmentX(0.5F);
			pieceLabel.setAlignmentY(0.5F);
			this.add(cardButton);
			this.add(pieceLabel);
			this.setMaximumSize(new Dimension(30, 75));
            this.setEnabled(false);
		}
		/**
		 * Method setChips.
		 * @param i
		 */
		private void setChipCount(int i) {
			if (i > 0)
				this.setEnabled(true);
			if (i<6)
				pieceLabel.setIcon(IconFactory.chipIcons[i]);
			else
				pieceLabel.setIcon(IconFactory.chipIcons[5]);
		}
		
		private boolean isSelected() {
			return cardButton.isSelected();
		}
		
		private void setSelected(boolean sel) {
			cardButton.setSelected(sel);
		}
		
	}
	
	/**
	 * Constructor for EssencePersonalityPanel.
	 */
	public EssencePersonalityPanel() {
		super();

		//Box mainPanel = new Box(BoxLayout.Y_AXIS);
		
		JPanel facePanel = new JPanel(new GridLayout(3,12,5,2));
		epPanes = new CardPane[IconFactory.faceCardIcons.length];
		//System.out.println("epPanes.lenght="+epPanes.length);
		for (int i=0; i<epPanes.length; i++) {
			epPanes[i] = new CardPane(IconFactory.faceCardIcons[i]);
			facePanel.add(epPanes[i]);
		}
		//mainPanel.add(facePanel);
        //this.add(mainPanel);
        this.add(facePanel);
	}

	/**
	 * Method action_CreateJO.
	 */
	protected void action_CreateJO(Player p) {
		try {
		    p.getEp().createJoker();
			p.getFd().performShocks(new short[]{0,0,0,1});
		} catch (InvalidCardPlayException e) {
		    JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Invalid Joker Play",
					JOptionPane.ERROR_MESSAGE);
		    return;
		}
		ConsciousBoardgameGUI.getInstance().getMkJOButton().setEnabled(false);
		this.clearSelectedButtons();
	}
	
	
	/**
	 * Method action_CreateXJ.
	 */
	protected void action_CreateXJ(Player p) {
		// get selected board ace cards
		ArrayList selCards = getSelectedAces();
		if (!(selCards.size()==2)) {
			JOptionPane.showMessageDialog(this,
						"You must combine 2 aces to make an extra joker.",
						"Invalid Ace Play",
						JOptionPane.ERROR_MESSAGE);
			return;
		}
		short [] shocks = {0,0,0,0};
		try {
			shocks = p.getEp().createXJ((Card) selCards.get(0), (Card) selCards.get(1));
    		p.getFd().performShocks(shocks);
		} catch (InvalidCardPlayException e) {
			JOptionPane.showMessageDialog(this,
						e.getMessage(),
						"Invalid XJ Play",
						JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ConsciousBoardgameGUI.getInstance().getMkXJButton().setEnabled(false);
		this.clearSelectedButtons();
	}

	/**
	 * Method action_CreateAce.
	 */
	protected void action_CreateAce(Player p) {
		// get selected board face cards
		ArrayList selCards = getSelectedFaceCards();
		if (!(selCards.size()==2)) {
			JOptionPane.showMessageDialog(this,
						"You must combine 2 pieces to make an ace.",
						"Invalid Transform Play",
						JOptionPane.ERROR_MESSAGE);
			return;
		}
		short [] shocks = {0,0,0,0};
		try {
			shocks = p.getEp().createAce((Card) selCards.get(0), (Card) selCards.get(1));
	    	p.getFd().performShocks(shocks);
		} catch (InvalidCardPlayException e) {
			JOptionPane.showMessageDialog(this,
						e.getMessage(),
						"Invalid Transform Play",
						JOptionPane.ERROR_MESSAGE);
			return;
		}
		
    	ConsciousBoardgameGUI.getInstance().getMkAceButton().setEnabled(false);
    	this.clearSelectedButtons();
	}

	/**
	 * Method getSelectedFaceCards.
	 * @return ArrayList
	 */
	private ArrayList getSelectedFaceCards() {
		ArrayList selected = new ArrayList();
		for (int i=6; i<epPanes.length; i++) {
			if (epPanes[i].isSelected())
				selected.add(getCardForPane(i));
		}
		return selected;
	}
	/**
	 * Method getSelectedAces.
	 * @return ArrayList
	 */
	private ArrayList getSelectedAces() {
		ArrayList selected = new ArrayList();
		for (int i=0; i<3; i++) {
			if (epPanes[i].isSelected())
				selected.add(getCardForPane(i));
		}
		return selected;
	}
	/**
	 * Method clearSelectedButtons.
	 */
	private void clearSelectedButtons() {
		for (int i=0; i<epPanes.length; i++) {
			epPanes[i].setSelected(false);
		}
	}
	
	/**
	 * Method updateState.
	 * @param ep
	 */
	public void updateView(EssenceAndPersonality ep) {
		Iterator iter = ep.getPartsOfBeing().iterator();
		while (iter.hasNext()) {
			Card c = (Card)iter.next();
			epPanes[EssencePersonalityPanel.getPaneForCard(c)]
					.setChipCount(ep.getChipsFor(c));
		}
		this.updateUI();
	}

	/**
	 * Method findPaneForCard.
	 * @param c
	 * @return int
	 */
	public static int getPaneForCard(Card c) {
		if (c.getRank()==Card.ACE || c.getSuit()==Card.JOKERS) {
			switch (c.getSuit()) {
				case Card.JOKERS:
					if (c.getRank()==Card.X_J)
						return 4;
					else if (c.getRank()==Card.JOKR)
						return 5;
				case Card.DIAMONDS:
					return 0;
				case Card.CLUBS:
					return 1;
				case Card.HEARTS:
					return 2;
				case Card.SPADES:
					return 3;
			}
		} else {
			int place=0;
			switch (c.getSuit()) {
				case Card.DIAMONDS:
					place += 12;
					break;
				case Card.CLUBS:
					place += 15;
					break;
				case Card.HEARTS:
					place += 6;
					break;
				case Card.SPADES:
					place += 9;
					break;
			}
			switch (c.getRank()) {
				case Card.QUEEN:
					place += 1;
					break;
				case Card.KING:
					place += 2;
					break;
			}
			return place;
		}
		System.err.println("Not A Face Card! (did not return)");
		return -1;
	}
	
	/**
	 * Method findPaneForCard.
	 * @param c
	 * @return int
	 */
	public static Card getCardForPane(int pane) {
		switch (pane) {
			case 0:
				return Card.AD;
			case 1:
				return Card.AC;
			case 2:
				return Card.AH;
			case 3:
				return Card.AS;
			case 4:
				return Card.XJ;
			case 5:
				return Card.JO;
			case 6:
				return Card.JH;
			case 7:
				return Card.QH;
			case 8:
				return Card.KH;
			case 9:
				return Card.JS;
			case 10:
				return Card.QS;
			case 11:
				return Card.KS;
			case 12:
				return Card.JD;
			case 13:
				return Card.QD;
			case 14:
				return Card.KD;
			case 15:
				return Card.JC;
			case 16:
				return Card.QC;
			case 17:
				return Card.KC;
			default:
				System.err.println(pane+" is not a valid pane! (return null)");
				return null;
		}
	}

	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    	
	 /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("EP Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        frame.getContentPane().add(new EssencePersonalityPanel());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}