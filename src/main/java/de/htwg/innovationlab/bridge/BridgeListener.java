package de.htwg.innovationlab.bridge;

import java.util.List;

import com.philips.lighting.hue.sdk.PHAccessPoint;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public interface BridgeListener {

	void accessPointsFound(List<PHAccessPoint> accessPointsList);
	
	void bridgeConnected();
	
	void searchAccessPoints();
	
	void authenticate();
	
	void terminate();
}
