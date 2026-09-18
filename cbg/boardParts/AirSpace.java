/*
 * Created on Jul 1, 2004
 */
package cbg.boardParts;

import cbg.player.*;

/**
 * @author Stephen Chudleigh
 **/
public class AirSpace extends BoardSpace {
	public AirSpace() {
		txtLable = "[Air]";
	}
	public void landOn(Player p) {
		players.add(p);
		p.breathe();
	}

}
