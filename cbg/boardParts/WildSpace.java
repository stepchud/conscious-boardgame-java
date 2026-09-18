/*
 * Created on Jul 1, 2004
 */
package cbg.boardParts;

import cbg.player.*;

/**
 * @author Stephen Chudleigh
 **/
public class WildSpace extends BoardSpace {
	public WildSpace() {
		txtLable = "[WILD]";
	}
	public void landOn(Player p) {
		players.add(p);
		p.giveWildChoice();
	}
}