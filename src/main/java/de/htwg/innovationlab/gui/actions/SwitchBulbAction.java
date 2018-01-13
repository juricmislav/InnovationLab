package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;

import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.bulb.Bulb;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class SwitchBulbAction extends RootAction {

	private static final long serialVersionUID = 1L;
	private Bulb bulb;

	public SwitchBulbAction(SmartBulb smartBulb, String name, Bulb bulb) {
		super(smartBulb, name);
		this.bulb = bulb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PHLightState lightState = new PHLightState();
		lightState.setOn(!bulb.isOn());
		bulb.setOn(!bulb.isOn());
		smartBulb.getBridgeController().updateLightState(bulb.getLight().getIdentifier(), lightState);
	}

}
