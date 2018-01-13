package de.htwg.innovationlab.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import de.htwg.innovationlab.data.ConnectionProperties;
import de.htwg.innovationlab.gui.SmartBulb;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class BridgeController {
	private SmartBulb smartBulb;
	private PHHueSDK pHHueSDK;
	private PHBridgeSearchManager sm;
	private ConnectionProperties connectionProperties;
	private List<BridgeListener> bridgeListeners;
	private PHBridge bridge;

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
		if (pHHueSDK != null) {
			try {
				pHHueSDK.getNotificationManager().unregisterSDKListener(phsdkListener);
				pHHueSDK.stopPushlinkAuthentication();
				pHHueSDK.destroySDK();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		pHHueSDK = null;
		bridge = null;
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
		String macAddress = connectionProperties.getMacAddress();
		if (ipAddress == null || userName == null || macAddress == null)
			return false;
		PHAccessPoint accessPoint = new PHAccessPoint(ipAddress, userName, macAddress);
		pHHueSDK.connect(accessPoint);
		return true;
	}

	public boolean propertiesDefined() {
		return !(connectionProperties.getIpAdress() == null || connectionProperties.getUserName() == null);
	}

	public void connectToBridge(PHAccessPoint accessPoint) {
		connectionProperties.setIpAddress(accessPoint.getIpAddress());
		connectionProperties.setMacAddress(accessPoint.getMacAddress());
		accessPoint.setUsername(connectionProperties.getUserName());
		pHHueSDK.connect(accessPoint);
	}

	public PHBridge getBridge() {
		return bridge;
	}

	public List<PHLight> getAllLights() {
		if (bridge == null)
			return null;
		return bridge.getResourceCache().getAllLights();
	}
	
	public Map<String, PHLight> getLights() {
		if (bridge == null)
			return null;
		return bridge.getResourceCache().getLights();
	}
	
	public PHLight getLight(String idn) {
		if (idn == null || bridge == null || bridge.getResourceCache().getLights() == null)
			return null;
		return bridge.getResourceCache().getLights().get(idn);
	}
	
	public boolean containtsLight(String idn) {
		if (bridge == null) return false;
		if (bridge.getResourceCache().getLights().containsKey(idn) && 
				bridge.getResourceCache().getLights().get(idn) != null) return true;
		return false;
	}

	public void updateLightState(String idn, PHLightState lightState) {
		if (idn == null || lightState == null) return;
		bridge.updateLightState(idn, lightState, null);
	}
	
	public void addLightManually(String idn) {
		if (idn == null || idn.equals("") || bridge == null) return;
		List<String> serials = new ArrayList<>();
		serials.add(idn);
		bridge.findNewLightsWithSerials(serials, getLightListener());
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
			BridgeController.this.bridge = bridge;
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
		public void onCacheUpdated(List<Integer> cache, PHBridge bridge) {
		}

		@Override
		public void onConnectionLost(PHAccessPoint accessPoint) {
		}

		@Override
		public void onConnectionResumed(PHBridge bridge) {
		}

		@Override
		public void onError(int code, final String message) {
		}

		@Override
		public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
		}
	};

	private PHLightListener getLightListener() {
		return new PHLightListener() {
			boolean found = false;

			@Override
			public void onSuccess() {
			}

			@Override
			public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
			}

			@Override
			public void onError(int arg0, String arg1) {
			}

			@Override
			public void onReceivingLightDetails(PHLight arg0) {
				System.out.println("details");
			}

			@Override
			public void onReceivingLights(List<PHBridgeResource> arg0) {
				if (found)
					return;
				found = true;
				JOptionPane.showMessageDialog(smartBulb, "Light added", "Info", JOptionPane.INFORMATION_MESSAGE);
			}

			@Override
			public void onSearchComplete() {
			}
		};
	}
}
