package cbg.ui;

import java.beans.PropertyChangeEvent;
import java.util.*;

import javax.swing.*;

import cbg.boardParts.Card;
import cbg.boardParts.Decks;
import cbg.common.UIConsts;
import cbg.player.Player;

/**
 * @author Stephen Chudleigh
 */
public class CardHandPanel extends JPanel
							implements UIConsts,
							java.beans.PropertyChangeListener
{

	/**
	 * Constructor for CardHandPanel.
	 */
	public CardHandPanel() {
		super();
	}

	/**
	 * Method playSelectedCards.
	 */
	public short [] playSelectedCards(Player plyr) {
		ArrayList selected = getSelectedCards(plyr);
		int nSelected = selected.size();
		short [] shocks = new short[] {0,0,0,0};
    	if (nSelected > 3) {
    		CBGDlgFactory.displayMessage("You can play up to 3 cards at one time.\n"
    											+"You tried to play "+selected+".");
			deselectAllButtons();
    		return shocks;
    	}
    	if (nSelected > plyr.getCardPlays()) {
    	    CBGDlgFactory.displayMessage("You have "+plyr.getCardPlays()+" card plays left this turn.\n"
    											+"You tried to play cards "+selected);
			deselectAllButtons();
    		return shocks;
    	}
    	if (nSelected == 1) { // face card play
    		Card c = (Card)selected.get(0);
    		if (!c.isFaceCard()) {
    		    CBGDlgFactory.displayMessage("You can only play a face card by itself.");
    			return shocks;
    		} else {
    			shocks = plyr.getEp().createPiece(c);
    			//System.out.println("creating piece caused "+shocks[0]+" self rememberings, "
    			//				+shocks[1]+" transform emotions, "
    			//				+shocks[2]+" wild shocks, "+
    			//				+shocks[3]+" all shocks.");
    			plyr.getPocHand().remove(c);
    			plyr.takeCardPlays(nSelected);
    		}
    	} else if (nSelected == 2) { // two card play
    		Card c1 = (Card)selected.get(0);
    		Card c2 = (Card)selected.get(1);
    		//System.out.println("Trying to play "+c1+" with "+c2);
    		Card newCard = Card.playsWith(c1,c2);
    		if (newCard == null) {
    		    CBGDlgFactory.displayMessage("These cards do not play together.");
    			deselectAllButtons();
    			return shocks;
    		} else {
    			shocks = plyr.getEp().createPiece(newCard);
    			//System.out.println("creating piece caused "+shocks[0]+" self rememberings, "
    			//				+shocks[1]+" transform emotions, "
    			//				+shocks[2]+" wild shocks, "+
    			//				+shocks[3]+" all shocks.");
    			plyr.getPocHand().remove(c1);
    			plyr.getPocHand().remove(c2);
    			plyr.takeCardPlays(nSelected);
    		}
    	} else if (nSelected == 3) { // trips
    	    Card c1 = (Card)selected.get(0);
    	    Card c2 = (Card)selected.get(1);
    	    Card c3 = (Card)selected.get(2);
    	    Card giveTwo = Card.playTrips(c1, c2, c3);
    	    if (giveTwo == null) {
    	        CBGDlgFactory.displayMessage("These cards do not play together.");
    			deselectAllButtons();
    			return shocks;
    	    } else {
    	        shocks = plyr.getEp().createTwoPieces(giveTwo);
    	        //System.out.println("creating piece caused "+shocks[0]+" self rememberings, "
    			//				+shocks[1]+" transform emotions, "
    			//				+shocks[2]+" wild shocks, "+
    			//				+shocks[3]+" all shocks.");
    	        plyr.getPocHand().remove(c1);
    	        plyr.getPocHand().remove(c2);
    	        plyr.getPocHand().remove(c3);
    			plyr.takeCardPlays(nSelected);
    	    }
    	}
    	deselectAllButtons();
    	return shocks;
	}

	public void keepSelectedCards(Player plyr) {
		ArrayList selCards = getSelectedCards(plyr);
		plyr.getPocHand().clear();
		plyr.setPocHand(selCards);
	}

	/**
	 * Method deselectAllButtons.
	 */
	public void deselectAllButtons() {
		for (int i=0; i<this.getComponentCount(); i++) {
			JToggleButton btn = (JToggleButton)this.getComponent(i);
			btn.setSelected(false);
		}
	}

	public ArrayList getSelectedCards(Player plyr) {
		ArrayList selCards = new ArrayList();
		for (int i=0; i<this.getComponentCount(); i++) {
			JToggleButton btn = (JToggleButton)this.getComponent(i);
			if (btn.isSelected()) {
				//System.out.println("The "+i+"th button is selected.");
				selCards.add(plyr.getPocHand().get(i));
			}
		}
		return selCards;
	}

	/**
	 * Method updateHandView.
	 * @param collection
	 */
	public void updateHandView(Collection cards) {
		this.removeAll();
		Iterator it = cards.iterator();
		while (it.hasNext()) {
			Card c = (Card)it.next();
			this.add(new JToggleButton(IconFactory.cardIcons[c.getRank()][c.getSuit()]));
		}
		this.updateUI();
	}

	public static void main(String[] args) {
		Decks.init();
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
        JFrame frame = new JFrame("Card Hand Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        CardHandPanel chp = new CardHandPanel();
        Collection c = new ArrayList();
        for (int i=0; i<50; i++) {
            c.add(Decks.drawPOCCard());
        }
        chp.updateHandView(c);
        frame.getContentPane().add(chp);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("POCHandChange")) {
            this.updateHandView((Collection)event.getNewValue());
        }
    }

}
