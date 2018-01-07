package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;

import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.gui.SmartBulb;

public class SwitchBulbAction extends RootAction {

	private static final long serialVersionUID = 1L;
	private PHLight light;

	public SwitchBulbAction(SmartBulb smartBulb, String name, PHLight light) {
		super(smartBulb, name);
		this.light = light;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PHLightState lightState = new PHLightState();
		lightState.setOn(!light.getLastKnownLightState().isOn());
		smartBulb.getBridgeController().updateLightState(light.getIdentifier(), lightState);
	}

}
