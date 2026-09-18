package cbg.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import cbg.boardParts.*;
import cbg.common.UIConsts;
import cbg.player.Player;

/**
 * @author Stephen Chudleigh
 * Created on Dec 27, 2005
 */
public class LawsInPlayPanel extends JPanel implements UIConsts {

	private JButton obeyBtn;
	private JPanel lawCards;

	/**
	 * Constructor for LawsInPlayPanel.
	 */
	public LawsInPlayPanel() {
		super();
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(createBtnRow());
		box.add(createLCPane());
		add(box);
	}

	/**
	 * Method createBtnRow.
	 * @return Component
	 */
	private Component createBtnRow() {
		obeyBtn = new JButton("Obey Law");
		obeyBtn.setActionCommand(OBEY_LAW_COMMAND);
		obeyBtn.addActionListener(ConsciousBoardgameGUI.getInstance());
		JLabel hint = new JLabel("Place mouse over the law card to read law");
		JPanel firstRow = new JPanel();
		firstRow.add(hint);
		firstRow.add(obeyBtn);
		return firstRow;
	}

	
	/**
	 * Method createLCPane.
	 * @return Component
	 */
	private Component createLCPane() {
		lawCards = new JPanel();
		return lawCards;
	}

	/**
	 * Method updateView.
	 */
	public void updateView(Player plyr) {
		lawCards.removeAll();
		Iterator it = plyr.getLawsInPlay().iterator();
		while (it.hasNext()) {
			LawCard c = (LawCard)it.next();
			//System.out.println("Card "+(i++)+" is "+c.toString()+" rank="+c.getRank()+" suit="+c.getSuit());
			JToggleButton button = new JToggleButton(
							IconFactory.cardIcons[c.getCard().getRank()][c.getCard().getSuit()]);
			button.setToolTipText(c.getLawDesc());
			lawCards.add(button);
		}
		validate();
	}

