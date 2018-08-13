package guiMethods;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import gameMethods.Card;

public class CardGUI extends JPanel{

	private static final long serialVersionUID = 1L;
	private Card card;
	private GameGUI gui;
	private BufferedImage img;
	private BufferedImage select_img;
	private BufferedImage highlight_img;
	private BufferedImage ordered_card_img;
	
	private int action_highlighted;
	
	private boolean highlight;
	private boolean first_selected;
	private boolean second_selected;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public CardGUI(Card card, GameGUI gui){
		this.card = card;
		this.gui = gui;
		
		this.img = genCardImage();
		
		
		this.highlight = false;
		this.first_selected = false;
		this.second_selected = false;
		
		this.action_highlighted = 0;
		
		try{
			this.select_img = ImageIO.read(getClass().getResource("/resources/card_select.png"));
			this.highlight_img = ImageIO.read(getClass().getResource("/resources/card_highlight.png"));
			this.ordered_card_img = ImageIO.read(getClass().getResource("/resources/ordered_card_select.png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public Card getCard(){
		return this.card;
	}
	
	public BufferedImage getImage(){
		if (this.highlight){
			return this.highlight_img;
		}
		else if (this.first_selected || this.second_selected){
			return this.select_img;
		}
		else{
			return this.img;
		}
	}
	
	public void setIsFirstSelected(boolean b){
		this.first_selected = b;
	}
	
	public void setIsSecondSelected(boolean b){
		this.second_selected = b;
	}
	
	public void setIsHighlighted(boolean b){
		this.highlight = b;
	}
	
	public int getActionHighlight(){
		return this.action_highlighted;
	}
	
	public void setActionHighlight(int i){
		this.action_highlighted = i;
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void paintComponent(Graphics g){
		BufferedImage final_img = new BufferedImage(this.img.getWidth(), this.img.getHeight(), this.img.getType());
		Graphics2D g2 = final_img.createGraphics();
		g2.drawImage(this.img, 0, 0, null);
		g2.setComposite(AlphaComposite.SrcOver);
		
		if (this.first_selected){
			g2.drawImage(this.ordered_card_img.getSubimage(0, 0, 150, 200), 0, 0, null);
		}
		else if (this.second_selected){
			g2.drawImage(this.ordered_card_img.getSubimage(0, 200, 150, 200), 0, 0, null);
		}
		else if(this.highlight){
			g2.drawImage(this.highlight_img, 0, 0, null);
		}
		g.drawImage(final_img, 0, 0, null);

	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(this.img.getWidth(), this.img.getHeight());
	}
	
	/***********************
	 * Image Methods       *
	 ***********************/
	public BufferedImage genCardImage(){
		BufferedImage img = new BufferedImage(150, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.drawImage(this.gui.getGameInfo().getCardBackImage(), 0, 0, null);
		
		int f = this.card.getFreeAction();
		int u = this.card.getUpperAction();
		int l = this.card.getLowerAction();
		g.setComposite(AlphaComposite.SrcOver);
		
		if (f != 0){
			BufferedImage free = this.gui.getGameInfo().getActionImage().getSubimage(0, (f-1)*60, 130, 60);
			g.drawImage(free, 10, 10, null);
		}
		BufferedImage upper = this.gui.getGameInfo().getActionImage().getSubimage(0, (u-1)*60, 130, 60);
		BufferedImage lower = this.gui.getGameInfo().getActionImage().getSubimage(0, (l-1)*60, 130, 60);
		g.drawImage(upper, 10, 70, null);
		g.drawImage(lower, 10, 130, null);
		
		return img;
	}
}
