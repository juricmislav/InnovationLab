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
		JButton button = new JButton();
		buttonPanel.add(button);
		add(buttonPanel, BorderLayout.WEST);
		button.setAction(new CreateRoomAction(smartBulb, "Create Room"));
	}
}
