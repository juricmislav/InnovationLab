package de.htwg.innovationlab.gui.frames;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AuthenticationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public AuthenticationFrame() {
		setTitle("Authentication");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		Dimension screenDim = getToolkit().getScreenSize();
		setLocation(screenDim.width / 4, screenDim.height / 4);
		
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
			image = ImageIO.read(Paths.get("resources/pushlink_image.png").toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
}
