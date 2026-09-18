/*
 * Created on Jun 30, 2004
 */
package cbg.boardParts;

/**
 * @author Stephen Chudleigh
 */
public class Board {
	private static BoardSpace [] board;
	private static final String spaces = "*AFAIFCACFFCIAACFFICAFACCFICFAAFCCLAFIC" +
								"CFAFICAFCCIAACFFICAICAFFICCAAIFCLLCFFI" +
								"AAICCFIACIFACIAFICAILFCAACICCFAICFFACI" +
								"CAIFCCFICACFALLCCFACCCFICFCAICCIAFFICA" +
								"ALCCIFACCCIFICAACCICFFCCIAFCCALLCCCAFF" +
								"ACIAFCCIACFACILCAFFCCAIAFCCIACFFICCCAI" +
								"CCFCALLCCAAFCIC*";
	private Board() {
		init();
	}
	public static void init() {
		//System.out.println("Making the board with "+spaces.length()+" spaces.");
		board = new BoardSpace[spaces.length()];
		for (int i=0; i<spaces.length(); i++) {
			switch (spaces.charAt(i)) {
				case 'F':
						board[i] = new FoodSpace();
						break;
				case 'A':
						board[i] = new AirSpace();
						break;
				case 'I':
						board[i] = new ImpSpace();
						break;
				case 'L':
						board[i] = new LawSpace();
						break;
				case 'C':
						board[i] = new CardSpace();
						break;
				case '*':
						board[i] = new WildSpace();
						break;
			}
		}
	}
	public static void initDeathBoard() {
		System.out.println("Making the afterlife board.");
		for (int i=0; i<board.length; i++) {
			switch (board[i].toString().charAt(1)) {
				case 'L':
						board[i] = new WildSpace();
						break;
				case 'C':
						board[i] = new DecaySpace();
						break;
			}
		}
	}
	public static void reverseBoard() {
		int left  = 0;          // index of leftmost element
		int right = board.length-1; // index of rightmost element
		while (left < right) {
			// exchange the left and right elements
			BoardSpace temp = board[left]; 
			board[left]  = board[right]; 
			board[right] = temp;

			// move the bounds toward the center
			left++;
			right--;
		}
	}
	public static BoardSpace getSpace(int i) {
		if (i >= spaces.length())
			i = spaces.length()-1;
		return board[i];
	}
	public static int getNumSpaces() {
		return spaces.length();
	}
	public static String nextTen(int pos) {
		String nextten = "";
		int maxpos = 10;
		if (getNumSpaces() <= pos+maxpos)
			maxpos = getNumSpaces()-pos;
		for (int i=0; i<maxpos; i++) {
			nextten += board[pos+i];
		}
		return nextten;
	}
	public static String next30(int pos) {
		int maxpos = 30;
		if (getNumSpaces() <= pos+maxpos)
			return spaces.substring(pos);
		
		return spaces.substring(pos, maxpos);
	}
	public static void printBoard() {
		System.out.print("Board=");
		for (int i=0; i<spaces.length(); i++) {
			System.out.print(board[i]);
		}
		System.out.println();
	}
	public static int getSpaceCharAt(int spaceNum) {
		return spaces.charAt(spaceNum);
	}
}
