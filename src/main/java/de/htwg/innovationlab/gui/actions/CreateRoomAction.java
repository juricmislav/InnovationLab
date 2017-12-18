package de.htwg.innovationlab.gui.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.rooms.Room;
import de.htwg.innovationlab.gui.rooms.RoomType;

public class CreateRoomAction extends RootAction {

	private static final long serialVersionUID = 1L;
	
	public CreateRoomAction(SmartBulb smartBulb, String name) {
		super(smartBulb, name);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new GridLayout(4, 1, 0, 5));
		
		JTextField name = new JTextField(25);
		dialogPanel.add(new JLabel("Name:"));
		dialogPanel.add(name);
		
		dialogPanel.add(new JLabel("Type:"));
		List<RoomType> roomTypes = Arrays.asList(RoomType.values());
		JComboBox<RoomType> comboBox = new JComboBox<>();
		for (RoomType roomType : roomTypes) {
			comboBox.addItem(roomType);
		}
		dialogPanel.add(comboBox);
		
		int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Create Room", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, smartBulb.getIcon());
		
		if (result == JOptionPane.OK_OPTION && name.getText().equals("")) {
			JOptionPane.showMessageDialog(smartBulb, "Invalid name", "Warning", JOptionPane.ERROR_MESSAGE);
			actionPerformed(e);
		}
		
		if (result == JOptionPane.OK_OPTION && !name.getText().equals("")) {
			smartBulb.getProfile().addRoom(new Room(name.getText(), roomTypes.get(comboBox.getSelectedIndex())));		
			smartBulb.refreshProfile();
		}
	}

}
