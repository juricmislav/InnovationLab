package de.htwg.innovationlab.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Application {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		try {
			SwingUtilities.invokeLater(() -> {
				new SmartBulb().setVisible(true);
			});
		} catch (Exception e) {}
	}
}
