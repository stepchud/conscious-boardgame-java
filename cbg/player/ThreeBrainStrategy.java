/*
 * Created on Oct 26, 2006
 */
package cbg.player;

import javax.swing.JOptionPane;

import cbg.boardParts.Board;
import cbg.ui.CBGDlgFactory;
import cbg.ui.ConsciousBoardgameGUI;

/**
 * @author Stephen
 */
public class ThreeBrainStrategy extends PlayerStrategy {

    public void dies(Player thePlayer) {
        if (survivesDeath(thePlayer)) {
            if (thePlayer.getFd().hasChrystallizedMental()) {
                JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                    "You just died.\n"+
                    "Fortunately, you aquired a Mental body,\n"+
                    "which is immortal within the limits of this solar system.\n"+
                    "You may continue playing after death as long as it takes\n"+
                    "for you to complete yourself and Start Over.",
                    "Mental Death",
                    JOptionPane.PLAIN_MESSAGE
                );
            } else if (thePlayer.getFd().hasChrystallizedAstral()) {
                JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                    "You just died.\n"+
                    "Fortunately, you aquired an Astral body during life.\n"+
                    "You may continue around the board for one full trip,\n"+
                    "after which your Astral body will decay and you will need\n"+
                    "a mental body to survive.",
                    "Astral Death",
                    JOptionPane.PLAIN_MESSAGE
                );
            } else {
                System.err.println("You can't survive death without astral or mental body.");
                JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                    "You can't survive death without astral or mental body.\n"
                    +"If you are reading this message, please report this bug\n"
                    +"to the creator of the game, or just pretend like it never\n"
                    +"happened. Whichever is easier for your conscience.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(ConsciousBoardgameGUI.getInstance(),
                "I have some bad news. You just died.\n"+
                "Unofortunately, the body you aquired during life was not sufficient\n"+
                "to enable you to withstand the shock of death.\n\n"+
                "You have until the end of this turn to complete an Astral body,\n"+
                "or your game will be over.\n",
                "A Simple Death",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
    
    public boolean survivesDeath(Player thePlayer) {
        if (thePlayer.getFd().hasChrystallizedMental()) {
            return true;
        } else if ((thePlayer.getFd().hasChrystallizedAstral())
                    && (thePlayer.boardTrips <= 1)) {
			return true;
		} else return false;
	}

    public void startDeath(Player thePlayer) {
        System.out.println("start 3-brained being death game");
        thePlayer.isDead = true;
        thePlayer.setRollMultiple(1);
        Board.initDeathBoard();
        thePlayer.getFd().shiftAfterDeath();
        if (thePlayer.getPocHand().size()>7) {
            ConsciousBoardgameGUI.getInstance().keepSelectedCardsConfig();
        } else if (thePlayer.getPocHand().size()<7) {
            int added = 7 - thePlayer.getPocHand().size();
            for (int i=0; i<added; i++) {
                thePlayer.drawPOCCard(true);
            }
            CBGDlgFactory.displayInformationMessage("Draw up to 7",
                    "You drew "+added+" cards to start death.\n");
        }
    }

    public boolean isHasnamuss() {
        return false;
    }

    public int getRollMultiple() {
        return 1;
    }
}
