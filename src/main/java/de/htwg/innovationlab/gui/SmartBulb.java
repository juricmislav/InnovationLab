package de.htwg.innovationlab.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import de.htwg.innovationlab.bridge.BridgeController;
import de.htwg.innovationlab.gui.actions.CloseAction;
import de.htwg.innovationlab.gui.actions.OpenAction;
import de.htwg.innovationlab.gui.actions.PreferencesAction;

public class SmartBulb extends JFrame {
	private static final long serialVersionUID = 1L;
	private BridgeController bridgeController = new BridgeController(this);
	private boolean connected = false;
	
	public static void main(String[] args) {
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
	
		initGUI();
		if (!bridgeController.autoConnectToBridge()) {
			new PreferencesAction(this, "").actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", System.currentTimeMillis(), 0));
		}
	}

	private void initGUI() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu connectionMenu = new JMenu("Connection");
		fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD, 14));
		connectionMenu.setFont(connectionMenu.getFont().deriveFont(Font.BOLD, 14));
		fileMenu.setMnemonic(KeyEvent.VK_F);
		connectionMenu.setMnemonic(KeyEvent.VK_C);
		menuBar.add(fileMenu);
		menuBar.add(connectionMenu);

		JMenuItem open = new JMenuItem(new OpenAction(this, "Open"));
		JMenuItem close = new JMenuItem(new CloseAction(this, "Close"));
		JMenuItem preferences = new JMenuItem(new PreferencesAction(this, "Preferences"));
		fileMenu.add(open);
		fileMenu.add(close);
		connectionMenu.add(preferences);

		setJMenuBar(menuBar);
	}

	public BridgeController getBridge() {
		return bridgeController;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	@Override
	public void dispose() {
		bridgeController.terminateInstance();
		super.dispose();
	}

	public void disconnectBridge() {
		connected = false;
		bridgeController.terminateInstance();
		bridgeController = new BridgeController(this);
	}
}
