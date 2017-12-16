package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import de.htwg.innovationlab.gui.SmartBulb;

public class OpenAction extends RootAction {
	private static final long serialVersionUID = 1L;

	public OpenAction(SmartBulb smartBulb, String name) {
		super(smartBulb, name);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open file");
		if (fc.showOpenDialog(smartBulb) != JFileChooser.APPROVE_OPTION) {
			return;
		}
	}

}
