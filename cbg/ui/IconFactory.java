package cbg.ui;

import javax.swing.ImageIcon;

import cbg.boardParts.Card;
import cbg.common.UIConsts;

/**
 * @author Stephen Chudleigh
 * Created on Dec 30, 2005
 */
public class IconFactory implements UIConsts {
	
	public static ImageIcon [][] cardIcons;

	public static ImageIcon [] chipIcons, faceCardIcons;

	public static ImageIcon cvgIcon,
					zeroPieceIcon,
					onePieceIcon,
					twoPieceIcon,
					threePieceIcon,
					fourPieceIcon,
					fivePieceIcon,
					diceIcon;
					
	public static ImageIcon chipIcon, 
					astralIcon,
					mentalIcon,
					caIcon,
					cmIcon,
					amIcon,
					camIcon,
					noChipIcon,
					oneAccrueIcon,
					twoAccrueIcon,
					oneVertAccrueIcon,
					twoVertAccrueIcon,
					diagramIcon;

	private static final String GameIcon = "/img/cardback.gif";
	
	private static final String [][] ChipImages =
		{ {"/img/clearsp.gif", "Transparent Icon"},
			{"/img/chip.gif", "Stuff Chip"},
			{"/img/astral.gif", "Astral Chip"},
			{"/img/mental.gif", "Mental Chip"},
			{"/img/ca.gif", "Chip plus Astral"},
			{"/img/cm.gif", "Chip plus Mental"},
			{"/img/am.gif", "Astral plus Mental Chips"},
			{"/img/cam.gif", "Stuff Astral Mental Chips"},
			{"/img/one_accum.gif", "One extra Chip"},
			{"/img/two_accum.gif", "Two extra Chips"},
			{"/img/one_accum_v.gif", "One extra chip vertical"},
			{"/img/two_accum_v.gif", "Two extra chips vertical"},
			{"/img/fd.gif", "Food Diagram"},
			{"/img/clear.gif", "clear"},
			{"/img/one_piece.gif", "one piece"},
			{"/img/two_piece.gif", "two pieces"},
			{"/img/three_piece.gif", "three piece"},
			{"/img/four_piece.gif", "four pieces"},
			{"/img/five_piece.gif", "five piece"}
		};
		
	private static final String [][] FaceCardImages = 
		{ {"/img/sAd.gif", "Ace Diamonds"},
			{"/img/sAc.gif", "Ace Clubs"},
			{"/img/sAh.gif", "Ace Hearts"},
			{"/img/sAs.gif", "Ace Spades"},
			{"/img/sxjoker.gif", "Extra Joker"},
			{"/img/sjoker.gif", "Joker"},
			{"/img/sJh.gif", "Jack Hearts"},
			{"/img/sQh.gif", "Queen Hearts"},
			{"/img/sKh.gif", "King Hearts"},
			{"/img/sJs.gif", "Jack Spades"},
			{"/img/sQs.gif", "Queen Spades"},
			{"/img/sKs.gif", "King Spades"},
			{"/img/sJd.gif", "Jack Diamonds"},
			{"/img/sQd.gif", "Queen Diamonds"},
			{"/img/sKd.gif", "King Diamonds"},
			{"/img/sJc.gif", "Jack Clubs"},
			{"/img/sQc.gif", "Queen Clubs"},
			{"/img/sKc.gif", "King Clubs"}
		};
		
	/**
	 * double array of card images [ rank ][ suit ]
	 */
	private static final String [][] CardImages = 
		{
			{"/img/clearCard.gif", null, null, null, null},
			{null, null, null, null},
			{null, "/img/s2s.gif", "/img/s2h.gif", "/img/s2c.gif", "/img/s2d.gif"},
			{null, "/img/s3s.gif", "/img/s3h.gif", "/img/s3c.gif", "/img/s3d.gif"},
			{null, "/img/s4s.gif", "/img/s4h.gif", "/img/s4c.gif", "/img/s4d.gif"},
			{null, "/img/s5s.gif", "/img/s5h.gif", "/img/s5c.gif", "/img/s5d.gif"},
			{null, "/img/s6s.gif", "/img/s6h.gif", "/img/s6c.gif", "/img/s6d.gif"},
			{null, "/img/s7s.gif", "/img/s7h.gif", "/img/s7c.gif", "/img/s7d.gif"},
			{null, "/img/s8s.gif", "/img/s8h.gif", "/img/s8c.gif", "/img/s8d.gif"},
			{null, "/img/s9s.gif", "/img/s9h.gif", "/img/s9c.gif", "/img/s9d.gif"},
			{null, "/img/s10s.gif", "/img/s10h.gif", "/img/s10c.gif", "/img/s10d.gif"},
			{null, "/img/sJs.gif", "/img/sJh.gif", "/img/sJc.gif", "/img/sJd.gif"},
			{null, "/img/sQs.gif", "/img/sQh.gif", "/img/sQc.gif", "/img/sQd.gif"},
			{null, "/img/sKs.gif", "/img/sKh.gif", "/img/sKc.gif", "/img/sKd.gif"},
			{null, "/img/sAs.gif", "/img/sAh.gif", "/img/sAc.gif", "/img/sAd.gif"},
			{"/img/sxjoker.gif", null, null, null, null},
			{"/img/sjoker.gif", null, null, null, null}
		};
		
