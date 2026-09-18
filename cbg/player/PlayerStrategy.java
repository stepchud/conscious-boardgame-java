/**
 * 
 */
package cbg.player;

/**
 * This abstract class provides different behaviors for the player.
 * This is implemented using the Strategy Pattern (315) from the GoF book
 * in order to encapsulate the behaviors outside the Player class.
 * 
 * @author Stephen
 */
public abstract class PlayerStrategy {

    public abstract boolean isHasnamuss();
    public abstract boolean survivesDeath(Player p);
    public abstract void dies(Player p);
    public abstract void startDeath(Player p);

}
