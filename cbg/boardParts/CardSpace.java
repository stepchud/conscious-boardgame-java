/*
 * Created on Jul 1, 2004
 */
package cbg.boardParts;

import cbg.player.*;

/**
 * @author Stephen Chudleigh
 **/
public class CardSpace extends BoardSpace {
	public CardSpace() {
		txtLable = "[Card]";
	}
	public void landOn(Player p) {
		players.add(p);
		p.drawPOCCard(false);
	}
}