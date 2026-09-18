/*
 * Created on Jul 1, 2004
 */
package cbg.player;

/**
 * @author db2admin
 */
public class Master extends LevelOfBeing {
	public Master() {
		cardPlays=4;
		level=MASTER;
	}
	public LevelOfBeing increaseLevelOfBeing() {
		return new Double();
	}
	public int getPlaysPerTurn() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see player.Player#hasAttainedNewLevelOfBeing()
	 */
	public boolean hasAttainedNewLevelOfBeing(EssenceAndPersonality ep) {
        return false;
		// get rid of Double for now
        // return ep.hasDoubled();
	}
}
