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

public class EditRoomAction extends RootAction {

	private static final long serialVersionUID = 1L;
	private Room room;

	public EditRoomAction(SmartBulb smartBulb, String name, Room room) {
		super(smartBulb, name);
		this.room = room;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new GridLayout(4, 1, 0, 5));
		
		JTextField name = new JTextField(25);
		name.setText(room.getName());
		dialogPanel.add(new JLabel("Name:"));
		dialogPanel.add(name);
		
		dialogPanel.add(new JLabel("Type:"));
		List<RoomType> roomTypes = Arrays.asList(RoomType.values());
		JComboBox<RoomType> comboBox = new JComboBox<>();
		for (RoomType roomType : roomTypes) {
			comboBox.addItem(roomType);
		}
		comboBox.setSelectedItem(room.getRoomType());
		dialogPanel.add(comboBox);
		
		int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Edit Room", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon("resources/room.png"));
		
		String nameText = name.getText().trim().replaceAll("\\s+", " ");
		if (result == JOptionPane.OK_OPTION && (nameText.equals("") || 
				!nameText.substring(0, 1).matches("[a-zA-Z]"))) {
			JOptionPane.showMessageDialog(smartBulb, "Invalid name", "Warning", JOptionPane.ERROR_MESSAGE);
			actionPerformed(e);
			return;
		}
		
		if (result == JOptionPane.OK_OPTION && !(nameText.equals("") || 
				!nameText.substring(0, 1).matches("[a-zA-Z]"))) {
				room.setName(nameText);
			room.setRoomType(roomTypes.get(comboBox.getSelectedIndex()));
			smartBulb.refreshProfile();
		}
	}

}
