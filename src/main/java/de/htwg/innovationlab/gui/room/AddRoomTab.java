package de.htwg.innovationlab.gui.room;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.actions.CreateRoomAction;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class AddRoomTab extends JPanel {

	private static final long serialVersionUID = 1L;

	public AddRoomTab(SmartBulb smartBulb) {
		setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		JButton createRoom = new JButton();
		createRoom.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		buttonPanel.add(createRoom);
		add(buttonPanel, BorderLayout.WEST);
		createRoom.setAction(new CreateRoomAction(smartBulb, "Create Room"));
	}
}
