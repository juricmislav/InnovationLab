package de.htwg.innovationlab.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.htwg.innovationlab.bridge.BridgeController;
import de.htwg.innovationlab.data.Profile;
import de.htwg.innovationlab.gui.actions.CloseAction;
import de.htwg.innovationlab.gui.actions.CreateProfileAction;
import de.htwg.innovationlab.gui.actions.OpenAction;
import de.htwg.innovationlab.gui.actions.PreferencesAction;
import de.htwg.innovationlab.gui.rooms.AddRoomTab;
import de.htwg.innovationlab.gui.rooms.Room;

public class SmartBulb extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();
	private BridgeController bridgeController = new BridgeController(this);
	private boolean connected = false;
	private ImageIcon icon = new ImageIcon("resources/icon.png");
	private Profile profile;
	private AddRoomTab addRoomTab = new AddRoomTab(SmartBulb.this);
	private JMenu profileMenu = new JMenu("Profile");

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		SwingUtilities.invokeLater(() -> {
			new SmartBulb().setVisible(true);
		});
	}

	public SmartBulb() {
		setTitle("IBulb");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		Dimension screenDim = getToolkit().getScreenSize();
		setLocation(screenDim.width / 7, screenDim.height / 7);
		setSize((int) (screenDim.width / 2.2), (int) (screenDim.height / 1.7));
		setIconImage(icon.getImage());

		initGUI();
		if (!bridgeController.autoConnectToBridge()) {
			new PreferencesAction(this, "").actionPerformed(
					new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", System.currentTimeMillis(), 0));
		}
		add(tabs);
		refreshProfile();
	}

	private void initGUI() {
		JMenuBar menuBar = new JMenuBar();		
		JMenu fileMenu = new JMenu("File");
		JMenu connectionMenu = new JMenu("Connection");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		connectionMenu.setMnemonic(KeyEvent.VK_C);
		profileMenu.setMnemonic(KeyEvent.VK_P);
		menuBar.add(fileMenu);
		menuBar.add(connectionMenu);
		menuBar.add(profileMenu);

		JMenuItem open = new JMenuItem(new OpenAction(this, "Open"));
		JMenuItem close = new JMenuItem(new CloseAction(this, "Close"));
		JMenuItem preferences = new JMenuItem(new PreferencesAction(this, "Preferences"));
		JMenuItem createProfile = new JMenuItem(new CreateProfileAction(this, "Create Profile"));
		fileMenu.add(open);
		fileMenu.add(close);
		connectionMenu.add(preferences);
		profileMenu.add(createProfile);

		setJMenuBar(menuBar);
	}

	public BridgeController getBridge() {
		return bridgeController;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
		profileMenu.setEnabled(true);
		refreshProfile();
	}

	@Override
	public void dispose() {
		bridgeController.terminateInstance();
		super.dispose();
	}

	public void disconnectBridge() {
		profileMenu.setEnabled(false);
		connected = false;
		bridgeController.terminateInstance();
		bridgeController = new BridgeController(this);
		refreshProfile();
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
		tabs.removeAll();
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public void refreshProfile() {
		if (profile == null || !connected) {
			tabs.setVisible(false);
			return;
		}
		tabs.setVisible(true);
		tabs.remove(addRoomTab);
		
		for (Room room : profile.getRooms()) {
			tabs.add(" " + room.getName() + " ", room);
		}
		
		tabs.add(" + ", addRoomTab);
	}
	
}
