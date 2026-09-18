/*
 * Created on Jul 1, 2004
 */
package cbg.player;

/**
 * @author db2admin
 */
public class Double extends LevelOfBeing {
	public Double() {
		cardPlays=4;
		level=DOUBLE;
	}
	public LevelOfBeing increaseLevelOfBeing() {
		return new Nothing();
	}

	/* (non-Javadoc)
	 * @see player.Player#hasAttainedNewLevelOfBeing()
	 */
	public boolean hasAttainedNewLevelOfBeing(EssenceAndPersonality ep) {
		return ep.hasNothing();
	}
}
