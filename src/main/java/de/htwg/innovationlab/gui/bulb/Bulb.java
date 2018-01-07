package de.htwg.innovationlab.gui.bulb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.actions.AutoAdjustmentAction;
import de.htwg.innovationlab.gui.actions.RemoveBulbAction;
import de.htwg.innovationlab.gui.actions.SetBrightnessAction;
import de.htwg.innovationlab.gui.actions.SetColorAction;
import de.htwg.innovationlab.gui.actions.SwitchBulbAction;
import de.htwg.innovationlab.gui.room.Room;

public class Bulb extends JPanel {

	private static final long serialVersionUID = 1L;
	private PHLight light;
	private Room room;
	private SmartBulb smartBulb;
	private JTextField currentColor = new JTextField(8);
	private JToggleButton switchButton = new JToggleButton();
	private JRadioButton autoAdjustmentCheckBox = new JRadioButton("Auto Adjustment");
	private JButton instantAutoAdjustment;
	private static final float MAX_HUE = 65535;
	private static final float MAX_SATURATION = 255;
	private static final float MAX_BRIGTHNESS = 255;

	public Bulb(PHLight light, Room room, SmartBulb smartBulb) {
		this.light = light;
		this.room = room;
		this.smartBulb = smartBulb;
		setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

		initGui();
		setColor(light.getLastKnownLightState().getHue(), light.getLastKnownLightState().getSaturation(),
				light.getLastKnownLightState().getBrightness());
		switchButton.setSelected(light.getLastKnownLightState().isOn());
	}

	public Bulb(PHLight light, Room room, SmartBulb smartBulb, int hue, int saturation, int brightness, boolean state, boolean autoAdjustment) {
		this.light = light;
		this.room = room;
		this.smartBulb = smartBulb;

		initGui();
		setColor(hue, saturation, brightness);
		switchButton.setSelected(state);
		PHLightState lightState = new PHLightState();
		lightState.setOn(state);
		lightState.setHue(hue);
		lightState.setSaturation(saturation);
		lightState.setBrightness(brightness);
		smartBulb.getBridgeController().updateLightState(light.getIdentifier(), lightState);
		autoAdjustmentCheckBox.setSelected(autoAdjustment);
	}

	public void setColor(int hue, int saturation, int brightness) {
		currentColor.setBackground(
				Color.getHSBColor(hue / MAX_HUE, saturation / MAX_SATURATION, brightness / MAX_BRIGTHNESS));
	}

	private void initGui() {
		setLayout(new BorderLayout());

		JButton changeColor = new JButton("Change Bulb Colour");
		changeColor.setAction(new SetColorAction(smartBulb, "Set Bulb Color", light, currentColor));

		JButton changeBrightness = new JButton("Change Bulb Brightness");
		changeBrightness.setAction(new SetBrightnessAction(smartBulb, "Set Bulb Brightness", light));

		JButton removeBulb = new JButton("Remove Bulb");
		removeBulb.setAction(new RemoveBulbAction(smartBulb, "Remove Bulb", room, this));

		switchButton.setAction(new SwitchBulbAction(smartBulb, "On / Off", light));

		instantAutoAdjustment = new JButton();
		instantAutoAdjustment.setAction(new AutoAdjustmentAction(smartBulb, "Instant Auto Adjustmenet", this));

		currentColor.setEnabled(false);

		Border bulbPanelBorder = BorderFactory.createTitledBorder("Lightbulb: " + light.getIdentifier());
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.setBorder(bulbPanelBorder);
		westPanel.add(currentColor, BorderLayout.NORTH);

		JPanel changeColorPanel = new JPanel();
		changeColorPanel.add(changeColor);

		JPanel changeBrightnessPanel = new JPanel();
		changeBrightnessPanel.add(changeBrightness);

		JPanel colorBrightnessButtonsPanel = new JPanel();
		colorBrightnessButtonsPanel.setLayout(new BorderLayout());
		colorBrightnessButtonsPanel.add(changeColorPanel, BorderLayout.NORTH);
		colorBrightnessButtonsPanel.add(changeBrightnessPanel, BorderLayout.CENTER);
		westPanel.add(colorBrightnessButtonsPanel, BorderLayout.CENTER);

		JPanel removeBulbPanel = new JPanel();
		removeBulbPanel.add(removeBulb);

		JPanel autoAdjustmentCheckBoxPanel = new JPanel();
		autoAdjustmentCheckBoxPanel.add(autoAdjustmentCheckBox);

		JPanel switchButtonPanel = new JPanel();
		switchButtonPanel.add(switchButton);

		JPanel autoAdjustmentPanel = new JPanel();
		autoAdjustmentPanel.add(instantAutoAdjustment);

		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		centralPanel.add(switchButtonPanel, BorderLayout.NORTH);
		centralPanel.add(autoAdjustmentPanel, BorderLayout.CENTER);

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout());
		eastPanel.add(removeBulbPanel, BorderLayout.NORTH);
		eastPanel.add(autoAdjustmentCheckBoxPanel, BorderLayout.CENTER);

		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
		add(centralPanel, BorderLayout.CENTER);
	}

	public PHLight getLight() {
		return light;
	}
	
	public boolean isAutoAdjustment() {
		return autoAdjustmentCheckBox.isSelected();
	}
	
	public void performAutoAdjustment() {
		ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
		autoAdjustmentCheckBox.getAction().actionPerformed(e);
	}
	
}
