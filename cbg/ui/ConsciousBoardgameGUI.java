/*
 * Created on Feb 13, 2005
 * Author: Stephen Chudleigh
 */
package cbg.ui;

import java.util.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import cbg.boardParts.*;
import cbg.common.UIConsts;
import cbg.player.*;

/**
 * @author Stephen Chudleigh
 * ConsciousBoardgame
 * Feb 13, 2005
 */
public class ConsciousBoardgameGUI extends JFrame
	implements ActionListener, java.beans.PropertyChangeListener, UIConsts {

    private static boolean testHasnamuss=false, firstGame=true;
    private static final long serialVersionUID = 5184830501394979215L;
	private static ConsciousBoardgameGUI gui = null;
	private Player currentPlayer;
    private String playerName = null;
	
	int res;//store screen resolution here
	//default scale, 72 units/inch
	//static final int ds = 70;
	
	//horizonal size inches
	static final int hSize = 9;
	//vertical size inches
	static final int vSize = 7;
	
	//offset below top and left of frame
	static final int TOP_MAIN_FRAME = 30;  
	static final int LEFT_MAIN_FRAME = 7;
	
	private AnimatedBoardPanel boardPanel;
	private EssencePersonalityPanel epPanel;
	private CardHandPanel chPanel;
	private FoodDiagramPanel fdPanel;
	private LawsInPlayPanel lipPanel;
	private JPanel buttonPanel = null;
	private PlayerStatsPanel statsPanel;
	private JLabel diceLabel;
	private JButton rollDiceBtn = new JButton("Roll Dice"), playButton, mkAceButton, mkXJButton, mkJOButton;
	private boolean isLawDlgShowing = false;
	
	private ConsciousBoardgameGUI(String title){
		super(title);
	}
	
	public static ConsciousBoardgameGUI getInstance() {
	    if (gui == null) {
	        gui = new ConsciousBoardgameGUI("The Conscious Boardgame v1.5");
	    }
	    return gui;
	}
 
 	public static void main(String [] args) {
        try {
            testHasnamuss = Boolean.valueOf(args[0]).booleanValue();
        } catch (Exception e) {
            System.out.println("To run a hasnamuss game provide a single run argument: \"true\".");
        }
 		IconFactory.createImageIcons();
        //long start = System.currentTimeMillis();
        try {
            initBoardParts();
             while (!Decks.isLDInit()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("it took "+(System.currentTimeMillis()-start)+" millis to init deck.");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                System.out.println("finished createAndShowGUI");
            }
        });
	}

	/**
     * 
     */
    static void initBoardParts() {
        Dice.init();
		Board.init();
		Decks.init();
    }

    /**
	 * Method createAndShowGUI.
	 */
	static void createAndShowGUI() {
		//Get screen resolution
		//int res = Toolkit.getDefaultToolkit().getScreenResolution();
		//Set Frame size
        System.out.println("res in pixels per inch="+Toolkit.getDefaultToolkit().getScreenResolution() );
        System.out.println("screen size="+Toolkit.getDefaultToolkit().getScreenSize().width
                +"X"+Toolkit.getDefaultToolkit().getScreenSize().height);

        getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getInstance().setCurrentPlayer(new Player());
		//setDefaultLookAndFeelDecorated(true);
        //getInstance().setIconImage( Toolkit.getDefaultToolkit().getImage("/img/cardback.gif"));

        Component abPanel = getInstance().createABPanel();
		Component foodDiag = getInstance().createFoodDiagramPane();
		Component epDiag = getInstance().createEPDiagram(); // create this before card hand
		Component ch = getInstance().createCardHandPane(); // create this before buttons
		Component btns = getInstance().createButtonPane();
		//epDiag.setSize(new Dimension(360,227));
		//foodDiag.setSize(new Dimension(286,264));
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		/*mainPanel.setBorder(
			BorderFactory.createEmptyBorder(
				SPACE_WIDTH+8,	//top
				0,
				10,		// bottom
				0
			)
		);*/
		
        //Dimension prefSize = new Dimension(hSize*res,vSize*res);
		//System.out.println("preffered size="+prefSize);
		//mainPanel.setPreferredSize(prefSize);
		//mainPanel.setMaximumSize(prefSize);
		//mainPanel.setBackground(Color.WHITE);
		mainPanel.add(btns, BorderLayout.PAGE_START);
		mainPanel.add(epDiag, BorderLayout.CENTER);
		mainPanel.add(foodDiag, BorderLayout.LINE_END);
		mainPanel.add(ch, BorderLayout.PAGE_END);
        System.out.println("Preferred sizes: mainPanel="+mainPanel.getPreferredSize()
                +";btns="+btns.getPreferredSize()
                +";epDiag="+epDiag.getPreferredSize()
                +";foodDiag="+foodDiag.getPreferredSize()
                +";ch="+ch.getPreferredSize());
        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(Box.createVerticalGlue());
        box.add(abPanel);
        //box.add(Box.createVerticalGlue());
        box.add(mainPanel);
        //box.setPreferredSize(mainPanel.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(box);
        scrollPane.setLocation(LEFT_MAIN_FRAME, TOP_MAIN_FRAME);      
        //scrollPane.setPreferredSize(box.getPreferredSize());
        scrollPane.setMinimumSize(box.getPreferredSize());
		Insets inset = getInstance().getInsets();
        Dimension prefSize = scrollPane.getPreferredSize();
        scrollPane.setBounds(inset.left,
                inset.top,
                prefSize.width,
                prefSize.height);
        getInstance().getContentPane().setPreferredSize(scrollPane.getPreferredSize());
        System.out.println("Preferred sizes: box="+box.getPreferredSize()
                +";scrollPane="+scrollPane.getPreferredSize()
                +";CBGUI.getContentPane()="+getInstance().getContentPane().getPreferredSize());
        getInstance().getContentPane().setLayout(new BorderLayout());
        getInstance().getContentPane().add(scrollPane, BorderLayout.CENTER);
        //getInstance().setSize(inset.left+inset.right+prefSize.width+10,
					//inset.top+inset.bottom+prefSize.height+20);
		//Display the window.
        getInstance().pack();
        getInstance().setVisible(true);
        box.revalidate();

        if (firstGame) getInstance().playerName = JOptionPane.showInputDialog(getInstance(), "Hi, What is your name?");
        getInstance().startGame();
	}

	/**
     * 
     */
    private void startGame() {
        if (playerName!=null && playerName.length()>0) {
            currentPlayer.setName(playerName);
            statsPanel.setPlayerName(playerName);
        }
        currentPlayer.initGame();
        String msg = "You start off with 2 law plays and 2 card plays this turn."
            +"\nYou are type "+currentPlayer.getType()
            +"\nGood Luck!";
        String title = "Welcome";
        if (firstGame) {
            msg = "Welcome to The Conscious Video Game, "+currentPlayer.getName()+"\n"+msg;
            firstGame=false;
        } else {
            title = "New Game";
        }
        JOptionPane.showMessageDialog(this, 
                msg,
                title,
                JOptionPane.PLAIN_MESSAGE);
        showLawsDialog(false);
    }

    /**
	 * Method showLawsDialog.
	 */
	public void showLawsDialog(boolean choiceOnly) {
		final PickLawsDialog pickLawsDlg = new PickLawsDialog(this);
		if (choiceOnly) pickLawsDlg.createChoiceDialog();
		else pickLawsDlg.createAndShowDialog();
		final JDialog dialog = new JDialog(this,
					"Pick Law Card Panel",
					false);
		dialog.setContentPane(pickLawsDlg.getContentPane());
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				JOptionPane.showMessageDialog(getInstance(),
							"You must pick your laws for this turn.",
							"Law Selection",
							JOptionPane.INFORMATION_MESSAGE);
			}
		});
		pickLawsDlg.addPropertyChangeListener(
			new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();
				if (dialog.isVisible()
					&& (e.getSource() == pickLawsDlg)
					&& (prop.equals(PickLawsDialog.FINISHED))
					&& (((Boolean)e.getNewValue()).booleanValue())) {
						dialog.setVisible(false);
						isLawDlgShowing = false;
						if (currentPlayer.getLawsInPlay().size()>0) {
							getInstance().showLawsInPlayPanel();
						}
				}
			}
		});
		
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
		isLawDlgShowing = true;
	}

	/**
	 * Method createAndShowLawsInPlay.
	 */
	private void showLawsInPlayPanel() {
		lipPanel.updateView(currentPlayer);
	    lipPanel.setVisible(true);
	}

	/**
	 * Method addLawToPlay.
	 */
	public void addLawToPlay(LawCard lc) {
		currentPlayer.addLawToPlay(lc);
		lipPanel.updateView(currentPlayer);
	}



	/**
	 * Method createFoodDiagramPane.
	 * @return Component
	 */
	private Component createFoodDiagramPane() {
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalGlue());
		fdPanel = new FoodDiagramPanel();
		statsPanel = new PlayerStatsPanel(currentPlayer.getName());
		box.add(statsPanel);
		box.add(fdPanel);
		currentPlayer.addPropertyChangeListener(statsPanel);
		currentPlayer.getFd().addPropertyChangeListener(fdPanel);
        
        box.setPreferredSize(new Dimension(FoodDiagramPanel.Width,FoodDiagramPanel.Height+statsPanel.getHeight()));
        box.setMaximumSize(box.getPreferredSize());
		return box;
	}

	/**
	 * Method createButtonPane.
	 * @return Component
	 */
	private Component createButtonPane() {
		// Buttons
		buttonPanel = new JPanel();
		playButton = new JButton("Play Selected Cards");
		mkAceButton = new JButton("Make Ace");
		mkXJButton = new JButton("Make Extra Joker");
		mkJOButton = new JButton("Make Joker");

		// Dice
		diceLabel = new JLabel("0");
		
		//	button size
		//int BUTTON_HEIGHT = 25;
		//int BUTTON_WIDTH=70;
		//rollDiceBtn.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		rollDiceBtn.setActionCommand(ROLL_DICE_COMMAND);
		rollDiceBtn.addActionListener(this);
		playButton.setActionCommand(PLAYCARD_COMMAND);
		playButton.addActionListener(this);
		mkAceButton.setActionCommand(MAKE_ACE_COMMAND);
		mkAceButton.addActionListener(this);
		mkAceButton.setVisible(false);
		mkXJButton.setActionCommand(MAKE_XJ_COMMAND);
		mkXJButton.addActionListener(this);
		mkXJButton.setVisible(false);
		mkJOButton.setActionCommand(MAKE_JO_COMMAND);
		mkJOButton.addActionListener(this);
		mkJOButton.setVisible(false);

		diceLabel.setIcon(IconFactory.diceIcon);
		//diceLabel.setOpaque(true);
		diceLabel.setForeground(Color.WHITE);
		diceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		diceLabel.setHorizontalTextPosition(JLabel.CENTER);
		diceLabel.setVerticalTextPosition(JLabel.CENTER);
		diceLabel.setText(""+Dice.getRoll());
		diceLabel.setVisible(true);
		
		//buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
		buttonPanel.add(rollDiceBtn);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		buttonPanel.add(diceLabel);
        buttonPanel.add(playButton);
		buttonPanel.add(mkAceButton);
		buttonPanel.add(mkXJButton);
		buttonPanel.add(mkJOButton);
		currentPlayer.getEp().addPropertyChangeListener(this);
        
		buttonPanel.setPreferredSize(new Dimension(SPACE_WIDTH*40, 30));
		return buttonPanel;
	}

	public Component getButtonPanel() {
		return buttonPanel;
	}

	/**
	 * Method createCardHandPane.
	 * @return Component
	 */
	private Component createCardHandPane() {
		JPanel aPanel = new JPanel(new BorderLayout());
		chPanel = new CardHandPanel();
		chPanel.setLayout(new GridLayout(0,9,5,0));
		JScrollPane scrollPane = new JScrollPane(
				chPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		aPanel.setPreferredSize(new Dimension(IconFactory.CARD_WIDTH*10, 163));
		aPanel.add(scrollPane, BorderLayout.CENTER);
		currentPlayer.addPropertyChangeListener(chPanel);
		chPanel.updateHandView(currentPlayer.getPocHand());
		return aPanel;
	}
	
	private Component createABPanel() {
		boardPanel = new AnimatedBoardPanel();
        boardPanel.setPreferredSize(new Dimension(SPACE_WIDTH*40,SPACE_WIDTH));
		return boardPanel;
	}

	/**
	 * Method createDiagram.
	 * @return Component
	 */
	private Component createEPDiagram() {
		Box box = new Box(BoxLayout.Y_AXIS);
		epPanel = new EssencePersonalityPanel();
		epPanel.updateView(currentPlayer.getEp());
		lipPanel = new LawsInPlayPanel();
		lipPanel.setVisible(false);
        lipPanel.setPreferredSize(new Dimension(444,100));
		box.add(epPanel);
		box.add(lipPanel);
        box.setPreferredSize(new Dimension(
                6*IconFactory.CARD_WIDTH+18,
                3*(IconFactory.CARD_HEIGHT)
                 + 2*IconFactory.chipIcons[2].getIconHeight()
                 + IconFactory.chipIcons[4].getIconHeight()
                 + lipPanel.getPreferredSize().height));
        box.setMaximumSize(box.getPreferredSize());
		return box;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (ROLL_DICE_COMMAND.equals(cmd)) {
		    rollDiceCommand();
		} else if (PLAYCARD_COMMAND.equals(cmd)) {
		    if (currentPlayer.isAsleep()) {
		        JOptionPane.showMessageDialog(this,
						"You are asleep.\n"+
						"You can not play cards while sleeping.\n",
						"Wake up!",
						JOptionPane.ERROR_MESSAGE);
		        lipPanel.deselectAllButtons();
		        chPanel.deselectAllButtons();
		        return;
		    }
			short [] shocks = {0,0,0,0};
			if (lipPanel.isVisible() && lipPanel.getSelectedLawCards(currentPlayer).size()>0)
				shocks = lipPanel.playSelectedCards(currentPlayer, chPanel);
        	else
        		shocks = chPanel.playSelectedCards(currentPlayer);
        	currentPlayer.getFd().performShocks(shocks);
		} else if (MAKE_ACE_COMMAND.equals(cmd)) {
		    if (currentPlayer.isAsleep()) {
		        JOptionPane.showMessageDialog(this,
						"You are asleep.\n"+
						"You can not transform emotions while sleeping.\n",
						"Wake up!",
						JOptionPane.ERROR_MESSAGE);
		        lipPanel.deselectAllButtons();
		        chPanel.deselectAllButtons();
		        return;
		    }
            epPanel.action_CreateAce(currentPlayer);
        } else if (MAKE_XJ_COMMAND.equals(cmd)) {
            if (currentPlayer.isAsleep()) {
		        JOptionPane.showMessageDialog(this,
						"You are asleep.\n"+
						"You can not make Extra-Jokers while sleeping.\n",
						"Wake up!",
						JOptionPane.ERROR_MESSAGE);
		        lipPanel.deselectAllButtons();
		        chPanel.deselectAllButtons();
		        return;
		    }
           	epPanel.action_CreateXJ(currentPlayer);
        }  else if (MAKE_JO_COMMAND.equals(cmd)) {
            if (currentPlayer.isAsleep()) {
		        JOptionPane.showMessageDialog(this,
						"You are asleep.\n"+
						"You can not make Jokers while sleeping.\n",
						"Wake up!",
						JOptionPane.ERROR_MESSAGE);
		        lipPanel.deselectAllButtons();
		        chPanel.deselectAllButtons();
		        return;
		    }
           	epPanel.action_CreateJO(currentPlayer);
        } else if (OBEY_LAW_COMMAND.equals(cmd)) {
            obeyLaw();
        } else if (DEATH_COMMAND.equals(cmd)) {
        	instantDeath(currentPlayer);
		} else if (KEEP_SELECTED_COMMAND.equals(cmd)) {
			if (chPanel.getSelectedCards(currentPlayer).size()>7) {
				JOptionPane.showMessageDialog(this,
						"You may only keep up to 7 cards from your hand.\n"+
						"Choose the cards you want to keep and press the\n"+
						"\'Keep Selected Cards\' button to accept your choice.",
						"Pick 7",
						JOptionPane.ERROR_MESSAGE);
			} else {
				chPanel.keepSelectedCards(currentPlayer);
				playButton.setText("Play Selected Cards");
				playButton.setActionCommand(PLAYCARD_COMMAND);
				rollDiceBtn.setEnabled(true);
				rollDiceBtn.doClick();
			}
		}
		if (currentPlayer.getEp().isComplete(currentPlayer.isHasnamuss())) {
            if (currentPlayer.isHasnamuss() && currentPlayer.getEp().cleansedHasnamuss()) {
                CBGDlgFactory.displayInformationMessage( "Redemption",
                        "Congratulations!\n" +
                        "You have cleansed yourself of being a Hasnamuss.\n" +
                        "You give up a Joker piece in exchange for your soul.");
                currentPlayer.cleanseHasnamuss();
                currentPlayer.getEp().removeJoker();
            }
            currentPlayer.getEp().setComplete(true);
			currentPlayer.getFd().checkComplete();
		}
		updateView();
	}

	public static void instantDeath(Player plyr) {
        if (plyr.survivesDeath()) {
            getInstance().rollDiceBtn.setText("Roll Dice");
            getInstance().rollDiceBtn.setActionCommand(ROLL_DICE_COMMAND);
            plyr.startDeathGame();
            getInstance().rollDiceBtn.doClick();
        } else {
            CBGDlgFactory.endGameMessage("\"Blessed is he who has a soul,\n"+
                    "Blessed is he who has none,\n"+
                    "But woah and grief to him who has it in embryo.\"\n"+
                    "Although you did not aquire a soul during this life,\n"+
                    "that's no reason you can't just start another game!\n"+
                    "Play Again?");
            
        }
    }

    /**
     * 
     */
    private void obeyLaw() {
        if (testHasnamuss) {
            // give a bunch of stuff to make the game a little shorter
            for (int i=0; i<15; i++) currentPlayer.drawPOCCard(true);
            currentPlayer.getEp().createTwoPieces(Card.AD);
            currentPlayer.getEp().createTwoPieces(Card.AC);
            currentPlayer.getEp().createTwoPieces(Card.AH);
            currentPlayer.getEp().createTwoPieces(Card.AS);
            currentPlayer.getEp().createTwoPieces(Card.XJ);
            currentPlayer.getEp().createTwoPieces(Card.QH);
            currentPlayer.getEp().createTwoPieces(Card.JH);
            currentPlayer.getEp().createTwoPieces(Card.QS);
            currentPlayer.getEp().createTwoPieces(Card.JS);
            currentPlayer.getEp().createTwoPieces(Card.QD);
            currentPlayer.getEp().createTwoPieces(Card.KD);
            LawEnforcer.enforceLawOnPlayer(Decks.test_DrawJokerLaw(), getInstance().getCurrentPlayer(), true);
            currentPlayer.getEp().createPiece(Card.JO);
            short [] f = new short [] {0,0,0,0,0,0,0,9};
            short [] a = new short [] {0,0,0,1,0,6};
            short [] i = new short [] {0,0,0,3};
            currentPlayer.getFd().enterChips(f, a, i);
            f = new short [] {0,0,0,0,0,0,0,6};
            a = new short [] {0,0,0,1,0,5};
            i = new short [] {0,0,0,3};
            currentPlayer.getFd().enterChips(f, a, i);
        }
        ArrayList selLaw = lipPanel.getSelectedLawCards(currentPlayer);
		if (selLaw.size() != 1) {
			JOptionPane.showMessageDialog(this,
    				"You can only obey one law card at a time.",
    				"Grace",
    				JOptionPane.INFORMATION_MESSAGE);
			lipPanel.deselectAllButtons();
    		return;
		} else {
			LawCard lc = (LawCard)selLaw.remove(0);
			if (lc.isObeyed()) {
				JOptionPane.showMessageDialog(this,
    				"You already obeyed the "+lc.getCard()+" law card.",
    				"Double Jeopardy",
    				JOptionPane.INFORMATION_MESSAGE);
				lipPanel.deselectAllButtons();
				return;
			}
			LawEnforcer.enforceLawOnPlayer(lc,currentPlayer, false);
			lipPanel.deselectAllButtons();
		}
    }

    /**
     * 
     */
    private void rollDiceCommand() {
        if (testHasnamuss) {
            currentPlayer.dies();
            testHasnamuss=false;
        }
		if (isLawDlgShowing) {
			JOptionPane.showMessageDialog(this,
    				"There's no escape from the long arm of The Law.\n"+
    				"You must choose and obey law cards before proceeding.",
    				"There's no escaping The Law",
    				JOptionPane.ERROR_MESSAGE);
    		return;
		}
    	if (!currentPlayer.hasObeyedLaws()) {
    		JOptionPane.showMessageDialog(this,
    				"You must obey all law cards in play before proceeding.",
    				"No escaping the law",
    				JOptionPane.ERROR_MESSAGE);
    		return;
    	} else if (lipPanel.isVisible())
    		lipPanel.setVisible(false);
    	//System.out.println("You rolled "+Dice.roll());
        int roll = Dice.roll()*currentPlayer.getRollMultiple();
    	if (currentPlayer.getBoardPos()+roll > (Board.getNumSpaces()-1)) {
    		roll = Board.getNumSpaces()-1-currentPlayer.getBoardPos();
    	}
		roll = CBGDlgFactory.showDiceOptionDialog(currentPlayer, roll);
		diceLabel.setText(String.valueOf(Dice.getRoll()));
		enableRoll(false);
        if ((currentPlayer.getBoardPos()+roll) > (Board.getNumSpaces()-1))
            roll = (Board.getNumSpaces()-1)-currentPlayer.getBoardPos();
        System.out.println("moving "+roll+" spaces");
		boardPanel.restart(roll, currentPlayer.getBoardPos());
		currentPlayer.move(roll);
		statsPanel.setAge(currentPlayer.getAge());
        enablePowerButtons();
		if (currentPlayer.getLawPlaysThis() > 0) {
			showLawsDialog(false);
		}
    }

    void enableRoll(boolean b) {
		rollDiceBtn.setEnabled(b);
	}
    
    public void keepSelectedCardsConfig() {
        playButton.setText("Keep Selected Cards (roll after death)");
        playButton.setActionCommand(KEEP_SELECTED_COMMAND);
        // temporarily disable other buttons to force choice
        disablePowerButtons();
        rollDiceBtn.setEnabled(false);
        JOptionPane.showMessageDialog(this,
            "Before you enter the 'afterlife', you may\n"+
            "keep up to 7 cards from your hand.\n"+
            "Choose the cards you want to keep and press the\n"+
            "'Keep Selected Cards' button to accept your choice.",
            "Pick 7",
            JOptionPane.INFORMATION_MESSAGE);
    }

	/**
	 * Method enablePowerButtons.
	 */
	private void enablePowerButtons() {
		if (currentPlayer.getBoardPos()>currentPlayer.getNoPowersTil()) {
			if (currentPlayer.getEp().foundSchool())
				getMkAceButton().setEnabled(true);
				
			if (currentPlayer.getEp().hasAprilFools())
				getMkXJButton().setEnabled(true);
				
			if (currentPlayer.getEp().has1001Words())
				getMkJOButton().setEnabled(true);
		}
	}

	/**
	 * Method enablePowerButtons.
	 */
	public void disablePowerButtons() {
		getMkAceButton().setEnabled(false);
		getMkXJButton().setEnabled(false);
		getMkJOButton().setEnabled(false);
	}
	
	/**
	 * Method lastTurn.
	 */
	public void lastTurn() {
		rollDiceBtn.setText("End Turn (Roll)");
		rollDiceBtn.setActionCommand(DEATH_COMMAND);
	}
	
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("LevelofBeing")) {
			LevelOfBeing lob = (LevelOfBeing)evt.getNewValue();
			if (lob.getLevel().equals(LevelOfBeing.STUDENT)) {
				mkAceButton.setVisible(true);
				JOptionPane.showMessageDialog(this,
					"You found a school.\n"+
					"You can now play two cards per turn.\n"+
					"New Skill: You can breathe when you eat once per turn.\n"+
					"New Power: You can transform emotions by combining two\n"+
					"different face cards of the same suit, creating an ace\n"+
					"of that suit, once per turn.\n" +
                    "New Aim: Put a piece on all four Aces for Steward.",
					"Found School!",
					JOptionPane.PLAIN_MESSAGE
				);
				//epPanel.setLobLabel(currentPlayer.getEp().getLob().toString());
			} else if (lob.getLevel().equals(LevelOfBeing.STEWARD)) {
				mkXJButton.setVisible(true);
				JOptionPane.showMessageDialog(this,
					"April Fools!\n"+
					"You can now play three cards per turn.\n"+
					"New Dice Option: You can re-roll once\n"+
					"if you don't like your roll.\n"+
					"New Skill: You can eat when you breathe once per turn.\n"+
					"New Power: You can make Extra Jokers by combining two\n"+
					"different aces of hearts, clubs, or diamonds.\n" +
                    "New Aim: Put a piece on the Joker by bumping through\n" +
                    "the Extra-Joker to become a Master.",
					"April Fools!",
					JOptionPane.PLAIN_MESSAGE
				);
				//epPanel.setLobLabel(currentPlayer.getEp().getLob().toString());
			} else if (lob.getLevel().equals(LevelOfBeing.MASTER)) {
				mkJOButton.setVisible(true);
				JOptionPane.showMessageDialog(this,
					"You 'Mastered' 1001 words flawlessly.\n"+
					"You can now play four cards per turn.\n"+
					"New Dice Option: You can take the opposite\n"+
					"side of the "+Dice.getNumSides()+"-sided die as your roll.\n"+
					"New Skill: You can 'carbon-12' a new impression 48 once per turn.\n"+
					"New Power: You can create Jokers by combining\n"+
					"an Extra Joker with an Ace of Spades.\n" +
                    "New Aim: Put three pieces on the Joker and\n" +
                    "complete your Mental body to Start Over!",
					"Mastery!",
					JOptionPane.PLAIN_MESSAGE
				);
				//epPanel.setLobLabel(currentPlayer.getEp().getLob().toString());
			}
			// Hack to give an extra card play this turn.
			currentPlayer.giveMCMoment();
			statsPanel.updateLob(lob.getLevel());
		} else if (evt.getPropertyName().equals("EPComplete")) {
			if (((Boolean)evt.getNewValue()).booleanValue()) {
				currentPlayer.getFd().checkComplete();
				if (currentPlayer.getFd().isComplete()) {
					CBGDlgFactory.showGameCompleted();
				} else {
					currentPlayer.getFd().addPropertyChangeListener(this);
				}
			}
		} else if (evt.getPropertyName().equals("FoodDiagComplete")) {
		    if (((Boolean)evt.getNewValue()).booleanValue()) {
				CBGDlgFactory.showGameCompleted();
		    }
		}
	}

	/**
	 * Returns the mkAceButton.
	 * @return JButton
	 */
	public JButton getMkAceButton() {
		return mkAceButton;
	}

	/**
	 * Returns the mkJOButton.
	 * @return JButton
	 */
	public JButton getMkJOButton() {
		return mkJOButton;
	}

	/**
	 * Returns the mkXJButton.
	 * @return JButton
	 */
	public JButton getMkXJButton() {
		return mkXJButton;
	}
	
	/**
	 * Method updateView.
	 */
	private void updateView() {
		statsPanel.updateActiveLawsLabel(currentPlayer.getActiveLaws());
		epPanel.updateView(currentPlayer.getEp());
		chPanel.updateHandView(currentPlayer.getPocHand());
		boardPanel.repaint();
		this.update(this.getGraphics());
	}
    /**
     * @return Returns the currentPlayer.
     */
    public Player getCurrentPlayer() {
        return gui.currentPlayer;
    }
    /**
     * @param currentPlayer The currentPlayer to set.
     */
    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void restart() {
        this.removeAll();
        this.dispose();
        gui = null;
        initBoardParts();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                System.out.println("finished createAndShowGUI");
            }
        });
    }
}
