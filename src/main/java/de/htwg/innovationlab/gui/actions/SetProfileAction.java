package de.htwg.innovationlab.gui.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import de.htwg.innovationlab.data.ProfileProperties;
import de.htwg.innovationlab.data.ProfileType;
import de.htwg.innovationlab.gui.SmartBulb;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class SetProfileAction extends RootAction {

	public SetProfileAction(SmartBulb smartBulb, String name) {
		super(smartBulb, name);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl T"));
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new GridLayout(4, 1, 0, 5));
		
		JTextField name = new JTextField(25);
		dialogPanel.add(new JLabel("Name:"));
		dialogPanel.add(name);
		
		dialogPanel.add(new JLabel("Type:"));
		List<ProfileType> profileTypes = Arrays.asList(ProfileType.values());
		JComboBox<ProfileType> comboBox = new JComboBox<>();
		for (ProfileType profilType : profileTypes) {
			comboBox.addItem(profilType);
		}
		if (smartBulb.getProfile() != null) {
			name.setText(smartBulb.getProfile().getName());
			comboBox.setSelectedItem(smartBulb.getProfile().getProfileType());
		}

		dialogPanel.add(comboBox);
		
		int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Set Profile", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, smartBulb.getIcon());

		String nameText = name.getText().trim().replaceAll("\\s+", " ");
		if (result == JOptionPane.OK_OPTION && nameText.equals("")) {
			JOptionPane.showMessageDialog(smartBulb, "Invalid name", "Warning", JOptionPane.ERROR_MESSAGE);
			actionPerformed(e);
			return;
		}
		
		if (result == JOptionPane.OK_OPTION && !nameText.equals("")) {
			if (smartBulb.getProfile() == null) {
				smartBulb.setProfile(new ProfileProperties(nameText, profileTypes.get(comboBox.getSelectedIndex())));		
			} else {
				smartBulb.getProfile().setName(nameText);
				smartBulb.getProfile().setProfileType(profileTypes.get(comboBox.getSelectedIndex()));
			}
			smartBulb.refreshProfile();
		}
	}

}
