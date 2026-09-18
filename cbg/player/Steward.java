/*
 * Created on Jul 1, 2004
 */
package cbg.player;

/**
 * @author Stephen Chudleigh
*/
public class Steward extends LevelOfBeing {
	public Steward() {
		cardPlays=3;
		level = STEWARD;
	}
	public LevelOfBeing increaseLevelOfBeing() {
		return new Master();
	}
	
	/* (non-Javadoc)
	 * @see player.Player#hasAttainedNewLevelOfBeing()
	 */
	public boolean hasAttainedNewLevelOfBeing(EssenceAndPersonality ep) {
		return ep.has1001Words();
	}
}
