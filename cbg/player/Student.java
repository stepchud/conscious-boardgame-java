/*
 * Created on Jul 1, 2004
 */
package cbg.player;

/**
 * @author Stephen Chudleigh
 */
public class Student extends LevelOfBeing {
	
	public Student() {
		cardPlays=2;
		level = STUDENT;
	}
	
	public LevelOfBeing increaseLevelOfBeing() {
		return new Steward();
	}

	/* (non-Javadoc)
	 * @see player.Player#hasAttainedNewLevelOfBeing()
	 */
	public boolean hasAttainedNewLevelOfBeing(EssenceAndPersonality ep) {
		return ep.hasAprilFools();
	}
}
