package de.htwg.innovationlab.gui.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JColorChooser;
import javax.swing.JTextField;

import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.gui.SmartBulb;

public class SetColorAction extends RootAction {
	private static final long serialVersionUID = 1L;
	private PHLight light;
	private JTextField currentColor;
	
	public SetColorAction(SmartBulb smartBulb, String name, PHLight light, JTextField currentColor) {
		super(smartBulb, name);
		this.light = light;
		this.currentColor = currentColor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Color lightColor = JColorChooser.showDialog(smartBulb, "Set Bulb Color", smartBulb.getBackground());

		if (lightColor != null) {
			float xy[] = PHUtilities.calculateXYFromRGB(lightColor.getRed(), lightColor.getGreen(),
					lightColor.getBlue(), "LCT001");
			PHLightState lightState = new PHLightState();
			lightState.setX(xy[0]);
			lightState.setY(xy[1]);
			smartBulb.getBridgeController().updateLightState(light.getIdentifier(), lightState);
			currentColor.setBackground(new Color(lightColor.getRed(), lightColor.getGreen(),
					lightColor.getBlue()));
		}
	}

}
