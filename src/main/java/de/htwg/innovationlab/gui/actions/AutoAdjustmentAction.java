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
	private final int hueDefault = 24029;
	private final int saturationDefault = 254;
	private final int brightnessDefault = 200;
	private final int redDefault = 0;
	private final int greenDefault = 255;
	private final int blueDefault = 51;
	private int hue = hueDefault;
	private int saturation = saturationDefault;
	private int brightness = brightnessDefault;
	private int red = redDefault;
	private int green = greenDefault;
	private int blue = blueDefault;

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
		bulb.setBrightness(brightness);
		smartBulb.getBridgeController().updateLightState(bulb.getLight().getIdentifier(), lightState);
		bulb.setDisplayColorRGB(red, green, blue);
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
				red = 255;
				green = 204;
				blue = 0;
			} 
			if (roomType.equals(RoomType.HALLWAY)) {
				hue = 13653;
				saturation = 203;
				red = 204;
				green = 255;
				blue = 51;
			}
			if (roomType.equals(RoomType.TOILET)) {
				hue = 32767;
				saturation = 152;
				red = 102;
				green = 255;
				blue = 255;
			}
			if (roomType.equals(RoomType.KITCHEN)) {
				hue = 0;
				saturation = 102;
				red = 255;
				green = 153;
				blue = 153;
			}
			if (roomType.equals(RoomType.BEDROOM)) {
				hue = 43689;
				saturation = 102;
				red = 153;
				green = 153;
				blue = 255;
			}
		}
		if (profileType == ProfileType.GAMER) {
			hue = 49151;
			saturation = 254;
			red = 51;
			green = 0;
			blue = 102;
		}
		if (profileType == ProfileType.ROMANTIC) {
			if (roomType.equals(RoomType.TOILET)) {
				hue = 2184;
				saturation = 254;
				red = 255;
				green = 51;
				blue = 0;
			} else {
				hue = 63350;
				saturation = 254;
				red = 255;
				green = 0;
				blue = 51;
				
			}
		}
		if (profileType == ProfileType.PARTY) {
			if (roomType.equals(RoomType.TOILET)) {
				hue = 32767;
				saturation = 254;
				red = 0;
				green = 204;
				blue = 204;
			} else {
				hue = 54612;
				saturation = 254;
				red = 153;
				green = 0;
				blue = 153;
			}
		}
	}
}
