package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.frames.PreferencesFrame;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class PreferencesAction extends RootAction {
	private static final long serialVersionUID = 1L;

	public PreferencesAction(SmartBulb smartBulb, String name) {
		super(smartBulb, name);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl P"));
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame pref = new PreferencesFrame(smartBulb);
		pref.setVisible(true);
	}

}
