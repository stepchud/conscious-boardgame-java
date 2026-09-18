/*
 * Created on Jun 30, 2004
 */
package cbg.boardParts;
import java.util.ArrayList;

import cbg.player.*;

/**
 * @author Stephen Chudleigh
 */
public abstract class BoardSpace implements cbg.common.UIConsts {
	
	protected ArrayList players;
	protected int boardPos;
	protected String txtLable;
	
	public BoardSpace() {
		players = new ArrayList();
	}
	public String passOver() {
		return null;
	}
	public void leave(Player p) {
		players.remove(p);
	}
	
	public abstract void landOn(Player p);
	
	public boolean hasPlayers() {
		if (players.isEmpty()) return false;

		return true;
	}
	public String toString() {
		return txtLable;
	}
}
