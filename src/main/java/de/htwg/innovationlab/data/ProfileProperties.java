package de.htwg.innovationlab.data;

import java.util.ArrayList;
import java.util.List;

import de.htwg.innovationlab.gui.room.Room;

public class ProfileProperties {

	private String name;
	private ProfileType profileType;
	private List<Room> rooms;
	private boolean autoAdjustment;
	
	public ProfileProperties(String name, ProfileType profileType) {
		if (name == null || profileType == null) {
			throw new IllegalArgumentException("Profile's name and type cannot be null.");
		}
		this.name = name;
		this.profileType = profileType;
		rooms = new ArrayList<>();
	}
	
	public boolean hasRooms() {
		return !rooms.isEmpty();
	}
	
	public List<Room> getRooms() {
		return rooms;
	}
	
	public void addRoom(Room room) {
		rooms.add(room);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setProfileType(ProfileType profileType) {
		this.profileType = profileType;
	}
	
	public String getName() {
		return name;
	}
	
	public void removeRoom(Room room) {
		rooms.remove(room);
	}
	
	public ProfileType getProfileType() {
		return profileType;
	}
	
	public boolean containtsRoom(String name) {
		for (Room room : rooms) {
			if (room.getName().equals(name)) return true;
		}
		return false;
	}
	
	public boolean isAutoAdjustment() {
		return autoAdjustment;
	}
	
	public void setAutoAdjustment(boolean autoAdjustment) {
		this.autoAdjustment = autoAdjustment;
	}
}
