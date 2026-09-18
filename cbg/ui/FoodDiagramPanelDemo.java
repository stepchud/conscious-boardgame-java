package cbg.ui;

import javax.swing.*;
import java.awt.*;

import cbg.boardParts.Decks;
import cbg.common.UIConsts;

/**
 * @author U778519
 */
public class FoodDiagramPanelDemo extends JPanel
									implements UIConsts {

	private FoodDiagramPanel fdPanel;

	/**
	 * Constructor FoodDiagramPanelDemo.
	 * @param frame
	 */
	public FoodDiagramPanelDemo() {
	}

	public static void main(String[] args) {
		IconFactory.createImageIcons();
		Decks.init();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}

	/**
	 * Method createAndShowGUI.
	 */
	private static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(false);

        //Create and set up the window.
        JFrame frame = new JFrame("FoodDiagramPanelDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		FoodDiagramPanelDemo fdDemo = new FoodDiagramPanelDemo();
	
        //Create and set up the content pane.
        Container contentPane = frame.getContentPane();
        contentPane.add(Box.createHorizontalStrut(50));
		contentPane.add(fdDemo.createDiagram(), BorderLayout.CENTER);
		contentPane.add(fdDemo.createButtonPane(), BorderLayout.PAGE_START);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}

	/**
	 * Method createDiagram.
	 * @return Component
	 */
	private Component createDiagram() {
		fdPanel = new FoodDiagramPanel();
		return fdPanel;
	}

	
    //Create the button that goes in the main window.
    protected JComponent createButtonPane() {

		JButton eatButton = new JButton("Eat");
		JButton breatheButton = new JButton("Breathe");
		JButton impButton = new JButton("Impression");
		JButton bweButton = new JButton("Shock Food");
		JButton slfRemButton = new JButton("Self-Remember");
		JButton transformButton = new JButton("Tranform Emotions");
		JButton endTurn = new JButton("End Turn, Move Pieces");
		JButton die = new JButton("Die!");

		eatButton.setActionCommand(EAT_COMMAND);
		//eatButton.addActionListener(fdPanel);
		breatheButton.setActionCommand(BREATHE_COMMAND);
		//breatheButton.addActionListener(fdPanel);
		impButton.setActionCommand(IMP_COMMAND);
		//impButton.addActionListener(fdPanel);
		bweButton.setActionCommand(SHOCK_FOOD_COMMAND);
		//bweButton.addActionListener(fdPanel);
		slfRemButton.setActionCommand(SELF_REMEMBER_COMMAND);
		//slfRemButton.addActionListener(fdPanel);
		transformButton.setActionCommand(TRANSFORM_COMMAND);
		//transformButton.addActionListener(fdPanel);
		endTurn.setActionCommand(END_TURN_COMMAND);
		//endTurn.addActionListener(fdPanel);
		die.setActionCommand(DEATH_COMMAND);
		//die.addActionListener(fdPanel);
		
        //Center the button in a panel with some space around it.
		JPanel buttonPanel = new JPanel();//use default FlowLayout
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        buttonPanel.add(eatButton);
        buttonPanel.add(breatheButton);
        buttonPanel.add(impButton);
        buttonPanel.add(bweButton);
        buttonPanel.add(slfRemButton);
        buttonPanel.add(transformButton);
        buttonPanel.add(endTurn);
        buttonPanel.add(die);

        return buttonPanel;
    }
}
