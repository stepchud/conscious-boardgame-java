/*
 * Created on Feb 24, 2004
 *
 * Author: Stephen Chudleigh
 */
package cbg.boardParts;
import java.awt.Color;
import java.util.Random;

public class Dice {
	
	private static Random rand;
	private static int numSides;
	private static int currentRoll;
	private static boolean hasZero;
	private static Color color;
	private static Dice instance;

	private Dice() {
		rand = new Random(System.currentTimeMillis());
		//	Default behavior: 10 sided die with a zero side
		//	Use setNumSides or setHasZero to change behavior
		Dice.hasZero = true;
		Dice.numSides = 10;
		Dice.color = Color.red;
	}
	public static Dice getInstance() {
		if (instance == null)
			instance = new Dice();
		return instance;
	}
	public static void init() {
		rand = new Random(System.currentTimeMillis());
		//	Default behavior: 10 sided die with a zero side
		//	Use setNumSides or setHasZero to change behavior
		Dice.hasZero = true;
		Dice.numSides = 10;
		Dice.color = Color.red;
	}
	public static void init(int s) {
		rand = new Random(System.currentTimeMillis());
		//	Default behavior: 10 sided die with a zero side
		//	Use setNumSides or setHasZero to change behavior
		Dice.hasZero = true;
		Dice.numSides = s;
		
	}
	public static int getNumSides() {
		return numSides;
	}
	public static void setNumSides(int i) {
		numSides = i;
	}
	public static boolean hasZero() {
		return hasZero;
	}
	public static void setHasZero(boolean b) {
		hasZero = b;
	}
	public static int roll() {
		if (hasZero) {
			currentRoll = rand.nextInt(numSides);
		} else {
			currentRoll = rand.nextInt(numSides)+1;
		}
		return currentRoll; 
	}
	/**
	 * @return color
	 */
	public static Color getColor() {
		return color;
	}
	/**
	 * @param color
	 */
	public static void setColor(Color color) {
		Dice.color = color;
	}

	/**
	 * @return
	 */
	public static int getRoll() {
		return currentRoll;
	}

	/**
	 * Method getOpposite.
	 * @return int
	 */
	public static int getOpposite() {
		int maxRoll = numSides;
		if (hasZero) maxRoll--;
		return (maxRoll-currentRoll);
	}

}
