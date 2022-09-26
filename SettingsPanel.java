import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/* Settings menu that is not complete */
public class SettingsPanel extends JPanel implements ActionListener {
	JComboBox<String> gridCombo = new JComboBox<>();
	JComboBox<String> speed     = new JComboBox<String>();
	JButton save = new JButton("Save"), cancel = new JButton("Cancel");
	
	int[] gridSizes = {10, 20, 30};
	
	SettingsPanel() {
		save.addActionListener(this);
		cancel.addActionListener(this);
		
		for (String str: new String[] {"Slow", "Medium", "Fast" }) {
			speed.addItem(str);
		}
		
		for (int i : gridSizes) {
			gridCombo.addItem(Integer.toString(i) + "*" + Integer.toString(i));
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == save) {
			// Change the variables in Prefs class
			
		} else {
			// Set everything back to initial values
		}
		
		// Change back to menu panel
	}
}
