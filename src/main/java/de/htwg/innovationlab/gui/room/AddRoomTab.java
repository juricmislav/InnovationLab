package de.htwg.innovationlab.gui.room;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.actions.CreateRoomAction;

public class AddRoomTab extends JPanel {

	private static final long serialVersionUID = 1L;

	public AddRoomTab(SmartBulb smartBulb) {
		setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		JButton createRoom = new JButton();
		createRoom.setPreferredSize(SmartBulb.preferredSize);
		buttonPanel.add(createRoom);
		add(buttonPanel, BorderLayout.WEST);
		createRoom.setAction(new CreateRoomAction(smartBulb, "Create Room"));
	}
}
