package de.htwg.innovationlab.gui.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.gui.SmartBulb;

public class SetBrightnessAction extends RootAction {
	private static final long serialVersionUID = 1L;
	private PHLight light;

	public SetBrightnessAction(SmartBulb smartBulb, String name, PHLight light) {
		super(smartBulb, name);
		this.light = light;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new GridLayout(4, 1, 0, 5));
		dialogPanel.add(new JLabel("Brightness:"));
		
		JSlider slider = new JSlider();
		slider.setMaximum(254);
		slider.setValue(light.getLastKnownLightState().getBrightness());
		dialogPanel.add(slider);

		int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Set Bulb Brightness", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, smartBulb.getIcon());
		
		if (result == JOptionPane.OK_OPTION) {
			PHLightState lightState = new PHLightState();
			lightState.setBrightness(slider.getValue());
			smartBulb.getBridgeController().updateLightState(light.getIdentifier(), lightState);
		}
	}

}
