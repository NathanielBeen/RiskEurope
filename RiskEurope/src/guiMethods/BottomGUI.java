package guiMethods;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import actions.*;
import gameMethods.Player;

public class BottomGUI extends JPanel {
	private GameGUI gui;
	private String action_str;
	private JTextArea text;
	
	private BufferedImage bottom_img;
	private BufferedImage action_img;
	private BufferedImage unit_img;

	/*********************
	 * Constructor       *
	 *********************/
	public BottomGUI(GameGUI gui) {
		this.gui = gui;
		this.action_str = "";
		
		try{
			this.bottom_img = ImageIO.read(new File("src/resources/bottom_panel.png"));
			this.action_img = ImageIO.read(new File("src/resources/current_action_icons.png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		this.unit_img = this.gui.getGameInfo().getMapUnitImage();
		
		setLayout(null);
		text = new JTextArea(action_str);
		text.setEditable(false);
		text.setLineWrap(true);
		text.setBounds(0,0,0,0);
		add(text);
	}
	
	/**********************
	 * Painting Functions *
	 **********************/
	@Override 
	public void paintComponent(Graphics g){
		Player curr_player = this.gui.getRisk().getCurrentPlayer();
		if (curr_player == null){
			curr_player = this.gui.getRisk().getPlayers().get(0);
		}
		BufferedImage back = this.gui.getGameInfo().getBackByPlayerID(curr_player.getID());;
		BufferedImage corr_back = new BufferedImage(getWidth(), getHeight(), back.getType());
		
		Graphics2D g2 = corr_back.createGraphics();
		g2.drawImage(back.getSubimage(0, 0, getWidth(), getHeight()), 0, 0, null);
		g2.setComposite(AlphaComposite.SrcOver);
		
		int image_x = (getWidth()-this.bottom_img.getWidth())/2;
		int image_y = (getHeight()-this.bottom_img.getHeight())/2;
		if (image_x < 0 || image_y < 0){
			image_x = 0;
			image_y = 0;
		}
		g2.drawImage(this.bottom_img, image_x, image_y, null);
		
		Action current_action = this.gui.getRisk().getCurrentAction();
		int action_y = convActionTypeToInt(current_action)*76;
		g2.drawImage(this.action_img.getSubimage(0, action_y, 157, 76), image_x+406, image_y+5, null);
		
		Font f = new Font("Arial", Font.BOLD, 20);
		g2.setFont(f);
		g2.setColor(Color.BLACK);
		
		int unit_x = 123+image_x;
		int unit_y = 9+image_y;
		for (int i = 0; i < 4; i++){
			BufferedImage unit = getUnitImage(i, curr_player);
			BufferedImage num = getNumberImage(curr_player.getReserveCount()[i]);
			g2.drawImage(unit, unit_x, unit_y, null);
			g2.drawImage(num, unit_x, unit_y+36, null);
			unit_x += 36;
		}
		
		g.drawImage(corr_back, 0, 0, null);
		
		int text_x = 572+image_x;
		int text_y = 9+image_y;
		text.setText(this.action_str);
		text.setBounds(text_x, text_y, 176, 68);
		revalidate();
	}
	
	public int convActionTypeToInt(Action a){
		if (a == null){
			return Action.NONE;
		}
		else if (a instanceof Expand){
			return Action.EXPAND;
		}
		else if (a instanceof Maneuver){
			return Action.MANEUVER;
		}
		else if (a instanceof SplitExpand){
			return Action.SPLIT_EXPAND;
		}
		else if (a instanceof Tax){
			return Action.TAX;
		}
		else if (a instanceof Spend){
			return Action.SPEND;
		}
		else if (a instanceof KingMe){
			return Action.KING_ME;
		}
		else if (a instanceof Fortify){
			return Action.FORTIFY;
		}
		else if (a instanceof SiegeAssault){
			return Action.SIEGE_ASSAULT;
		}
		else if (a instanceof Placement){
			return Action.PLACEMENT;
		}
		else{
			return -1;
		}
	}

	public BufferedImage getUnitImage(int type, Player p){
		return this.unit_img.getSubimage(type*32, p.getID()*32, 32, 32);
	}
	
	public BufferedImage getNumberImage(int num){
		BufferedImage img = new BufferedImage(32,32,this.bottom_img.getType());
		Graphics2D g = img.createGraphics();
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		Font f = new Font("Arial", Font.BOLD, 20);
		g.setFont(f);
		g.setColor(Color.BLACK);
		
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector c = g.getFont().createGlyphVector(frc, Integer.toString(num));
		Rectangle2D r = c.getVisualBounds();
		int r_x = (int)(32-r.getWidth())/2;
		int r_y = (int)(32-r.getHeight()/2);
		
		g.drawString(Integer.toString(num), r_x, r_y);
		return img;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public void setActionString(String s){
		this.action_str = s;
	}
}