	/**
	 * Constructor for IconFactory.
	 */
	private IconFactory() {
		super();
		createImageIcons();
	}

	/**
	 * Method createImageIcons.
	 */
	public static void createImageIcons() {
		cvgIcon = createImageIcon(GameIcon, "Game Window Icon");
		cardIcons = new ImageIcon [Card.JOKR+1][Card.DIAMONDS+1];
		cardIcons[0][0] = createImageIcon(CardImages[0][0], "Clear Card");
		for (int i=2; i<Card.X_J; i++) {
			for (int j=Card.SPADES; j<=Card.DIAMONDS; j++) {
				//System.out.println("Getting "+CardImages[i][j]+" i="+i+" j="+j);
				cardIcons[i][j] = createImageIcon(CardImages[i][j], ((new Card(i,j)).toString()));
			}
		}
		//System.out.println("Getting "+CardImages[Card.X_J][Card.JOKERS]);
		cardIcons[Card.X_J][Card.JOKERS] = 
			createImageIcon(
				CardImages[Card.X_J][Card.JOKERS], 
				((new Card(Card.X_J,Card.JOKERS)).toString())
			);
		//System.out.println("Getting "+CardImages[Card.JOKR][Card.JOKERS]);
		cardIcons[Card.JOKR][Card.JOKERS] =
			createImageIcon(
				CardImages[Card.JOKR][Card.JOKERS],
				((new Card(Card.JOKR,Card.JOKERS)).toString())
			);
		
		faceCardIcons = new ImageIcon[FaceCardImages.length];
		for (int i=0; i<faceCardIcons.length; i++) {
			faceCardIcons[i] = createImageIcon(
				FaceCardImages[i][0], 
				FaceCardImages[i][1]
			);
		}
		
		zeroPieceIcon = createImageIcon(ChipImages[13][0],ChipImages[13][1]);
		onePieceIcon = createImageIcon(ChipImages[14][0],ChipImages[14][1]);
		twoPieceIcon = createImageIcon(ChipImages[15][0],ChipImages[15][1]);
		threePieceIcon = createImageIcon(ChipImages[16][0],ChipImages[16][1]);
		fourPieceIcon = createImageIcon(ChipImages[17][0],ChipImages[17][1]);
		fivePieceIcon = createImageIcon(ChipImages[18][0],ChipImages[18][1]);
		chipIcons = new ImageIcon[] {
				zeroPieceIcon,
				onePieceIcon,
				twoPieceIcon,
				threePieceIcon,
				fourPieceIcon,
				fivePieceIcon
		};
		
		noChipIcon = createImageIcon(ChipImages[0][0],ChipImages[0][1]);
		chipIcon = createImageIcon(ChipImages[1][0],ChipImages[1][1]);
		astralIcon = createImageIcon(ChipImages[2][0],ChipImages[2][1]);
		mentalIcon = createImageIcon(ChipImages[3][0],ChipImages[3][1]);
		caIcon = createImageIcon(ChipImages[4][0],ChipImages[4][1]);
		cmIcon = createImageIcon(ChipImages[5][0],ChipImages[5][1]);
		amIcon	 = createImageIcon(ChipImages[6][0],ChipImages[6][1]);
		camIcon	 = createImageIcon(ChipImages[7][0],ChipImages[7][1]);
		oneAccrueIcon = createImageIcon(ChipImages[8][0],ChipImages[8][1]);
		twoAccrueIcon = createImageIcon(ChipImages[9][0],ChipImages[9][1]);
		oneVertAccrueIcon = createImageIcon(ChipImages[10][0],ChipImages[10][1]);
		twoVertAccrueIcon = createImageIcon(ChipImages[11][0],ChipImages[11][1]);
		diagramIcon = createImageIcon(ChipImages[12][0],ChipImages[12][1]);
		
		diceIcon = createImageIcon("/img/dice.gif", "Red Die");
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = IconFactory.class.getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
