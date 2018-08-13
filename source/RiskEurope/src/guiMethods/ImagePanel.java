package guiMethods;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private BufferedImage back;

	public ImagePanel(BufferedImage img) {
		this.back = img;
	}
	
	@Override
	public void paintComponent(Graphics g){
		BufferedImage corr_back = this.back.getSubimage(0, 0, getWidth(), getHeight());
		g.drawImage(corr_back, 0, 0, null);
	}
	
	public void setImage(BufferedImage img){
		this.back = img;
	}
}