	/**
	 * Method playSelectedCards.
	 */
	public short [] playSelectedCards(Player plyr, CardHandPanel chPanel) {
		ArrayList selectedCards = chPanel.getSelectedCards(plyr);
		ArrayList selectedLawCards = this.getSelectedLawCards(plyr);
		int nC = selectedCards.size();
		int nLC = selectedLawCards.size();
		
		short [] shocks = new short[] {0,0,0,0};
		if ((nC+nLC) > 3) {
			JOptionPane.showMessageDialog(this, "You can play up to 3 cards at a time.\n"
												+"You tried to play "+nC
												+"cards and "+nLC+" laws.");
			deselectAllButtons();
			chPanel.deselectAllButtons();
			return shocks;
		}
		if (nC > plyr.getCardPlays()) {
			JOptionPane.showMessageDialog(this, "You have "+plyr.getCardPlays()+" card plays left this turn.\n"
												+"You tried to play "+nC);
			deselectAllButtons();
			chPanel.deselectAllButtons();
			return shocks;
		}
		if ((nC+nLC) == 1) { // face law card play
			LawCard lc = ((LawCard)selectedLawCards.remove(0));
			if (!lc.getCard().isFaceCard()) {
				JOptionPane.showMessageDialog(this, "You can only play a face card by itself.");
				deselectAllButtons();
				return shocks;
			} else if (lc.isPlayed()) {
				if (lc.isPlayed()) {
					JOptionPane.showMessageDialog(this, "Law Card "+lc.getCard()+" has already been played");
					return shocks;
				}
			} else {
				shocks = plyr.getEp().createPiece(lc.getCard());
				lc.setPlayed(true);
			}
		} else if ((nC+nLC) == 2) { // two card play
			LawCard lc1 = (LawCard)selectedLawCards.remove(0);
			Card c1 = lc1.getCard();
			if (lc1.isPlayed()) {
				JOptionPane.showMessageDialog(this, "Law Card "+c1+" has already been played");
				return shocks;
			}
			
			LawCard lc2 = null;
			Card c2 = null;
			if (nLC == 1)
				c2 = (Card)selectedCards.get(0);
			else {
				lc2 = (LawCard)selectedLawCards.remove(0);
				c2 = lc2.getCard();
				if (lc2.isPlayed()) {
					JOptionPane.showMessageDialog(this, "Law Card "+c2+" has already been played");
					return shocks;
				}
			}
			//System.out.println("Trying to play "+c1+" with "+c2);
			Card newCard = Card.playsWith(c1,c2);
			if (newCard == null) {
				JOptionPane.showMessageDialog(this, "These cards do not play together.");
				deselectAllButtons();
				return shocks;
			} else {
				shocks = plyr.getEp().createPiece(newCard);
				lc1.setPlayed(true);
				if (lc2 != null)
					lc2.setPlayed(true);
				else
					plyr.getPocHand().remove(c2);
				plyr.takeCardPlays(nC);
			}
		} else if ((nC+nLC) == 3) { // trips
		    LawCard lc1 = (LawCard)selectedLawCards.remove(0);
			Card c1 = lc1.getCard();
			if (lc1.isPlayed()) {
				JOptionPane.showMessageDialog(this, "Law Card "+c1+" has already been played");
				return shocks;
			}
			
			LawCard lc2 = null;
			LawCard lc3 = null;
			Card c2 = null;
			Card c3 = null;
			if (nLC == 1) { // two cards from hand
				c2 = (Card)selectedCards.get(0);
				c3 = (Card)selectedCards.get(1);
			} else if (nLC == 2) {
				lc2 = (LawCard)selectedLawCards.remove(0);
				c2 = lc2.getCard();
				c3 = (Card)selectedCards.get(0);
				if (lc2.isPlayed()) {
					JOptionPane.showMessageDialog(this, "Law Card "+c2+" has already been played");
					return shocks;
				}
			} else { // playing trip law cards???
			    System.out.println("trip law card play?");
			    lc2 = (LawCard)selectedLawCards.remove(0);
			    lc3 = (LawCard)selectedLawCards.remove(0);
			    c2 = lc2.getCard();
			    c3 = lc3.getCard();
			    if (lc2.isPlayed()) {
					JOptionPane.showMessageDialog(this, "Law Card "+c2+" has already been played");
					return shocks;
				}
			    if (lc3.isPlayed()) {
					JOptionPane.showMessageDialog(this, "Law Card "+c3+" has already been played");
					return shocks;
				}
			}
			System.out.println("Playing trips! "+c1+", "+c2+", "+c3);
			Card newCard = Card.playTrips(c1,c2,c3);
			if (newCard == null) {
				JOptionPane.showMessageDialog(this, "These cards do not play together.");
				deselectAllButtons();
				return shocks;
			} else {
				shocks = plyr.getEp().createTwoPieces(newCard);
				lc1.setPlayed(true);
				if (lc2 != null)
					lc2.setPlayed(true);
				else
					plyr.getPocHand().remove(c2);
				if (lc3 != null)
					lc3.setPlayed(true);
				else
					plyr.getPocHand().remove(c3);
				plyr.takeCardPlays(nC);
			}
		}
		chPanel.deselectAllButtons();
		deselectAllButtons();
		return shocks;
	}

	public ArrayList getSelectedLawCards(Player plyr) {
		ArrayList selCards = new ArrayList();
		for (int i=0; i<lawCards.getComponentCount(); i++) {
			JToggleButton btn = (JToggleButton)lawCards.getComponent(i);
			if (btn.isSelected()) {
				selCards.add(plyr.getLawsInPlay().get(i));
			}
		}
		return selCards;
	}

	/**
	 * Method deselectAllButtons.
	 */
	public void deselectAllButtons() {
		for (int i=0; i<lawCards.getComponentCount(); i++) {
			JToggleButton btn = (JToggleButton)lawCards.getComponent(i);
			btn.setSelected(false);
		}
	}
}
