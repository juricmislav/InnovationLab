package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.bulb.Bulb;
import de.htwg.innovationlab.gui.room.Room;

public class RemoveBulbAction extends RootAction {

	private static final long serialVersionUID = 1L;
	private Room room;
	private Bulb bulb;

	public RemoveBulbAction(SmartBulb smartBulb, String name, Room room, Bulb bulb) {
		super(smartBulb, name);
		this.room = room;
		this.bulb = bulb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog(smartBulb, "Are you sure?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"));
		if (result == JOptionPane.YES_OPTION) {
			room.removeBulb(bulb);
		}
	}

}
