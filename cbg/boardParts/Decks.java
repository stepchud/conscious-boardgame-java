/*
 * Created on Jul 1, 2004
 */
package cbg.boardParts;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
/**
 * @author Stephen Chudleigh
 **/
public class Decks {
    private static boolean isLDInit=false;
	private static ArrayList pocDeck, newPocDeck, lawDeck, newLawDeck;
	private static Random random;
	
	private static void createPOCDeck() {
		random = new Random(System.currentTimeMillis());
		newPocDeck = new ArrayList(100);

		////////// Fill newPocDeck with cards ///////////////
		for (int i=Card.SPADES; i<=Card.DIAMONDS; i++)
		{
			for (int j=2; j<=4; j++)
			{
				for (int k=1; k<=4; k++)
				{
					newPocDeck.add(new Card(j,i));
				}
			}
		}
		for (int i=Card.SPADES; i<=Card.DIAMONDS; i++)
		{
			for (int j=5; j<=7; j++)
			{
				for (int k=1; k<=3; k++)
				{
					newPocDeck.add(new Card(j,i));
				}
			}
		}
		for (int i=Card.SPADES; i<=Card.DIAMONDS; i++)
		{
			for (int j=8; j<=10; j++)
			{
				for (int k=1; k<=2; k++)
				{
					newPocDeck.add(new Card(j,i));
				}
			}
		}
		newPocDeck.add(new Card(Card.JACK,Card.DIAMONDS));
		newPocDeck.add(new Card(Card.JACK,Card.DIAMONDS));
		newPocDeck.add(new Card(Card.JACK,Card.DIAMONDS));
		newPocDeck.add(new Card(Card.JACK,Card.CLUBS));
		newPocDeck.add(new Card(Card.JACK,Card.CLUBS));
		newPocDeck.add(new Card(Card.JACK,Card.HEARTS));
		newPocDeck.add(new Card(Card.JACK,Card.SPADES));
		newPocDeck.add(new Card(Card.JACK,Card.SPADES));
		newPocDeck.add(new Card(Card.JACK,Card.SPADES));
		newPocDeck.add(new Card(Card.JACK,Card.SPADES));
		newPocDeck.add(new Card(Card.QUEEN,Card.DIAMONDS));
		newPocDeck.add(new Card(Card.QUEEN,Card.DIAMONDS));
		newPocDeck.add(new Card(Card.QUEEN,Card.CLUBS));

        // copy new deck into used deck
        pocDeck = new ArrayList(newPocDeck);
        // and shuffle them
        for (int i=0; i<20; i++)
            shufflePocDeck();
        
        //System.out.println("POC Deck shuffled.");
		/*
		 * 
		 *System.out.println("Deck looks like:");
		Iterator it = newPocDeck.iterator();
		while (it.hasNext()) {
			System.out.print(it.next()+",");
		}
		System.out.println();*/
		////////////// End pocDeck creation ////////////////////
	}

	private static void shufflePocDeck() {
		ArrayList shuffledDeck = new ArrayList(100);
		shuffledDeck.add(pocDeck.remove(random.nextInt(pocDeck.size())));
		while (!pocDeck.isEmpty()) {
			Card c = (Card)pocDeck.remove(0);
			shuffledDeck.add(random.nextInt(shuffledDeck.size()+1), c);
		}

		pocDeck = shuffledDeck;
	}

	/**
	 * Method shufflePOCDeck.
	 */
	private static void reshufflePOCDeck() {
        // reuse new deck
        pocDeck = new ArrayList(newPocDeck);
        for (int i=0; i<10; i++) 
            shufflePocDeck();
	}

	private static void createLawDeck() {
		newLawDeck = new ArrayList();
		try {
			InputStream is = Decks.class.getResourceAsStream("/docs/LawCardText.txt");
			if (is == null)
				System.err.println("!!LawCardText.txt not found!!");
			BufferedReader br = new BufferedReader(
								new InputStreamReader(
								new BufferedInputStream(is)));
	        //    		new FileReader(is));
	        StringBuffer desc;
			String line = br.readLine().trim();
			while (line != null) {
				int cardNum = Integer.parseInt(line);
				line = br.readLine().trim();
				Card c = getCardForString(line);
				desc = new StringBuffer("<html>");
				line = br.readLine().trim();
				while (!line.equals("")) {
					desc.append(line).append("<br>");
					line = br.readLine();
				}
				desc.append("</html>");
				//System.out.println("Law #"+cardNum+" is "+c+", law text:\n"+desc);
				LawCard lc = new LawCard(cardNum, c, desc.toString());
				newLawDeck.add(lc);
				line = br.readLine();
				if (c.equals(Card.KD))
					LawCard.KingDiamonds = lc;
				else if (c.equals(Card.KC))
					LawCard.KingClubs = lc;
				else if (c.equals(Card.KH))
					LawCard.KingHearts = lc;
				else if (c.equals(Card.KS))
					LawCard.KingSpades = lc;
                else if (c.equals(Card.JO))
                    LawCard.Joker = lc;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
		lawDeck = new ArrayList(newLawDeck);
        isLDInit=true;
        //System.out.println("Law deck initialized, size="+lawDeck.size());
	}

	/**
	 * Method shuffleLawDeck.
	 */
	private static void shuffleLawDeck() {
        createLawDeck();
	}
	
	/**
	 * Method getCardForString.
	 * @param line
	 * @return Card
	 */
	private static Card getCardForString(String line) {
		if (line.startsWith("EXTRA"))
			return new Card(Card.X_J, Card.JOKERS);
		else if (line.startsWith("JOKER"))
			return new Card(Card.JOKR, Card.JOKERS);
			
		String rank = line.substring(0,line.length()-1);
		String suit = line.substring(line.length()-1);
		int iRank = Card.ACE;
		try {
			iRank = Integer.parseInt(rank);
		} catch (NumberFormatException nf) {
			if (rank.equals("J"))
				iRank = Card.JACK;
			else if (rank.equals("Q"))
				iRank = Card.QUEEN;
			else if (rank.equals("K"))
				iRank = Card.KING;
			else if (rank.equals("A"))
				iRank = Card.ACE;
		}
		int iSuit = Card.DIAMONDS;
		if (suit.equals("S"))
			iSuit = Card.SPADES;
		else if (suit.equals("H"))
			iSuit = Card.HEARTS;
		else if (suit.equals("C"))
			iSuit = Card.CLUBS;
			
		return new Card(iRank,iSuit);
	}


	public static void init() {
		createPOCDeck();
		shuffleLawDeck();		
	}

	public static Card drawPOCCard() {
		if (pocDeck.size()>0) {
			return (Card)pocDeck.remove(random.nextInt(pocDeck.size()));
		} else {
			System.out.println("No more POC cards, shuffling the deck.");
			reshufflePOCDeck();
		}
		return (Card)pocDeck.remove(random.nextInt(pocDeck.size()));
	}

	public static LawCard drawLawCard() {
		if (lawDeck.size()>0) {
			return (LawCard)lawDeck.remove(random.nextInt(lawDeck.size()));
		} else {
			System.out.println("No more LAW cards, shuffling the deck.");
			shuffleLawDeck();
			return (LawCard)lawDeck.remove(random.nextInt(lawDeck.size()));
		}
	}
    public static LawCard test_DrawJokerLaw() {
        if (lawDeck != null) {
            int lastIndex = lawDeck.size()-1;
            System.out.println("law deck size="+lawDeck.size());
            return (LawCard)lawDeck.remove(lastIndex); // draw JOKER
        } return null;
    }

    public static boolean isLDInit() {
        return isLDInit;
    }
}
