package de.htwg.innovationlab.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.htwg.innovationlab.gui.rooms.Room;

public class Profile {

	private String name;
	private ProfileType profileType;
	private Map<String, Room> rooms;
	
	public Profile(String name, ProfileType profileType) {
		if (name == null || profileType == null) {
			throw new IllegalArgumentException("Profile's name and type cannot be null.");
		}
		this.name = name;
		this.profileType = profileType;
		rooms = new HashMap<>();
	}
	
	public Room getRoom(String name) {
		return rooms.get(name);
	}
	
	public boolean hasRooms() {
		return !rooms.isEmpty();
	}
	
	public List<Room> getRooms() {
		return new ArrayList<>(rooms.values());
	}
	
	public void addRoom(Room room) {
		rooms.put(room.getName(), room);
	}
	
}
