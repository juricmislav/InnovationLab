package de.htwg.innovationlab.bridge;

import java.util.List;

import com.philips.lighting.hue.sdk.PHAccessPoint;

public interface BridgeListener {

	void accessPointsFound(List<PHAccessPoint> accessPointsList);
	
	void bridgeConnected();
	
	void searchAccessPoints();
	
	void authenticate();
	
	void terminate();
}
