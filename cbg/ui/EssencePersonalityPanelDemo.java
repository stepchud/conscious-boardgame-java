package cbg.ui;

import javax.swing.*;

import cbg.common.UIConsts;

import java.awt.*;
/**
 * @author Stephen Chudleigh
 */
public class EssencePersonalityPanelDemo  extends JPanel
									implements UIConsts {

	private EssencePersonalityPanel epPanel;
	private CardHandPanel chPanel;

	/**
	 * Constructor FoodDiagramPanelDemo.
	 * @param frame
	 */
	public EssencePersonalityPanelDemo() {
	}
	
	public static void main(String[] args) {
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

		EssencePersonalityPanelDemo epDemo = new EssencePersonalityPanelDemo();
	
		//Create and set up the content pane.
		Container contentPane = frame.getContentPane();
		
		Component epDiag = epDemo.createDiagram();
		Component ch = epDemo.createCardHandPane(); // create this before buttons!
		Component btns = epDemo.createButtonPane();
        
        contentPane.add(btns, BorderLayout.PAGE_START);
		contentPane.add(ch, BorderLayout.CENTER);
		contentPane.add(epDiag, BorderLayout.PAGE_END);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}

	private Component createCardHandPane() {
		chPanel = new CardHandPanel();
		JScrollPane scrollPane = new JScrollPane(
				chPanel,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setMinimumSize(new Dimension(chPanel.getWidth(), chPanel.getHeight()+20));
		return scrollPane;
	}

	private Component createDiagram() {
		epPanel = new EssencePersonalityPanel();
		//epPanel.updateView();
		return epPanel;
	}
	
	private JComponent createButtonPane() {
		JButton drawButton = new JButton("Draw A Card");
		JButton playButton = new JButton("Play Selected Cards");
		JButton mkAceButton = new JButton("Make Ace");
		JButton mkXJButton = new JButton("Make Extra Joker");
		JButton mkJOButton = new JButton("Make Joker");
		/* chPanel isn't an ActionListener anymore.
		drawButton.setActionCommand(DRAWCARD_COMMAND);
		drawButton.addActionListener(chPanel);
		playButton.setActionCommand(PLAYCARD_COMMAND);
		playButton.addActionListener(chPanel);
		*/
		mkAceButton.setActionCommand(MAKE_ACE_COMMAND);
		//mkAceButton.addActionListener(epPanel);
		mkXJButton.setActionCommand(MAKE_XJ_COMMAND);
		//mkXJButton.addActionListener(epPanel);
		mkJOButton.setActionCommand(MAKE_JO_COMMAND);
		//mkJOButton.addActionListener(epPanel);
		
		//Center the button in a panel with some space around it.
	  	JPanel buttonPanel = new JPanel();

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        buttonPanel.add(drawButton);
        buttonPanel.add(playButton);
		buttonPanel.add(mkAceButton);
		buttonPanel.add(mkXJButton);
		buttonPanel.add(mkJOButton);
        return buttonPanel;
	}
}
