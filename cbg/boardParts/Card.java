/*
 * Created on Feb 11, 2004
 *
 * Author: Stephen Chudleigh
 */
package cbg.boardParts;

/**
 * @author Stephen Chudleigh
 */
public class Card implements Comparable {
	
	public static final Card JS = new Card(Card.JACK,Card.SPADES);
	public static final Card QS = new Card(Card.QUEEN,Card.SPADES);
	public static final Card KS = new Card(Card.KING,Card.SPADES);
	public static final Card JH = new Card(Card.JACK,Card.HEARTS);
	public static final Card QH = new Card(Card.QUEEN,Card.HEARTS);
	public static final Card KH = new Card(Card.KING,Card.HEARTS);
	public static final Card JC = new Card(Card.JACK,Card.CLUBS);
	public static final Card QC = new Card(Card.QUEEN,Card.CLUBS);
	public static final Card KC = new Card(Card.KING,Card.CLUBS);
	public static final Card JD = new Card(Card.JACK,Card.DIAMONDS);
	public static final Card QD = new Card(Card.QUEEN,Card.DIAMONDS);
	public static final Card KD = new Card(Card.KING,Card.DIAMONDS);
	public static final Card AS = new Card(Card.ACE,Card.SPADES);
	public static final Card AH = new Card(Card.ACE,Card.HEARTS);
	public static final Card AC = new Card(Card.ACE,Card.CLUBS);
	public static final Card AD = new Card(Card.ACE,Card.DIAMONDS);
	public static final Card XJ = new Card(Card.X_J, Card.JOKERS);
	public static final Card JO = new Card(Card.JOKR, Card.JOKERS);
	
	/// RANK
	public static final int JACK=11, QUEEN=12, KING=13, ACE=14, X_J=15, JOKR=16;
	/// SUIT
	public static final int JOKERS=0, SPADES=1, HEARTS=2, CLUBS=3, DIAMONDS=4;
	
	protected int rank;
	protected int suit;
	
	public Card(int r, int s) {
		this.rank = r;
		this.suit = s;
	}

	public int getRank() {
		return rank;
	}
	public int getSuit() {
		return suit;
	}
	public void setRank(int i) {
		rank = i;
	}
	public void setSuit(int i) {
		suit = i;
	}
	public boolean isFaceCard() {
		if (this.rank > 10) return true;
		return false;
	}
	public String toString() {
		String cardVal;
		switch (rank) {
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
			case Card.JACK: cardVal="Jack";
			break;
			case Card.QUEEN: cardVal="Queen";
			break;
			case Card.KING: cardVal="King";
			break;
			case Card.ACE:	cardVal="Ace";
			break;
			case Card.X_J: return "EXTRA JOKER";
			case Card.JOKR: return "JOKER";
			default: return "NotACard";
		}
		cardVal += " of";
		switch (suit) {
			case Card.SPADES: cardVal+=" Spades";
			return cardVal;
			case Card.HEARTS: cardVal+=" Hearts";
			return cardVal;
			case Card.CLUBS: cardVal+=" Clubs";
			return cardVal;
			case Card.DIAMONDS: cardVal+=" Diamonds";
			return cardVal;
		}
		return "NotACard";
	}
	
	public boolean equals(Object o) {
		if ((o != null) && (o.getClass().equals(this.getClass())))
		{
			return (((Card)o).suit == this.suit) 
				&& (((Card)o).rank == this.rank);
		}
		return false;
	}

	public int hashCode () {
			return rank*10+suit;
	}
	
	/**
	 * Method compareTo will throw NullPointerException if aCard is null.
	 * @param aCard
	 * @return int
	 */
	public int compareTo(Object o) {
		
		Card aCard = (Card)o;
		
		final int EQUAL=0,
			GREATER=1,
			LESS=-1;
			
		if (this == aCard)
			return EQUAL;
		
		if (this.suit > aCard.suit) {
			return GREATER;
		} else if (this.suit < aCard.suit) {
			return LESS;
		} else { // suits are equal
			if (this.rank < aCard.rank)
				return LESS;
			else if (this.rank > aCard.rank)
				return GREATER;
		}
			
		//all comparisons have yielded equality
	    //verify that compareTo is consistent with equals (optional)
	    //assert this.equals(aThat) : "compareTo inconsistent with equals.";
		if (!this.equals(aCard))
			System.err.println("compareTo inconsistent with equals: !"
					+this.toString()+".equals("+aCard+")");
		
	    return EQUAL;
	}
	
	/**
	 * Method playsWith.
	 * @param aCard
	 * @param bCard
	 * @return Card created by the card play, or else null if they do not play together
	 */
	public static Card playsWith(Card aCard, Card bCard) {
		if (aCard.equals(bCard))
			return null;

		if (!(aCard.suit == bCard.suit))
			return null;
			
		if (aCard.isFaceCard() || bCard.isFaceCard()) // must play face card by itself
			return null;
			
		if (aCard.rank < 5 && bCard.rank < 5) // same suit, different rank, between 2-4
			return new Card(Card.JACK, aCard.suit);
		
		if (aCard.rank > 7 && bCard.rank > 7) // same suit, different rank, between 8-10
			return new Card(Card.KING, aCard.suit);
			
		if ((aCard.rank >=5 && aCard.rank <= 7) && (bCard.rank >=5 && bCard.rank <= 7))
			return new Card(Card.QUEEN, aCard.suit);
		return null;
	}

    /**
     * @param c1
     * @param c2
     * @param c3
     * @return
     */
    public static Card playTrips(Card c1, Card c2, Card c3) {
        if (c1.equals(c2) || c1.equals(c3) || c2.equals(c3)) // must be 3 different ranks
            return null;
        if ((c1.suit != c2.suit) || (c1.suit != c3.suit)) // must be the same suit
            return null;
        if (c1.isFaceCard() || c2.isFaceCard() || c3.isFaceCard()) // must play face cards separate
            return null;
        if (c1.rank < 5 && c2.rank < 5 && c3.rank < 5)
            return new Card(Card.JACK, c1.suit);
        if (c1.rank > 7 && c2.rank > 7 && c3.rank > 7)
            return new Card(Card.KING, c1.suit);
        if (c1.rank >= 5 && c1.rank <= 7 && c2.rank >= 5 && c2.rank <= 7 && c3.rank >= 5 && c3.rank <= 7)
             return new Card(Card.QUEEN, c1.suit);
        // else
        return null;
    }
}
