/*
 * Created on Jul 1, 2004
 */
package cbg.boardParts;

import cbg.player.*;

/**
 * @author Stephen Chudleigh
 **/
public class ImpSpace extends BoardSpace {
	public ImpSpace() {
		txtLable = "[Imp]";
	}
	public void landOn(Player p) {
		players.add(p);
		p.takeImpression();
	}

}