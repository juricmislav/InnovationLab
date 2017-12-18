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

import de.htwg.innovationlab.data.Profile;
import de.htwg.innovationlab.data.ProfileType;
import de.htwg.innovationlab.gui.SmartBulb;

public class CreateProfileAction extends RootAction {

	public CreateProfileAction(SmartBulb smartBulb, String name) {
		super(smartBulb, name);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
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
		dialogPanel.add(comboBox);
		
		int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Create Profile", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, smartBulb.getIcon());
		
		if (result == JOptionPane.OK_OPTION && name.getText().equals("")) {
			JOptionPane.showMessageDialog(smartBulb, "Invalid name", "Warning", JOptionPane.ERROR_MESSAGE);
			actionPerformed(e);
		}
		
		if (result == JOptionPane.OK_OPTION && !name.getText().equals("")) {
			smartBulb.setProfile(new Profile(name.getText(), profileTypes.get(comboBox.getSelectedIndex())));		
			smartBulb.refreshProfile();
		}
	}

}
