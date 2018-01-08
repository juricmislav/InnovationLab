package de.htwg.innovationlab.gui.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.room.Room;
import de.htwg.innovationlab.gui.room.RoomType;

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
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(SmartBulb.class.getResource("/room.png")));
		
		String nameText = name.getText().trim().replaceAll("\\s+", " ");
		if (result == JOptionPane.OK_OPTION && (nameText.equals("") || 
				!nameText.substring(0, 1).matches("[a-zA-Z]") || smartBulb.getProfile().containtsRoom(nameText))) {
			JOptionPane.showMessageDialog(smartBulb, "Invalid name", "Warning", JOptionPane.ERROR_MESSAGE);
			actionPerformed(e);
			return;
		}
		
		if (result == JOptionPane.OK_OPTION && !(nameText.equals("") || 
				!nameText.substring(0, 1).matches("[a-zA-Z]") || smartBulb.getProfile().containtsRoom(nameText))) {
			smartBulb.getProfile().addRoom(new Room(nameText, roomTypes.get(comboBox.getSelectedIndex()), smartBulb));
			smartBulb.refreshProfile();
		}
	}

}
