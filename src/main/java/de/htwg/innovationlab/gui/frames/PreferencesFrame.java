package de.htwg.innovationlab.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.philips.lighting.hue.sdk.PHAccessPoint;

import de.htwg.innovationlab.bridge.BridgeListener;
import de.htwg.innovationlab.gui.SmartBulb;

public class PreferencesFrame extends JFrame implements BridgeListener {
	private static final long serialVersionUID = 1L;
	private SmartBulb smartBulb;
	private JButton autoConnect = new JButton();
	private JButton disconnect = new JButton();
	private JButton setup = new JButton();
	private JButton ok = new JButton("OK");
	private JTextField status;

	public PreferencesFrame(SmartBulb smartBulb) {
		this.smartBulb = smartBulb;
		setAlwaysOnTop(true);
		smartBulb.setEnabled(false);
		setTitle("Preferences");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		Dimension screenDim = getToolkit().getScreenSize();
		setLocation(screenDim.width / 6, screenDim.height / 6);
		setSize((int) (screenDim.width / 4.5), (int) (screenDim.height / 3));
		setIconImage(smartBulb.getIcon().getImage());

		smartBulb.getBridgeController().addBridgeListener(this);
		setButtonActions();
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout(10, 10));
		JPanel autoConnectPanel = new JPanel();
		JPanel disconnectPanel = new JPanel();
		JPanel setupPanel = new JPanel();
		JPanel okPanel = new JPanel();
		autoConnect.setPreferredSize(SmartBulb.preferredSize);
		disconnect.setPreferredSize(SmartBulb.preferredSize);
		setup.setPreferredSize(SmartBulb.preferredSize);
		ok.setPreferredSize(SmartBulb.preferredSize);
		ok.setAction(new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;	
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});
		autoConnectPanel.add(autoConnect);
		disconnectPanel.add(disconnect);
		setupPanel.add(setup);
		okPanel.add(ok);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 0));
		buttonPanel.add(autoConnectPanel);
		buttonPanel.add(disconnectPanel);
		buttonPanel.add(setupPanel);
		add(buttonPanel, BorderLayout.NORTH);
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		centralPanel.add(okPanel, BorderLayout.SOUTH);
		add(centralPanel, BorderLayout.CENTER);
		
		initStatusPanel();
	}

	private void initStatusPanel() {
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(BorderFactory.createEmptyBorder());
		JTextField statusText = new JTextField("Status:");
		status = new JTextField();
		statusText.setEditable(false);
		status.setEditable(false);
		statusText.setBorder(null);
		status.setBorder(null);
		statusPanel.setLayout(new BorderLayout(10, 10));
		statusPanel.add(statusText, BorderLayout.WEST);
		statusPanel.add(status, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.SOUTH);
		
		refreshStatus();
	}
	
	public void refreshStatus() {
		if (smartBulb.isConnected()) {
			status.setForeground(Color.GREEN);
			status.setText("Connected");
			autoConnect.setEnabled(false);
			disconnect.setEnabled(true);
			setup.setEnabled(false);
		} else {
			status.setForeground(Color.RED);
			status.setText("Not Connected");
			autoConnect.setEnabled(smartBulb.getBridgeController().propertiesDefined());
			disconnect.setEnabled(false);
			setup.setEnabled(true);
		}
	}

	private void setButtonActions() {
		
		autoConnect.setAction(new AbstractAction("Auto Connect") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (smartBulb.getBridgeController().autoConnectToBridge()) {
					smartBulb.loadServer(false);
					refreshStatus();
				};
			}
		});
		
		disconnect.setAction(new AbstractAction("Disconnect") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				smartBulb.disconnectBridge();
				smartBulb.getBridgeController().addBridgeListener(PreferencesFrame.this);
			}
		});
		
		setup.setAction(new AbstractAction("Setup") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SetupFrame setupFrame = new SetupFrame(PreferencesFrame.this);
				setupFrame.setVisible(true);
			}
		});
	}

	public SmartBulb getSmartBulb() {
		return smartBulb;
	}
	
	@Override
	public void dispose() {
		smartBulb.setEnabled(true);
		setVisible(false);
		if (smartBulb.isConnected() && smartBulb.getProfile() == null) {
				smartBulb.setUpProfile();
		}
		super.dispose();
	}

	@Override
	public void accessPointsFound(List<PHAccessPoint> accessPointsList) {}

	@Override
	public void bridgeConnected() {
		refreshStatus();
	}

	@Override
	public void searchAccessPoints() {}

	@Override
	public void authenticate() {}

	@Override
	public void terminate() {
		refreshStatus();
	}

}
