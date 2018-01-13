package de.htwg.innovationlab.gui.room;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.philips.lighting.model.PHLight;

import de.htwg.innovationlab.gui.SmartBulb;
import de.htwg.innovationlab.gui.actions.AddLightBulbAction;
import de.htwg.innovationlab.gui.actions.EditRoomAction;
import de.htwg.innovationlab.gui.actions.RemoveRoomAction;
import de.htwg.innovationlab.gui.bulb.Bulb;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class Room extends JPanel {
	private static final long serialVersionUID = 1L;
	private RoomType roomType;
	private String name;
	private SmartBulb smartBulb;
	private List<Bulb> bulbs;
	private JPanel bulbsPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(bulbsPanel);
	
	public Room(String name, RoomType roomType, SmartBulb smartBulb) {
		if (name == null || roomType == null) {
			throw new IllegalArgumentException("Room's name and type cannot be null.");
		}
		this.name = name;
		this.roomType = roomType;
		this.smartBulb = smartBulb;
		bulbs = new ArrayList<>();
		
		initGui();
	}
	
	private void initGui() {
		setLayout(new BorderLayout());

		JButton addBulb = new JButton();
		addBulb.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		addBulb.setAction(new AddLightBulbAction(smartBulb, "Add LightBulb"));
		
		JButton editRoom = new JButton();
		editRoom.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		editRoom.setAction(new EditRoomAction(smartBulb, "Edit Room", this));
		
		JButton removeRoom = new JButton();
		removeRoom.setPreferredSize(SmartBulb.PREFERRED_SIZE);
		removeRoom.setAction(new RemoveRoomAction(smartBulb, "Remove Room", this));
		
		JPanel addBulbPanel = new JPanel();
		addBulbPanel.setLayout(new BorderLayout());
		addBulbPanel.add(addBulb, BorderLayout.WEST);
		
		JPanel editRoomPanel = new JPanel();
		editRoomPanel.add(editRoom);
		editRoomPanel.add(removeRoom);

		add(addBulbPanel, BorderLayout.NORTH);
		add(editRoomPanel, BorderLayout.SOUTH);
	
		bulbsPanel.setLayout(new GridLayout(0, 1));
		add(scrollPane, BorderLayout.CENTER);
		refreshLights();
	}
	
	public String getName() {
		return name;
	}
	
	public RoomType getRoomType() {
		return roomType;
	}
	
	public List<Bulb> getBulbs() {
		return bulbs;
	}
	
	public void removeBulb(Bulb bulb) {
		bulbs.remove(bulb);
		refreshLights();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	
	public void addLight(PHLight light) {
		bulbs.add(new Bulb(light, this, smartBulb));
		refreshLights();
	}
	
	public void addBulb(Bulb bulb) {
		bulbs.add(bulb);
		refreshLights();
	}
	
	public void refreshLights() {
		bulbsPanel.removeAll();
		for (Bulb bulb : bulbs) {
			bulbsPanel.add(bulb);
		}
		scrollPane.repaint();
	}

}
