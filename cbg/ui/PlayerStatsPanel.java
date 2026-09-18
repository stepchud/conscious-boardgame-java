package cbg.ui;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import javax.swing.*;

import cbg.boardParts.LawCard;
import cbg.common.UIConsts;
import cbg.player.LevelOfBeing;

public class PlayerStatsPanel extends JPanel implements java.beans.PropertyChangeListener, UIConsts {

	private static final long serialVersionUID = -1774620791714208348L;
	private JLabel nameAgeLabel, ageLbl, activeLawsLbl, cardPlaysLabel, lobLabel;

	public PlayerStatsPanel(String name) {
		createGUIComponents(name);
	}
	
	private void createGUIComponents(String name) {
		nameAgeLabel = new JLabel("Player: "+name+", Age: ");
		ageLbl = new JLabel(String.valueOf(0));
		JLabel cplLabel = new JLabel("Card plays left: ");
		cardPlaysLabel = new JLabel("2");
		JLabel levelLabel = new JLabel("Level of Being: ");
		lobLabel = new JLabel(LevelOfBeing.MULTIPLICITY);
		JLabel activeLawsLabel = new JLabel("Active Laws: ");
		activeLawsLbl = new JLabel("");
		
		JPanel panel0 = new JPanel();
		panel0.add(nameAgeLabel);
		panel0.add(ageLbl);
		JPanel panel1 = new JPanel();
		panel1.add(cplLabel);
		panel1.add(cardPlaysLabel);
		JPanel panel2 = new JPanel();
		panel2.add(levelLabel);
		panel2.add(lobLabel);
		JPanel panel3 = new JPanel();
		panel3.add(activeLawsLabel);
		panel3.add(activeLawsLbl);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(panel0);
		this.add(panel1);
		this.add(panel2);
		this.add(panel3);
	}
	public void setPlayerName(String name) {
	    nameAgeLabel.setText("Player: "+name+", Age: ");
	}
	public void setAge(int age) {
		ageLbl.setText(String.valueOf(age));
	}
	public void updateActiveLawsLabel(ArrayList laws) {
		if (laws.size()>0) {
			StringBuffer lb = new StringBuffer("<html>"+((LawCard)laws.get(0)).getCard().toString());
			for (int i=1; i<laws.size(); i++) {
				lb.append(",<br>").append(((LawCard)laws.get(i)).getCard().toString());
			}
			lb.append("</html>");
			activeLawsLbl.setText(lb.toString());
		} else {
			activeLawsLbl.setText("");
		}
	}
	public void updateLob(String newLob) {
		lobLabel.setText(newLob);
	}

	public void propertyChange(PropertyChangeEvent arg0) {
	    if (arg0.getPropertyName().equals("CardPlaysLeft")) {
	        Object newVal = arg0.getNewValue();
	        cardPlaysLabel.setText(newVal.toString());
	    }
	}
}
