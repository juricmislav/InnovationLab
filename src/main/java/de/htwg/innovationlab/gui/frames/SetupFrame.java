package de.htwg.innovationlab.gui.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import com.philips.lighting.hue.sdk.PHAccessPoint;

import de.htwg.innovationlab.bridge.BridgeListener;
import de.htwg.innovationlab.gui.SmartBulb;

public class SetupFrame extends JFrame implements BridgeListener {

	private static final long serialVersionUID = 1L;
	private SmartBulb smartBulb;
	private PreferencesFrame preferencesFrame;
	private JButton findBridges = new JButton();
	private JButton connectBridge = new JButton();
	private List<PHAccessPoint> accessPointsList;
	private JList<String> accessPointsJList;
	private DefaultListModel<String> dataModel;
	private PHAccessPoint selectedAccessPoint;
	private JProgressBar progressBar;
	private AuthenticationFrame authenticationFrame;

	public SetupFrame(PreferencesFrame preferencesFrame) {
		this.preferencesFrame = preferencesFrame;
		smartBulb = preferencesFrame.getSmartBulb();
		setAlwaysOnTop(true);
		preferencesFrame.setEnabled(false);
		setTitle("Setup");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		Dimension screenDim = getToolkit().getScreenSize();
		setLocation(screenDim.width / 5, screenDim.height / 5);
		setSize((int) (screenDim.width / 5), (int) (screenDim.height / 3));

		initGui();
		setButtonActions();
		setListListener();
		connectBridge.setEnabled(false);
	}

	private void initGui() {
		setLayout(new BorderLayout(10, 2));

		Dimension preferredSize = new Dimension(150, 35);

		JPanel findBridgesPanel = new JPanel();
		findBridgesPanel.add(findBridges);
		findBridgesPanel.setPreferredSize(preferredSize);
		add(findBridgesPanel, BorderLayout.NORTH);

		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(2, 1));

		JPanel connectPanel = new JPanel();
		connectPanel.add(connectBridge);
		connectPanel.setPreferredSize(preferredSize);
		lowerPanel.add(connectPanel);
		add(lowerPanel, BorderLayout.SOUTH);

		progressBar = new JProgressBar();
		progressBar.setBorderPainted(false);
		lowerPanel.add(progressBar);

		dataModel = new DefaultListModel<String>();
		accessPointsJList = new JList<>(dataModel);
		JScrollPane scrollPane = new JScrollPane(accessPointsJList);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void setButtonActions() {
		findBridges.setAction(new AbstractAction("Find Bridges") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				connectBridge.setEnabled(false);
				selectedAccessPoint = null;
				smartBulb.getBridge().addBridgeListener(SetupFrame.this);
				smartBulb.getBridge().findAccessPoints();
			}
		});

		connectBridge.setAction(new AbstractAction("Connect") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				smartBulb.getBridge().connectToBridge(selectedAccessPoint);
			}
		});
	}

	private void setAccessPointList(List<PHAccessPoint> accessPointsList) {
		if (accessPointsList.isEmpty()) {
			return;
		}
		SetupFrame.this.accessPointsList = accessPointsList;
		String[] data = new String[accessPointsList.size()];
		for (int i = 0; i < accessPointsList.size(); i++) {
			data[i] = accessPointsList.get(i).getIpAddress();
		}

		this.accessPointsJList.setListData(data);
		this.setSize((int) (this.getSize().getWidth()), (int) (this.getSize().getHeight()));
	}

	private void setListListener() {
		accessPointsJList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedIndex = accessPointsJList.getSelectedIndex();
				if (selectedIndex > -1) {
					connectBridge.setEnabled(true);
					selectedAccessPoint = accessPointsList.get(selectedIndex);
					return;
				}
				connectBridge.setEnabled(false);
				selectedAccessPoint = null;
			}
		});
	}

	@Override
	public void dispose() {
		preferencesFrame.refreshStatus();
		preferencesFrame.setEnabled(true);
		smartBulb.getBridge().removeBridgeListener(SetupFrame.this);
		super.dispose();
	}

	@Override
	public void accessPointsFound(List<PHAccessPoint> accessPointsList) {
		setAccessPointList(accessPointsList);
		progressBar.setIndeterminate(false);
	}

	@Override
	public void bridgeConnected() {
		if (authenticationFrame != null) {
			authenticationFrame.dispose();
		}
		dispose();
	}

	@Override
	public void searchAccessPoints() {
		progressBar.setIndeterminate(true);
	}

	@Override
	public void authenticate() {
		authenticationFrame = new AuthenticationFrame();
		authenticationFrame.setVisible(true);
		authenticationFrame.setAlwaysOnTop(true);
	}

	@Override
	public void terminate() {
		if (authenticationFrame != null) {
			authenticationFrame.dispose();
		}
		dispose();
	}
}
