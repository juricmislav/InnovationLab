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

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class SetupFrame extends JFrame implements BridgeListener {

	private static final long serialVersionUID = 1L;
	private SmartBulb smartBulb;
	private PreferencesFrame preferencesFrame;
	private JButton findBridges = new JButton();
	private JButton connectBridge = new JButton();
	private JButton cancel = new JButton();
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
		setIconImage(smartBulb.getIcon().getImage());

		initGui();
		setButtonActions();
		setListListener();
		connectBridge.setEnabled(false);
		progressBar.setVisible(false);
	}

	private void initGui() {
		setLayout(new BorderLayout(5, 5));

		JPanel findBridgesPanel = new JPanel();
		findBridgesPanel.add(findBridges);
		findBridges.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		add(findBridgesPanel, BorderLayout.NORTH);

		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(3, 1));
		add(lowerPanel, BorderLayout.SOUTH);
		
		progressBar = new JProgressBar();
		progressBar.setBorderPainted(false);
		lowerPanel.add(progressBar);

		JPanel connectPanel = new JPanel();
		connectPanel.add(connectBridge);
		connectBridge.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		lowerPanel.add(connectPanel);

		cancel.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		JPanel cancelPanel = new JPanel();
		cancelPanel.add(cancel);
		lowerPanel.add(cancelPanel);

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
				smartBulb.getBridgeController().addBridgeListener(SetupFrame.this);
				smartBulb.getBridgeController().findAccessPoints();
			}
		});

		connectBridge.setAction(new AbstractAction("Connect") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				smartBulb.getBridgeController().connectToBridge(selectedAccessPoint);
			}
		});
		
		cancel.setAction(new AbstractAction("Cancel") {
			private static final long serialVersionUID = 1L;

			
			@Override
			public void actionPerformed(ActionEvent e) {
				terminate();
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
			data[i] = accessPointsList.get(i).getIpAddress() + " | " + accessPointsList.get(i).getBridgeId();
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
					selectedAccessPoint = accessPointsList.get(selectedIndex);
					connectBridge.setEnabled(true);
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
		smartBulb.getBridgeController().removeBridgeListener(SetupFrame.this);
		super.dispose();
	}

	@Override
	public void accessPointsFound(List<PHAccessPoint> accessPointsList) {
		setAccessPointList(accessPointsList);
		progressBar.setIndeterminate(false);
		progressBar.setVisible(false);
	}

	@Override
	public void bridgeConnected() {
		if (authenticationFrame != null) {
			smartBulb.loadServer(false);
			authenticationFrame.dispose();
		}
		dispose();
	}

	@Override
	public void searchAccessPoints() {
		progressBar.setIndeterminate(true);
		progressBar.setVisible(true);
	}

	@Override
	public void authenticate() {
		authenticationFrame = new AuthenticationFrame(smartBulb);
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
