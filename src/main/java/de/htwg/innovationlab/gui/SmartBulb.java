package de.htwg.innovationlab.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.philips.lighting.hue.listener.PHGroupListener;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHHueError;

import de.htwg.innovationlab.bridge.BridgeController;
import de.htwg.innovationlab.data.Profile;
import de.htwg.innovationlab.data.ProfileType;
import de.htwg.innovationlab.gui.actions.CloseAction;
import de.htwg.innovationlab.gui.actions.PreferencesAction;
import de.htwg.innovationlab.gui.actions.SetProfileAction;
import de.htwg.innovationlab.gui.bulb.Bulb;
import de.htwg.innovationlab.gui.frames.ProgressFrame;
import de.htwg.innovationlab.gui.room.AddRoomTab;
import de.htwg.innovationlab.gui.room.Room;
import de.htwg.innovationlab.gui.room.RoomType;

public class SmartBulb extends JFrame implements PHGroupListener {
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();
	private BridgeController bridgeController = new BridgeController(this);
	private boolean connected = false;
	private ImageIcon icon = new ImageIcon("resources/icon.png");
	private Path profilePropertiesPath = Paths.get("Profile.properties");
	private Profile profile;
	private AddRoomTab addRoomTab = new AddRoomTab(SmartBulb.this);
	private JMenu profileMenu = new JMenu("Profile");
	private JMenuItem setProfile;
	private JRadioButton autoAdjustment = new JRadioButton("Auto Adjustment");
	private ScheduledExecutorService executor;

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
		Dimension screenDim = getToolkit().getScreenSize();
		setLocation(screenDim.width / 7, screenDim.height / 7);
		setSize((int) (screenDim.width / 2.2), (int) (screenDim.height / 1.7));
		setIconImage(icon.getImage());

		initGUI();
		if (!bridgeController.autoConnectToBridge()) {
			new PreferencesAction(this, "").actionPerformed(
					new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", System.currentTimeMillis(), 0));
		}

		if (profilePropertiesPath.toFile().exists()) {
			new ProgressFrame(this);
		} else {
			ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
			setProfile.getAction().actionPerformed(e);
			add(tabs);
			refreshProfile();
		}

		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(periodicTask, 2, 3600, TimeUnit.SECONDS);
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

		JMenuItem close = new JMenuItem(new CloseAction(this, "Close"));
		JMenuItem preferences = new JMenuItem(new PreferencesAction(this, "Preferences"));
		setProfile = new JMenuItem(new SetProfileAction(this, "Set Profile"));
		autoAdjustment.addActionListener(new SetAutoAdjustment());
		fileMenu.add(close);
		connectionMenu.add(preferences);
		profileMenu.add(setProfile);
		profileMenu.add(autoAdjustment);

		setJMenuBar(menuBar);
	}

	public void load() {
		add(tabs);
		loadProfile();
		refreshProfile();
	}

	public BridgeController getBridgeController() {
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
		executor.shutdown();
		saveProfile();
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
		try {
			if (profile == null || !connected) {
				tabs.setVisible(false);
				return;
			}
			int index = tabs.getSelectedIndex();
			tabs.setVisible(true);
			tabs.removeAll();

			for (Room room : profile.getRooms()) {
				tabs.add(" " + room.getName() + " ", room);
			}

			tabs.add(" + ", addRoomTab);
			if (tabs.getTabCount() > 1 && index < tabs.getTabCount())
				tabs.setSelectedIndex(index);
		} catch (Exception e) {

		}
	}

	public Room getSelectedRoom() {
		return (Room) tabs.getSelectedComponent();
	}

	public void loadProfile() {
		if (!profilePropertiesPath.toFile().exists())
			return;
		try {
			List<String> lines = Files.readAllLines(profilePropertiesPath);
			if (lines.isEmpty())
				return;
			Room lastRoom = null;
			for (String line : lines) {
				if (line.startsWith("#profile")) {
					String[] values = line.split("\t");
					if (values.length != 4)
						return;
					profile = new Profile(values[1], ProfileType.valueOf(values[2]));
					profile.setAutoAdjustment(Boolean.valueOf(values[3]));
					autoAdjustment.setSelected(Boolean.valueOf(values[3]));
				}
				if (line.startsWith("#room")) {
					String[] values = line.split("\t");
					if (values.length != 3)
						return;
					lastRoom = new Room(values[1], RoomType.valueOf(values[2]), this);
					profile.addRoom(lastRoom);
				}
				if (line.startsWith("#bulb")) {
					String[] values = line.split("\t");
					if (values.length != 7 || !bridgeController.containtsLight(values[1]))
						return;
					Bulb bulb = new Bulb(bridgeController.getLight(values[1]), lastRoom, this,
							Integer.valueOf(values[2]), Integer.valueOf(values[3]), Integer.valueOf(values[4]),
							Boolean.valueOf(values[5]), Boolean.valueOf(values[6]));
					lastRoom.addBulb(bulb);
				}
			}
		} catch (IOException e) {
			System.err.println("Could not read from property file: " + e);
		}
	}

	private void saveProfile() {
		if (profile == null)
			return;
		File file = profilePropertiesPath.toFile();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Could not make conn property file: " + e);
			}
		}

		try (PrintWriter writer = new PrintWriter(
				new OutputStreamWriter(new FileOutputStream(profilePropertiesPath.toString()), "utf-8"))) {
			StringBuilder sb = new StringBuilder();
			sb.append("#profile\t");
			sb.append(profile.getName());
			sb.append("\t");
			sb.append(profile.getProfileType());
			sb.append("\t");
			sb.append(profile.isAutoAdjustment());
			writer.println(sb.toString());

			for (Room room : profile.getRooms()) {
				sb = new StringBuilder();
				sb.append("#room\t");
				sb.append(room.getName());
				sb.append("\t");
				sb.append(room.getRoomType());
				writer.println(sb.toString());

				for (Bulb bulb : room.getBulbs()) {
					sb = new StringBuilder();
					sb.append("#bulb\t");
					sb.append(bulb.getLight().getIdentifier());
					sb.append("\t");
					sb.append(bulb.getLight().getLastKnownLightState().getHue());
					sb.append("\t");
					sb.append(bulb.getLight().getLastKnownLightState().getSaturation());
					sb.append("\t");
					sb.append(bulb.getLight().getLastKnownLightState().getBrightness());
					sb.append("\t");
					sb.append(bulb.getLight().getLastKnownLightState().isOn());
					sb.append("\t");
					sb.append(bulb.isAutoAdjustment());
					writer.println(sb.toString());
				}
			}

		} catch (IOException e) {
		}
	}
	
	Runnable periodicTask = new Runnable() {
	    public void run() {
			if (!profile.isAutoAdjustment()) return;
	        List<Room> rooms = new ArrayList<>(profile.getRooms());
	        for (Room room : rooms) {
	        	for (Bulb bulb : new ArrayList<>(room.getBulbs())) {
	        		if (bulb.isAutoAdjustment()) {
	        			bulb.performAutoAdjustment();
	        		}
	        	}
	        }
	    }
	};

	@Override
	public void onError(int code, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStateUpdate(Map<String, String> successAttribute, List<PHHueError> errorAttribute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreated(PHGroup group) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceivingAllGroups(List<PHBridgeResource> groupHeaders) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceivingGroupDetails(PHGroup group) {
		// TODO Auto-generated method stub

	}

	private class SetAutoAdjustment implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (profile == null)
				return;
			profile.setAutoAdjustment(autoAdjustment.isSelected());
		}
	}
}
