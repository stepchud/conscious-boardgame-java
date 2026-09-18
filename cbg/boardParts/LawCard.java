/*
 * Created on Feb 24, 2004
 * Author: Stephen Chudleigh
 */
package cbg.boardParts;

public class LawCard {

	public static LawCard KingDiamonds, KingClubs, KingHearts, KingSpades, Joker;
	private Card card;
	private String lawDesc;
	private int lawValue;

	private boolean played = false;
	private boolean obeyed = false;
	
	public LawCard(int val, Card c, String desc) {
		lawValue = val;
		card = c;
		lawDesc = desc;
	}
	
	/**
	 * @return
	 */
	public String getLawDesc() {
		return lawDesc;
	}

	/**
	 * @return
	 */
	public int getLawValue() {
		return lawValue;
	}

	/**
	 * Method getCard.
	 */
	public Card getCard() {
		return card;
	}
	/**
	 * Returns the obeyed.
	 * @return boolean
	 */
	public boolean isObeyed() {
		return obeyed;
	}

	/**
	 * Returns the played.
	 * @return boolean
	 */
	public boolean isPlayed() {
		return played;
	}

	/**
	 * Sets the obeyed.
	 * @param obeyed The obeyed to set
	 */
	public void setObeyed(boolean obeyed) {
		this.obeyed = obeyed;
	}

	/**
	 * Sets the played.
	 * @param played The played to set
	 */
	public void setPlayed(boolean played) {
		this.played = played;
	}
		
	/**
	 * @param string
	 */
	public void setLawDesc(String string) {
		lawDesc = string;
	}

	/**
	 * @param i
	 */
	public void setLawValue(int i) {
		lawValue = i;
	}

	public boolean equals(Object o) {
		if ((o != null) && (o.getClass().equals(this.getClass())))
		{
			if (((LawCard)o).getCard().equals(this.getCard()))
				if (((LawCard)o).getLawValue() == this.getLawValue())
					if (((LawCard)o).getLawDesc().equals(this.getLawDesc()))
						return true;
		}
		return false;
	}

	public int hashCode () {
			return this.getLawDesc().hashCode()
					*this.getCard().hashCode()
					*this.getLawValue();
	}
	
	public String toString() {
		String cardVal;
		switch (card.rank) {
			case 2: cardVal="2";
			break;
			case 3: cardVal="3";
			break;
			case 4: cardVal="4";
			break;
			case 5: cardVal="5";
			break;
			case 6: cardVal="6";
			break;
			case 7: cardVal="7";
			break;
			case 8: cardVal="8";
			break;
			case 9: cardVal="9";
			break;
			case 10: cardVal="10";
			break;
			case Card.JACK: cardVal="Jk";
			break;
			case Card.QUEEN: cardVal="Qu";
			break;
			case Card.KING: cardVal="Kg";
			break;
			case Card.X_J: cardVal="X_J";
			case Card.JOKR: cardVal="JOKR";
			default: return "NotACard:rank";
		}
		switch (card.suit) {
			case Card.SPADES: cardVal+="S";
			break;
			case Card.HEARTS: cardVal+="H";
			break;
			case Card.CLUBS: cardVal+="C";
			break;
			case Card.DIAMONDS: cardVal+="D";
			break;
			case Card.JOKERS: break;
			default: return "NotACard:suit";
		}
		return cardVal+"-"+lawDesc;
	}
}
