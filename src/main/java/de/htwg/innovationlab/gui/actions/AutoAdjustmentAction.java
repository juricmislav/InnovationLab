package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;

import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.bulb.Bulb;

public class AutoAdjustmentAction extends RootAction {

	private static final long serialVersionUID = 1L;
	private Bulb bulb;

	public AutoAdjustmentAction(SmartBulb smartBulb, String name, Bulb bulb) {
		super(smartBulb, name);
		this.bulb = bulb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int hue = 30000;
		int saturation = 224;
		int brightness = 224;
		
		PHLightState lightState = new PHLightState();
		lightState.setHue(30000);
		lightState.setSaturation(224);
		lightState.setBrightness(224);
		smartBulb.getBridgeController().updateLightState(bulb.getLight().getIdentifier(), lightState);
		bulb.setdisplayColorHSBtoRGB(hue, saturation, brightness);
		bulb.setHSB(hue, saturation, brightness);
	}

}
