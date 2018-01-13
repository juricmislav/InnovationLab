package de.htwg.innovationlab.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Innovation Lab Project 2017/2018
 * HTWG Konstanz, University of Applied Sciences
 *
 * @author Mislav Jurić
 * @version 1.0
 */
public class ConnectionProperties {

	private String ipAddress;
	private String userName;
	private String macAddress;
	private final Path path = Paths.get("Connection.properties");

	public ConnectionProperties() {
		File file = path.toFile();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Could not make conn property file: " + e);
			}
		}
		loadProperties();
	}

	private void loadProperties() {
		try {
			List<String> lines = Files.readAllLines(path);
			if (lines.isEmpty())
				return;
			for (String line : lines) {
				if (line.startsWith("ip_address") && line.contains("=")) {
					String[] pairs = line.split("=");
					if (pairs.length != 2) return;
					ipAddress = pairs[1];
				}
				if (line.startsWith("user_name") && line.contains("=")) {
					String[] pairs = line.split("=");
					if (pairs.length != 2) return;
					userName = pairs[1];
				}
				if (line.startsWith("mac_address") && line.contains("=")) {
					String[] pairs = line.split("=");
					if (pairs.length != 2) return;
					macAddress = pairs[1];
				}
			}
		} catch (IOException e) {
			System.err.println("Could not read from property file: " + e);
		}
	}

	public String getIpAdress() {
		return ipAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMacAddress() {
		return macAddress;
	}
	
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public void saveProperties() {
		
		try (PrintWriter writer = new PrintWriter(
				new OutputStreamWriter(new FileOutputStream(path.toString()), "utf-8"))) {
			StringBuilder sb = new StringBuilder();
			sb.append("ip_address=");
			sb.append(ipAddress);
			writer.println(sb.toString());
			
			sb = new StringBuilder();
			sb.append("user_name=");
			sb.append(userName);
			writer.println(sb.toString());
			
			sb = new StringBuilder();
			sb.append("mac_address=");
			sb.append(macAddress);
			writer.print(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
