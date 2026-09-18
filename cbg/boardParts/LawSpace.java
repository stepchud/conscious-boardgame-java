/*
 * Created on Jul 1, 2004
 */
package cbg.boardParts;

import cbg.player.*;

/**
 * @author Stephen Chudleigh
 **/
public class LawSpace extends BoardSpace {
	public LawSpace() {
		txtLable = "[Law]";
	}
	public void landOn(Player p) {
		players.add(p);
		p.drawLawCard();
		p.giveMCMoment();
	}
	public String passOver() {
		return "You passed a law, draw one law card.\n";
	}
}