/*
 * Created on Jun 30, 2004
 */
package cbg.boardParts;

import cbg.player.*;

/**
 * @author Stephen Chudleigh
 */
public class FoodSpace extends BoardSpace {
	public FoodSpace() {
		txtLable = "[Food]";
	}
	public void landOn(Player p) {
		players.add(p);
		p.eat();
	}

}
