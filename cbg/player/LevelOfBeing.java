/*
 * Created on Jan 17, 2005
 *
 * Author: Stephen Chudleigh
 */
package cbg.player;

/**
 * @author Stephen Chudleigh
 * ConsciousBoardgame
 * Jan 17, 2005
 */
public abstract class LevelOfBeing {

	public static final String MULTIPLICITY="Multiplicity",
                                DOUBLE="Double",
                                MASTER="Master",
                                NOTHING="Nothing",
                                STEWARD="Steward",
                                STUDENT="Student";
	
	protected String level;
	protected int cardPlays;
	
	public abstract boolean hasAttainedNewLevelOfBeing(EssenceAndPersonality ep);
	public abstract LevelOfBeing increaseLevelOfBeing();
	public String toString() {
		return level;
	}
	
	public int getPlaysPerTurn(){
		return cardPlays;
	}

	
	/**
	 * Returns the level.
	 * @return String
	 */
	public String getLevel() {
		return level;
	}

}
