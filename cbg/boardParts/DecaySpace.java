package cbg.boardParts;

import cbg.player.Player;

/**
 * @author Stephen Chudleigh
 * Created on Jan 4, 2006
 */
public class DecaySpace extends BoardSpace {

	/**
	 * Constructor for DecaySpace.
	 */
	public DecaySpace() {
		txtLable = "[Decay]";
	}

	/**
	 * @see cbg.boardParts.BoardSpace#landOn(Player)
	 */
	public void landOn(Player p) {
		players.add(p);
		p.decays();
	}

}
