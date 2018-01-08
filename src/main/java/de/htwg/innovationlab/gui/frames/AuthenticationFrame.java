package de.htwg.innovationlab.gui.frames;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.htwg.innovationlab.gui.SmartBulb;

public class AuthenticationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private URL imagePath = SmartBulb.class.getResource("/pushlink_image.png");
	
	public AuthenticationFrame(SmartBulb smartBulb) {		
		setTitle("Authentication");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		Dimension screenDim = getToolkit().getScreenSize();
		setLocation(screenDim.width / 4, screenDim.height / 4);
		setIconImage(smartBulb.getIcon().getImage());
		
		Image image = getImage();
		setSize(image.getWidth(this), image.getHeight(this));
		setResizable(false);
		
		add(new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, this);
			}
		});
	}
	
	private Image getImage() {
		Image image = null;
		try {
			image = ImageIO.read(imagePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
}
