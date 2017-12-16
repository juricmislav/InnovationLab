package de.htwg.innovationlab.bridge;

import java.util.ArrayList;
import java.util.List;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;

import de.htwg.innovationlab.data.ConnectionProperties;
import de.htwg.innovationlab.gui.SmartBulb;

public class BridgeController {
	private SmartBulb smartBulb;
	private PHHueSDK pHHueSDK;
	private PHBridgeSearchManager sm;
	private ConnectionProperties connectionProperties;
	private List<BridgeListener> bridgeListeners;

	public BridgeController(SmartBulb smartBulb) {
		this.smartBulb = smartBulb;
		pHHueSDK = PHHueSDK.getInstance();
		pHHueSDK.getNotificationManager().registerSDKListener(phsdkListener);
		connectionProperties = new ConnectionProperties();
	}

	public void terminateInstance() {
		if (bridgeListeners != null) {
			for (BridgeListener listener : new ArrayList<>(bridgeListeners)) {
				listener.terminate();
			}
		}
		pHHueSDK.getNotificationManager().unregisterSDKListener(phsdkListener);
		pHHueSDK.stopPushlinkAuthentication();
		pHHueSDK.destroySDK();
	}

	public void addBridgeListener(BridgeListener listener) {
		if (bridgeListeners == null) {
			bridgeListeners = new ArrayList<>();
		}
		bridgeListeners.add(listener);
	}

	public void removeBridgeListener(BridgeListener listener) {
		if (bridgeListeners != null) {
			bridgeListeners.remove(listener);
		}
	}

	public void findAccessPoints() {
		for (BridgeListener listener : new ArrayList<>(bridgeListeners)) {
			listener.searchAccessPoints();
		}
		if (sm == null) {
			sm = (PHBridgeSearchManager) pHHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
		}
		sm.search(true, true);
		// sm.ipAddressSearch();
	}

	public boolean autoConnectToBridge() {
		String ipAddress = connectionProperties.getIpAdress();
		String userName = connectionProperties.getUserName();
		if (ipAddress == null || userName == null)
			return false;
		PHAccessPoint accessPoint = new PHAccessPoint(ipAddress, userName, null);
		pHHueSDK.connect(accessPoint);
		return true;
	}

	public boolean propertiesDefined() {
		if (connectionProperties.getIpAdress() == null || connectionProperties.getUserName() == null)
			return false;
		return true;
	}

	public void connectToBridge(PHAccessPoint accessPoint) {
		connectionProperties.setIpAddress(accessPoint.getIpAddress());
		accessPoint.setUsername(connectionProperties.getUserName());
		pHHueSDK.connect(accessPoint);
	}

	private PHSDKListener phsdkListener = new PHSDKListener() {

		@Override
		public void onAccessPointsFound(List<PHAccessPoint> accessPointsList) {
			if (bridgeListeners != null) {
				for (BridgeListener listener : new ArrayList<>(bridgeListeners)) {
					listener.accessPointsFound(accessPointsList);
				}
			}
		}

		@Override
		public void onAuthenticationRequired(PHAccessPoint accessPoint) {
			if (bridgeListeners != null) {
				for (BridgeListener listener : new ArrayList<>(bridgeListeners)) {
					listener.authenticate();
				}
			}
			pHHueSDK.startPushlinkAuthentication(accessPoint);
		}

		@Override
		public void onBridgeConnected(PHBridge bridge, String username) {
			smartBulb.setConnected(true);
			connectionProperties.setUserName(username);
			connectionProperties.saveProperties();
			if (bridgeListeners != null) {
				for (BridgeListener listener : new ArrayList<>(bridgeListeners)) {
					listener.bridgeConnected();
				}
			}
		}

		@Override
		public void onCacheUpdated(List<Integer> cache, PHBridge bridge) {}

		@Override
		public void onConnectionLost(PHAccessPoint accessPoint) {}

		@Override
		public void onConnectionResumed(PHBridge bridge) {}

		@Override
		public void onError(int code, final String message) {}

		@Override
		public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {}
	};
}
