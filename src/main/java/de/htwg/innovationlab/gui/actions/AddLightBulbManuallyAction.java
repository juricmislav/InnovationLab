package de.htwg.innovationlab.gui.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.htwg.innovationlab.gui.SmartBulb;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class AddLightBulbManuallyAction extends RootAction {
	private static final long serialVersionUID = 1L;

	public AddLightBulbManuallyAction(SmartBulb smartBulb, String name) {
		super(smartBulb, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new GridLayout(4, 1, 0, 5));
		
		JTextField name = new JTextField(25);
		dialogPanel.add(new JLabel("Lightbulbs's serial number:"));
		dialogPanel.add(name);
		
		int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Add Lightbulb", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, smartBulb.getIcon());

		String nameText = name.getText().trim().replaceAll("\\s+", "");
		if (result == JOptionPane.OK_OPTION && nameText.equals("")) {
			JOptionPane.showMessageDialog(smartBulb, "Invalid name", "Warning", JOptionPane.ERROR_MESSAGE);
			actionPerformed(e);
			return;
		}
		
		if (result == JOptionPane.OK_OPTION && !nameText.equals("")) {
			smartBulb.getBridgeController().addLightManually(nameText);
		}
	}

}
