/*
 * Created on Jul 1, 2004
 */
package cbg.player;
import cbg.boardParts.*;
/**
 * @author Stephen Chudleigh
 **/
public class Nothing extends LevelOfBeing {
	
	short currentChipsOnJoker;
	
	public Nothing() {
		currentChipsOnJoker = 5;
		level = NOTHING;
	}
	public LevelOfBeing increaseLevelOfBeing() {
		return this;
	}

	/* (non-Javadoc)
	 * @see player.Player#hasAttainedNewLevelOfBeing()
	 */
	public boolean hasAttainedNewLevelOfBeing(EssenceAndPersonality ep) {
		if (currentChipsOnJoker < ep.getChipsFor(Card.JO)) {
			currentChipsOnJoker = (short)ep.getChipsFor(Card.JO);
			return true;
		} else return false;
	}
}
