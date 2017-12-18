package de.htwg.innovationlab.gui.rooms;

import javax.swing.JPanel;

public class Room extends JPanel {

	private RoomType roomType;
	private String name;
	
	public Room(String name, RoomType roomType) {
		if (name == null || roomType == null) {
			throw new IllegalArgumentException("Room's name and type cannot be null.");
		}
		this.name = name;
		this.roomType = roomType;
	}
	
	public String getName() {
		return name;
	}
	
	public RoomType getRoomType() {
		return roomType;
	}
}
