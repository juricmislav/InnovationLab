package de.htwg.innovationlab.gui.bulb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

/**
 * Innovation Lab Project 2017/2018 HTWG Konstanz, University of Applied
 * Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class Bulb extends JPanel {

	private static final long serialVersionUID = 1L;
	private PHLight light;
	private Room room;
	private SmartBulb smartBulb;
	private JTextField displayColor = new JTextField(8);
	private JToggleButton switchButton = new JToggleButton();
	private JRadioButton autoAdjustmentCheckBox = new JRadioButton("Auto Adjustment");
	private JButton instantAutoAdjustment;
	private JLabel nameDisplay = new JLabel();
	private int hue;
	private int saturation;
	private int brightness;
	private boolean isOn;
	public static final float MAX_HUE = 65534;
	public static final float MAX_SATURATION = 254;
	public static final float MAX_BRIGTHNESS = 254;

	public Bulb(PHLight light, Room room, SmartBulb smartBulb) {
		nameDisplay.setText("Name: " + light.getIdentifier());
		this.light = light;
		this.room = room;
		this.smartBulb = smartBulb;
		this.hue = light.getLastKnownLightState().getHue();
		this.saturation = light.getLastKnownLightState().getSaturation();
		this.brightness = light.getLastKnownLightState().getBrightness();
		this.isOn = light.getLastKnownLightState().isOn();

		initGui();
		setdisplayColorHSBtoRGB(light.getLastKnownLightState().getHue(), light.getLastKnownLightState().getSaturation(),
				light.getLastKnownLightState().getBrightness());
		switchButton.setSelected(light.getLastKnownLightState().isOn());
		switchButton.setSelected(isOn);
	}

	public Bulb(PHLight light, String name, Room room, SmartBulb smartBulb, int hue, int saturation, int brightness,
			int r, int g, int b, boolean state, boolean autoAdjustment, boolean isOn) {
		nameDisplay.setText("Name: " + name);
		this.light = light;
		this.room = room;
		this.smartBulb = smartBulb;
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
		this.isOn = isOn;

		initGui();
		switchButton.setSelected(state);
		PHLightState lightState = new PHLightState();
		lightState.setOn(state);
		lightState.setHue(hue);
		lightState.setSaturation(saturation);
		lightState.setBrightness(brightness);
		lightState.setOn(isOn);
		smartBulb.getBridgeController().updateLightState(light.getIdentifier(), lightState);
		autoAdjustmentCheckBox.setSelected(autoAdjustment);
		switchButton.setSelected(isOn);
		displayColor.setBackground(new Color(r, g, b));
	}

	public void setdisplayColorHSBtoRGB(int hue, int saturation, int brightness) {
		displayColor.setText("");
		int col = Color.HSBtoRGB(hue / (float) MAX_HUE, saturation / (float) MAX_SATURATION,
				brightness / (float) MAX_BRIGTHNESS);
		displayColor.setBackground(new Color(Math.abs(col)));
	}

	public void setDisplayColorRGB(int r, int g, int b) {
		displayColor.setText("");
		displayColor.setBackground(new Color(r, g, b));
	}

	public void autoAdjusted() {
		displayColor.setText("Auto Adjusted");
	}

	private void initGui() {
		setLayout(new BorderLayout());
		setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

		JButton changeColor = new JButton("Change Bulb Colour");
		changeColor.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		changeColor.setAction(new SetColorAction(smartBulb, "Set Bulb Color", this));

		JButton changeBrightness = new JButton("Change Bulb Brightness");
		changeBrightness.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		changeBrightness.setAction(new SetBrightnessAction(smartBulb, "Set Bulb Brightness", this));

		JButton removeBulb = new JButton("Remove Bulb");
		removeBulb.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		removeBulb.setAction(new RemoveBulbAction(smartBulb, "Remove Bulb", room, this));

		JButton renameButton = new JButton("Rename");
		renameButton.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		renameButton.addActionListener(new RenameLight());
		
		nameDisplay.setFocusable(false);

		switchButton.setAction(new SwitchBulbAction(smartBulb, "On / Off", this));
		switchButton.setPreferredSize(SmartBulb.PREFERRED_SIZE);

		instantAutoAdjustment = new JButton();
		instantAutoAdjustment.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		instantAutoAdjustment.setAction(new AutoAdjustmentAction(smartBulb, "Adjust now", this));

		displayColor.setEnabled(false);
		displayColor.setHorizontalAlignment(JTextField.CENTER);
		displayColor
				.setFont(new Font(displayColor.getFont().getFontName(), Font.ITALIC, displayColor.getFont().getSize()));
		displayColor.setPreferredSize(SmartBulb.PREFERRED_SIZE);

		Border bulbPanelBorder = BorderFactory.createTitledBorder("Lightbulb idn: " + light.getIdentifier());
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.setBorder(bulbPanelBorder);
		westPanel.add(displayColor, BorderLayout.NORTH);

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
		
		JPanel nameDisplayPanel = new JPanel();
		nameDisplayPanel.add(nameDisplay);
		
		JPanel renameBulbPanel = new JPanel();
		renameBulbPanel.add(renameButton);

		JPanel midCentralPanel = new JPanel();
		midCentralPanel.setLayout(new BorderLayout());
		midCentralPanel.add(switchButtonPanel, BorderLayout.NORTH);
		midCentralPanel.add(autoAdjustmentPanel, BorderLayout.CENTER);
		
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		centralPanel.add(nameDisplayPanel, BorderLayout.NORTH);
		centralPanel.add(midCentralPanel, BorderLayout.CENTER);
		
		JPanel eastMidPanel = new JPanel();
		eastMidPanel.setLayout(new BorderLayout());
		eastMidPanel.add(autoAdjustmentCheckBoxPanel, BorderLayout.NORTH);
		eastMidPanel.add(removeBulbPanel, BorderLayout.CENTER);

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout());
		eastPanel.add(renameBulbPanel, BorderLayout.NORTH);
		eastPanel.add(eastMidPanel, BorderLayout.CENTER);

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
		new AutoAdjustmentAction(smartBulb, "Auto Adjustment", this)
				.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}

	public int getHue() {
		return hue;
	}

	public void setHue(int hue) {
		this.hue = hue;
	}

	public int getSaturation() {
		return saturation;
	}

	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setHSB(int hue, int saturation, int brightness) {
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public JTextField getDisplayColor() {
		return displayColor;
	}

	public Room getRoom() {
		return room;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

	public int getR() {
		return displayColor.getBackground().getRed();
	}

	public int getG() {
		return displayColor.getBackground().getGreen();
	}

	public int getB() {
		return displayColor.getBackground().getBlue();
	}

	public String getName() {
		return nameDisplay.getText();
	}

	private class RenameLight implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JPanel dialogPanel = new JPanel();
			dialogPanel.setLayout(new GridLayout(4, 1, 0, 5));

			JTextField name = new JTextField(25);
			name.setText(Bulb.this.nameDisplay.getText().substring(6));
			dialogPanel.add(new JLabel("Name:"));
			dialogPanel.add(name);

			int result = JOptionPane.showConfirmDialog(smartBulb, dialogPanel, "Rename Bulb",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
					new ImageIcon(SmartBulb.class.getResource("/icon.png")));

			String nameText = name.getText().trim().replaceAll("\\s+", " ");
			if (result == JOptionPane.OK_OPTION && nameText.equals("")) {
				JOptionPane.showMessageDialog(smartBulb, "Invalid name", "Warning", JOptionPane.ERROR_MESSAGE);
				actionPerformed(e);
				return;
			}

			if (result == JOptionPane.OK_OPTION && !nameText.equals("")) {
				Bulb.this.nameDisplay.setText("Name: " + nameText);
				smartBulb.refreshProfile();
			}
		}

	}
}
