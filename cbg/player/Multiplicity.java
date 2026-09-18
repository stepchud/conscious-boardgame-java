/*
 * Created on Jul 1, 2004
 */
package cbg.player;

/**
 * @author Stephen Chudleigh 
 **/
public class Multiplicity extends LevelOfBeing {
	public Multiplicity() {
		super();
		cardPlays=1;
		level = MULTIPLICITY;
	}
	public LevelOfBeing increaseLevelOfBeing() {
		return new Student();
	}

	public boolean hasAttainedNewLevelOfBeing(EssenceAndPersonality ep) {
		return ep.foundSchool();
	}
}
