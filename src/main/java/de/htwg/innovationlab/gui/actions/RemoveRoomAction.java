package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.room.Room;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class RemoveRoomAction extends RootAction {

	private static final long serialVersionUID = 1L;
	private Room room;

	public RemoveRoomAction(SmartBulb smartBulb, String name, Room room) {
		super(smartBulb, name);
		this.room = room;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog(smartBulb, "Are you sure?", "Warning",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
				UIManager.getIcon("OptionPane.warningIcon"));
		if (result == JOptionPane.YES_OPTION) {
			smartBulb.getProfile().removeRoom(room);
			smartBulb.refreshProfile();
		}
	}

}
