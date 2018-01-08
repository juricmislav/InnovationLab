package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.data.ProfileType;
import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.bulb.Bulb;
import de.htwg.innovationlab.gui.room.RoomType;

public class AutoAdjustmentAction extends RootAction {

	private static final long serialVersionUID = 1L;
	private Bulb bulb;
	private final int hueDefault = 30000;
	private final int saturationDefault = 254;
	private final int brightnessDefault = 155;
	private int hue = hueDefault;
	private int saturation = saturationDefault;
	private int brightness = brightnessDefault;

	public AutoAdjustmentAction(SmartBulb smartBulb, String name, Bulb bulb) {
		super(smartBulb, name);
		this.bulb = bulb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		determineBestLight();
		
		PHLightState lightState = new PHLightState();
		lightState.setHue(hue);
		lightState.setSaturation(saturation);
		lightState.setBrightness(brightness);
		smartBulb.getBridgeController().updateLightState(bulb.getLight().getIdentifier(), lightState);
		bulb.setdisplayColorHSBtoRGB(hue, saturation, brightness);
		bulb.setHSB(hue, saturation, brightness);
		bulb.autoAdjusted();
	}

	private void determineBestLight() {
		RoomType roomType = bulb.getRoom().getRoomType();
		ProfileType profileType = smartBulb.getProfile().getProfileType();
		
		DateFormat formatHour = new SimpleDateFormat("HH");
		DateFormat formatMinutes = new SimpleDateFormat("mm");
		Date date = new Date();
		int hour = Integer.parseInt(formatHour.format(date));
		int min = Integer.parseInt(formatMinutes.format(date));

		if (hour >= 21 || hour < 7) {
			brightness = 10;
		} 
		if (hour >= 7 && hour < 14) {
			brightness = hour * 19;
			if (min > 30) brightness += 7;
		}
		if (hour >= 14 && hour < 21) {
			brightness = 110 + (21 - hour) * 19;
			if (min > 30) brightness += 7;
		}
		
		if (profileType == ProfileType.LEISURE) {
			if (roomType.equals(RoomType.LIVINGROOM)) {
				hue = 8738;
				saturation = 254;
			} 
			if (roomType.equals(RoomType.HALLWAY)) {
				hue = 32767;
				saturation = 254;
			}
			if (roomType.equals(RoomType.TOILET)) {
				hue = 13653;
				saturation = 254;
			}
			if (roomType.equals(RoomType.KITCHEN)) {
				hue = 32767;
				saturation = 102;
			}
			if (roomType.equals(RoomType.BEDROOM)) {
				hue = 36408;
				saturation = 152;
			}
		}
		if (profileType == ProfileType.GAMER) {
			hue = 43689;
			saturation = 110;
		}
		if (profileType == ProfileType.ROMANTIC) {
			if (roomType.equals(RoomType.TOILET)) {
				hue = 13653;
				saturation = 254;
			} else {
				hue = 0;
				saturation = 254;
			}
		}
		if (profileType == ProfileType.PARTY) {
			if (roomType.equals(RoomType.TOILET)) {
				hue = 43689;
				saturation = 110;
			} else {
				hue = 54612;
				saturation = 254;
			}
		}
	}
}
