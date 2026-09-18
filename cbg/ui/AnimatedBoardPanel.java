package cbg.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import cbg.boardParts.Board;
import cbg.common.UIConsts;

public class AnimatedBoardPanel extends JPanel implements Runnable, UIConsts {

	private Thread runner;
	// lower values for higher speed (value is pause between each move in milliseconds)
    public static final int Speed = 200;    
	int currPlyrPosition = 0;
	int firstBoardSpace = 0;
	int roll = 0;
    
	public AnimatedBoardPanel() {
		super();
	}
	private void setFirstBoardSpace(int p) {
		firstBoardSpace = p;
	}
	private void setRoll(int r) {
		roll = r;
	}
	public void restart(int currRoll, int startOn) {
		if (runner != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		setRoll(currRoll);
		setFirstBoardSpace(startOn);
		runner = new Thread(this);
		runner.start();
	}

	public void run() {
		Thread thisThread = Thread.currentThread();
		while (runner == thisThread) {
			while (currPlyrPosition<roll) {
				currPlyrPosition++;
				repaint();
				try {
					Thread.sleep(Speed);
				} catch (InterruptedException e) { }
			}
			while (currPlyrPosition>0) {
				firstBoardSpace++;
				currPlyrPosition--;
				repaint();
				try {
					Thread.sleep(Speed);
				} catch (InterruptedException e) { }
			}
			runner = null;
		}
		// This should be called here inside thread execution
        ConsciousBoardgameGUI.getInstance().enableRoll(true);
	}

	//Override the paint() method
	public void paint(Graphics g){
		super.paint(g);
		//Downcast the Graphics object to a
		// Graphics2D object
		Graphics2D g2 = (Graphics2D)g;

		//Draw Board Spaces
		drawBoardSpaces(g2);
		//drawPlayer(this.getPlyrScrPos(currentPlayer.getBoardPos()), g2);
		drawPlayer(new Point(currPlyrPosition*SPACE_WIDTH, 0), g2);
	}

	/**
	 * @param g2
	 */
	private void drawBoardSpaces(Graphics2D g2) {
		Color prevColor = g2.getColor();
		int spaceNum = firstBoardSpace;
		int lastBoardSpace = 40;
		if (lastBoardSpace > (Board.getNumSpaces()-firstBoardSpace))
				lastBoardSpace = (Board.getNumSpaces()-firstBoardSpace);

		for (int i=0; i<lastBoardSpace; i++) {
			switch (Board.getSpace(i+firstBoardSpace).toString().charAt(1)) {
			case 'F':
				drawFoodSpace(new Point(SPACE_WIDTH*i,0), g2);
				break;
			case 'A':
				drawAirSpace(new Point(SPACE_WIDTH*i,0), g2);
				break;
			case 'I':
				drawImpSpace(new Point(SPACE_WIDTH*i,0), g2);
				break;
			case 'L':
				drawLawCardSpace(new Point(SPACE_WIDTH*i,0), g2);
				break;
			case 'C':
				drawCardSpace(new Point(SPACE_WIDTH*i,0), g2);
				break;
			case 'W':
				drawWildSpace(new Point(SPACE_WIDTH*i, 0), g2);
				break;
			case 'D':
				drawCardSpace(new Point(SPACE_WIDTH*i, 0), g2);
				break;
			default:
				break;
			
			}			
			spaceNum++;
		}
		g2.setColor(prevColor);
	}
	/**
	 * @param point
	 * @param g2
	 */
	private void drawWildSpace(Point p, Graphics2D g2) {
		g2.setColor(Color.lightGray);
		g2.fill(new Ellipse2D.Double(p.getX(), p.getY(), SPACE_WIDTH, SPACE_WIDTH));
	}

	private void drawFoodSpace(Point p, Graphics2D g2) {
		g2.setColor(Color.yellow);
		g2.fill(new Ellipse2D.Double(p.getX(), p.getY(), SPACE_WIDTH, SPACE_WIDTH));
	}
	private void drawAirSpace(Point p, Graphics2D g2) {
		g2.setColor(Color.blue);
		g2.fill(new Ellipse2D.Double(p.getX(), p.getY(), SPACE_WIDTH, SPACE_WIDTH));
	}
	private void drawImpSpace(Point p, Graphics2D g2) {
		g2.setColor(Color.red);
		g2.fill(new Ellipse2D.Double(p.getX(), p.getY(), SPACE_WIDTH, SPACE_WIDTH));
	}
	private void drawCardSpace(Point p, Graphics2D g2) {
		//g2.setColor(Color.getHSBColor(0.3f, 0.4f, 0.2f));
		g2.setColor(Color.orange);
		g2.fill(new Ellipse2D.Double(p.getX(), p.getY(), SPACE_WIDTH, SPACE_WIDTH));
		g2.setColor(Color.black);
		g2.fill(new Ellipse2D.Double(p.getX()+SPACE_WIDTH/4, p.getY()+SPACE_WIDTH/4, SPACE_WIDTH/2, SPACE_WIDTH/2));
	}
	private void drawLawCardSpace(Point p, Graphics2D g2) {
		g2.setColor(Color.green);
		g2.fill(new Ellipse2D.Double(p.getX(), p.getY(), SPACE_WIDTH, SPACE_WIDTH));
		g2.setColor(Color.red);
		g2.fill(new Ellipse2D.Double(p.getX()+SPACE_WIDTH/4, p.getY()+SPACE_WIDTH/4, SPACE_WIDTH/2, SPACE_WIDTH/2));
	}
	private void drawPlayer(Point p, Graphics2D g2) {
		g2.setColor(Color.black);
		g2.fillRect((int)p.getX(),((int)p.getY()+SPACE_WIDTH/2)-1,SPACE_WIDTH,3);
		g2.fillRect(((int)p.getX()+SPACE_WIDTH/2)-1,(int)p.getY(),3,SPACE_WIDTH);
	}	
}
