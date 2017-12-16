package de.htwg.innovationlab.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import de.htwg.innovationlab.gui.SmartBulb;

public class CloseAction extends RootAction {
	private static final long serialVersionUID = 1L;
	
	public CloseAction(SmartBulb smartBulb, String name) {
		super(smartBulb, name);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));	
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		smartBulb.dispatchEvent(new WindowEvent(smartBulb, WindowEvent.WINDOW_CLOSING));
	}

}
