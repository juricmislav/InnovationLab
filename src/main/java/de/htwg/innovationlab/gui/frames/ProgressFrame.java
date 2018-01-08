package de.htwg.innovationlab.gui.frames;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import de.htwg.innovationlab.gui.SmartBulb;

public class ProgressFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private SmartBulb smartBulb;
	private JProgressBar progressBar = new JProgressBar();
	private boolean executeLoadingProfile;
	private Timer timer;

	public ProgressFrame(SmartBulb smartBulb, boolean executeLoadingProfile) {
		this.smartBulb = smartBulb;
		this.executeLoadingProfile = executeLoadingProfile;
		setTitle("Loading");
		setLocationRelativeTo(smartBulb);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenDim = getToolkit().getScreenSize();
		setSize((int) (screenDim.width / 7), (int) (screenDim.height / 16));
		setLayout(new GridLayout());
		setResizable(false);
		setAlwaysOnTop(true);
		setVisible(true);
		add(progressBar);

		timer = new Timer(3, progressBarAction);
		timer.start();
	}
	
	ActionListener progressBarAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (progressBar.getValue() >= 100) {
				timer.stop();
				setVisible(false);
				if (executeLoadingProfile) {
					executeLoadingProfile = false;
					smartBulb.loadProfile();
				}
				dispose();
				return;
			}
			progressBar.setValue(progressBar.getValue() + 1);
		}
	};
	
	@Override
	public void dispose() {
		timer.stop();
		if (executeLoadingProfile) {
			smartBulb.loadProfile();
		}
		super.dispose();
	}
}