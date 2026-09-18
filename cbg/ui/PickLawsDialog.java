package cbg.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.beans.*;
import javax.swing.*;

import cbg.boardParts.*;
import cbg.common.UIConsts;
import cbg.player.Player;

/**
 * @author Stephen Chudleigh
 */
public class PickLawsDialog extends JDialog 
						implements UIConsts, ActionListener {

	private LawCard randomLaw, chosenLaw = null;
	private boolean finished = false;

	public static final String FINISHED = "finished";

	private JPanel lhPanel, playsPanel;
	private JLabel instruction,
					randomLabel,
					choiceLabel;
	
	private JButton randomButton;
	
	/**
	 * This constructor for CardHandPanel is only used
	 * for testing the panel in a standalone mode.
	 *
	public PickLawsDialog() {
		super();
		plyr = new Player("P1");
		plyr.setLawPlaysThis(1);
		for (int i=0; i<5; i++) {
			plyr.drawLawCard();
		}
	}*/
	
	/**
	 * Constructor for CardHandPanel.
	 */
	public PickLawsDialog(JFrame frame) {
		super(frame, false);
	}
	
	private PropertyChangeSupport changes =
            new PropertyChangeSupport(this);
            
    public void addPropertyChangeListener(PropertyChangeListener l) {
	    changes.addPropertyChangeListener(l);
	}
	public void removePropertyChangeListener(PropertyChangeListener l) {
	    changes.removePropertyChangeListener(l);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (BYRANDOM_COMMAND.equals(cmd)) {
		    Player player = ConsciousBoardgameGUI.getInstance().getCurrentPlayer();
			int roll1 = Dice.roll();
			if (roll1 == 0) {
				JOptionPane.showMessageDialog(this,
					"You rolled a 0, no law chosen by random.");
				randomLabel.setIcon(null);
				randomLabel.setText("None by random.");
			} else if (player.getLawHand().size()==0) {
				JOptionPane.showMessageDialog(this,
					"You have no law cards in your hand,\n"+
					"no law chosen by random.");
				randomLabel.setIcon(null);
				randomLabel.setText("None by random.");
			} else {
				Random r = new Random(System.currentTimeMillis());
				int pick = r.nextInt(player.getLawHand().size());
			
				JOptionPane.showMessageDialog(this,
						"You drew card number "+(pick+1));
				randomLaw = (LawCard)player.getLawHand().get(pick);
				player.getLawHand().remove(pick);
				player.addLawToPlay(randomLaw);
				randomLabel.setIcon(IconFactory.cardIcons[randomLaw.getCard().getRank()][randomLaw.getCard().getSuit()]);
				randomLabel.setVerticalTextPosition(SwingConstants.TOP);
				randomLabel.setText(randomLaw.getLawDesc());
				//System.out.println("currentPlayer law plays="+currentPlayer.getLawPlaysThis());
				lhPanel.remove(pick);
			}
			if (player.getLawPlaysThis() == 1) {
				setFinished(true);
			} else if (player.getLawHand().size() == 0) {
				JOptionPane.showMessageDialog(this,
						"There are no more law cards\n"+
						"in your hand, no law by choice.");
				setFinished(true);
			} else {
				randomButton.setVisible(false);
				enableLawButtons();
				instruction.setText("Now select a law card by choice. (Click on the desired law)");
			}
		} else if (CHOICE_BTN_COMMAND.equals(cmd)) {
			if(!chooseCard(e))
				return;
			
			setFinished(true);
		}
		this.validate();
		lhPanel.updateUI();
	}

	/**
	 * Method chooseCard.
	 * @param e
	 */
	private boolean chooseCard(ActionEvent e) {
	    Player player = ConsciousBoardgameGUI.getInstance().getCurrentPlayer();
		int value = Integer.parseInt(((JButton)e.getSource()).getName());
		for (int i=0; i<player.getLawHand().size(); i++) {
			LawCard lc = (LawCard)player.getLawHand().get(i);
			if ( value == lc.getLawValue() ) {
				chosenLaw = lc;
				break;
			}
		}
		if (chosenLaw.getCard().getRank() == Card.ACE && player.hasNonAceLaw()) {
		    CBGDlgFactory.displayInformationMessage("Can't Choose Death",
		            "Sorry, you can't choose death if there\n"+
					"is another option available.");
			return false;
		}
		int n = JOptionPane.showConfirmDialog(
    					this, "You chose law card "+chosenLaw.getCard()+
    					"\nAccept this choice?",
                		"Confirm Choice",
						JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			player.addLawToPlay(chosenLaw);
			player.getLawHand().remove(chosenLaw);
			return true;
		}
		return false;
	}
	
	/**
	 * Method enableLawButtons.
	 */
	private void enableLawButtons() {
		for (int i=0; i<lhPanel.getComponentCount(); i++) {
			lhPanel.getComponent(i).setEnabled(true);
		}
	}

	public void displayHandView() {
		lhPanel.removeAll();
		Iterator it = ConsciousBoardgameGUI.getInstance().getCurrentPlayer().getLawHand().iterator();
		while (it.hasNext()) {
			LawCard lc = (LawCard)it.next();
			JButton lcBtn = new JButton(IconFactory.cardIcons[lc.getCard().getRank()][lc.getCard().getSuit()]);
			lcBtn.setName(String.valueOf(lc.getLawValue()));
			lcBtn.setHorizontalTextPosition(SwingConstants.CENTER);
			lcBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			lcBtn.setText(lc.getLawDesc());
			lcBtn.setActionCommand(CHOICE_BTN_COMMAND);
			lcBtn.addActionListener(this);
			lcBtn.setEnabled(false);
			lhPanel.add(lcBtn);
		}
		this.validate();
	}
	
	/**
	 * Method updateHandView.
	 * @param collection
	 */
	public void updateHandView(Collection cards) {
		lhPanel.removeAll();
		Iterator it = cards.iterator();
		while (it.hasNext()) {
			LawCard lc = (LawCard)it.next();
			JButton lcBtn = new JButton(IconFactory.cardIcons[lc.getCard().getRank()][lc.getCard().getSuit()]);
			lcBtn.setText(lc.getLawDesc());
			lcBtn.setActionCommand(CHOICE_BTN_COMMAND);
			lcBtn.addActionListener(this);
			lhPanel.add(lcBtn);
		}
		this.validate();
	}

	/*** For Testing Purposes Only
	public static void main(String[] args) {
    	Decks.init();
		Dice.init();
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
     *//*
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Pick Law Card Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		PickLawsDialog pickPanel = new PickLawsDialog();
	
        //Create and set up the content pane.
        Container contentPane = frame.getContentPane();
		contentPane.add(pickPanel.createLawHand(), BorderLayout.PAGE_END);
		contentPane.add(pickPanel.createButtonPane(false), BorderLayout.LINE_START);
		contentPane.add(pickPanel.createPlaysPane(),BorderLayout.CENTER);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        pickPanel.playsPanel.setMinimumSize(pickPanel.playsPanel.getSize());
    }*/
    
	public void createAndShowDialog() {
		//Create and set up the content pane.
		Container contentPane = this.getContentPane();
		contentPane.add(this.createLawHand(), BorderLayout.PAGE_END);
		contentPane.add(this.createButtonPane(false), BorderLayout.LINE_START);
		contentPane.add(this.createPlaysPane(),BorderLayout.CENTER);
		//Display the window.
		//this.pack();
		//this.setVisible(true);
		//this.setModal(true);
	}

    public void createChoiceDialog() {
    	//Create and set up the content pane.
		Container contentPane = this.getContentPane();
		contentPane.add(this.createLawHand(), BorderLayout.PAGE_END);
		contentPane.add(this.createButtonPane(true), BorderLayout.CENTER);
		enableLawButtons();
		//Display the window.
		//this.pack();
		//this.setModal(true);
	}

	/**
	 * Method createPlaysPane.
	 * @return Component
	 */
	private Component createPlaysPane() {
		playsPanel = new JPanel(new GridLayout(0,2));
		Box rPanel = new Box(BoxLayout.Y_AXIS);
		Box cPanel = new Box(BoxLayout.Y_AXIS);
		JLabel randTxt = new JLabel("By Random:");
		randTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
		rPanel.add(randTxt);
		randomLabel = new JLabel(IconFactory.cardIcons[0][0]);
		randomLabel.setAlignmentX(0f);
		rPanel.add(randomLabel);
		if (ConsciousBoardgameGUI.getInstance().getCurrentPlayer().getLawPlaysThis() == 1) {
			cPanel.add(new JLabel(""));
			choiceLabel = new JLabel("");
			cPanel.add(choiceLabel);
		} else {
			JLabel chosenTxt = new JLabel("By Choice:");
			chosenTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
			cPanel.add(new JLabel("By Choice:"));
			choiceLabel = new JLabel(IconFactory.cardIcons[0][0]);
			cPanel.add(choiceLabel);
		}
		playsPanel.add(rPanel);
		playsPanel.add(cPanel);
		playsPanel.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
		return playsPanel;
	}


	/**
	 * Method createLawHand.
	 * @return Component
	 */
	private Component createLawHand() {
		lhPanel = new JPanel(new GridLayout(1,ConsciousBoardgameGUI.getInstance().getCurrentPlayer().getLawHand().size()));
		displayHandView();
		JScrollPane scrollPane = new JScrollPane(
				lhPanel,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setMinimumSize(new Dimension(lhPanel.getWidth(), lhPanel.getHeight()+20));
		return scrollPane;
	}


    protected JComponent createButtonPane(boolean choiceOnly) {
		if (choiceOnly) {
			instruction = new JLabel("Click on the law card of your choice.");
		} else {
			randomButton = new JButton("One by Random");
			instruction = new JLabel("Pick one by random (Click the button)");
			randomButton.setActionCommand(BYRANDOM_COMMAND);
			randomButton.addActionListener(this);
		}
        //Center the button in a panel with some space around it.
		JPanel buttonPanel = new JPanel();//use default FlowLayout
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
        buttonPanel.setAlignmentX(0.5f);
        buttonPanel.setAlignmentY(0.5f);
        buttonPanel.add(instruction);
        if (!choiceOnly)
			buttonPanel.add(randomButton);

        return buttonPanel;
	}

	/**
	 * Returns the finished.
	 * @return boolean
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Sets the finished.
	 * @param finished The finished to set
	 */
	public void setFinished(boolean newValue) {
		boolean oldValue = finished;
		this.finished = newValue;
		changes.firePropertyChange(FINISHED, oldValue, newValue);
	}

}
