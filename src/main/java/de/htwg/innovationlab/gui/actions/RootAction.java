package de.htwg.innovationlab.gui.actions;

import javax.swing.AbstractAction;

import de.htwg.innovationlab.gui.SmartBulb;

public abstract class RootAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	protected SmartBulb smartBulb;
	
	public RootAction(SmartBulb smartBulb, String name) {
		super(name);
		this.smartBulb = smartBulb;
	}
}
