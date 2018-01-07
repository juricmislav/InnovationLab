package de.htwg.innovationlab.gui.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.philips.lighting.model.PHLight;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.bulb.Bulb;
import de.htwg.innovationlab.gui.room.Room;

public class AddLightBulbAction extends RootAction {

	private static final long serialVersionUID = 1L;

	public AddLightBulbAction(SmartBulb smartBulb, String name) {
		super(smartBulb, name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<PHLight> lights = smartBulb.getBridgeController().getAllLights();
		for (Room room : smartBulb.getProfile().getRooms()) {
			for (Bulb bulb : room.getBulbs()) {
				lights.remove(bulb.getLight());
			}
		}
		if (lights == null || lights.isEmpty()) {
			JOptionPane.showMessageDialog(smartBulb, "All lights are in use.\nRemove from other room(s) to use again.",
					"Info", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new GridLayout(3, 1, 0, 5));

		dialogPanel.add(new JLabel("Available lights:"));
		JComboBox<PHLight> comboBox = new JComboBox<>();
		for (PHLight light : lights) {
			comboBox.addItem(light);
		}
		dialogPanel.add(comboBox);

		int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Add Lightbulb",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, smartBulb.getIcon());

		if (result == JOptionPane.OK_OPTION) {
			smartBulb.getSelectedRoom().addLight(lights.get(comboBox.getSelectedIndex()));
			smartBulb.getSelectedRoom().refreshLights();
			smartBulb.refreshProfile();
		}
	}

}
