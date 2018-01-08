package de.htwg.innovationlab.gui.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JColorChooser;

import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.bulb.Bulb;

public class SetColorAction extends RootAction {
	private static final long serialVersionUID = 1L;
	private Bulb bulb;

	public SetColorAction(SmartBulb smartBulb, String name, Bulb bulb) {
		super(smartBulb, name);
		this.bulb = bulb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Color color = JColorChooser.showDialog(smartBulb, "Set Bulb Color", smartBulb.getBackground());

		if (color != null) {
			float[] HSB = new float[3];
			HSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), HSB);
			PHLightState lightState = new PHLightState();

			lightState.setHue((int) Math.round(HSB[0] * Bulb.MAX_HUE));
			lightState.setSaturation((int) Math.round(HSB[1] * Bulb.MAX_SATURATION));

			smartBulb.getBridgeController().updateLightState(bulb.getLight().getIdentifier(), lightState);
			bulb.getDisplayColor().setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue()));
			bulb.getDisplayColor().setText("");

			bulb.setHSB(
					(int) Math.round(HSB[0] * Bulb.MAX_HUE), 
					(int) Math.round(HSB[1] * Bulb.MAX_SATURATION), 
					(int) Math.round(HSB[2] * Bulb.MAX_BRIGTHNESS));
		}
	}

}
