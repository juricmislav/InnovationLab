package de.htwg.innovationlab.gui.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

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
public class SetBrightnessAction extends RootAction {
	private static final long serialVersionUID = 1L;
	private Bulb bulb;

	public SetBrightnessAction(SmartBulb smartBulb, String name, Bulb bulb) {
		super(smartBulb, name);
		this.bulb = bulb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new GridLayout(4, 1, 0, 5));
		dialogPanel.add(new JLabel("Brightness:"));
		
		JSlider slider = new JSlider();
		slider.setMinimum(0);
		slider.setMaximum(254);
		slider.setValue(bulb.getBrightness());
		dialogPanel.add(slider);

		int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Set Bulb Brightness", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, smartBulb.getIcon());
		
		if (result == JOptionPane.OK_OPTION) {
			PHLightState lightState = new PHLightState();
			lightState.setBrightness(slider.getValue());
			smartBulb.getBridgeController().updateLightState(bulb.getLight().getIdentifier(), lightState);
			bulb.setBrightness(slider.getValue());
		}
	}
}
