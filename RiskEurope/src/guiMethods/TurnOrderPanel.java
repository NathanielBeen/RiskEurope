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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import gameMethods.Player;

public class TurnOrderPanel extends JPanel{
	private ArrayList<Player> turn_order;
	private SideGUI gui;
	
	BufferedImage nameplates;
	BufferedImage turn_order_icon;
	BufferedImage current_turn_order_icon;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public TurnOrderPanel(ArrayList<Player> turn_order, SideGUI gui){
		this.turn_order = turn_order;
		this.gui = gui;
		
		try{
			this.nameplates = ImageIO.read(new File("src/resources/nameplates.png"));
			this.turn_order_icon = ImageIO.read(new File("src/resources/turn_order_icon.png"));
			this.current_turn_order_icon = ImageIO.read(new File("src/resources/current_turn_order_icon.png"));
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void paintComponent(Graphics g){
		BufferedImage back = this.gui.getGui().getGameInfo().getBlackBack();
		BufferedImage corr_back = back.getSubimage(0, 0, getWidth(), getHeight());
		g.drawImage(corr_back, 0, 0, null);
		if (!this.turn_order.isEmpty()){
			int x = 10;
			int y = 10;
			for (Player p : this.turn_order){
				BufferedImage player_icon = genPlayerIcon(p, getWidth()-20, (getHeight()-50)/4);
				g.drawImage(player_icon, x, y, null);
				y += ((getHeight()-50)/4)+10;
			}
		}
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public BufferedImage genPlayerIcon(Player p, int width, int height){
		if (width < 0 ){
			width = 1;
		}
		if (height < 0){
			height = 1;
		}
		BufferedImage back = this.gui.getGui().getGameInfo().getBackByPlayerID(p.getID());
		BufferedImage corr_back = new BufferedImage(width, height, back.getType());
		
		Graphics2D g = corr_back.createGraphics();
		g.drawImage(back.getSubimage(0, 0, width, height), 0, 0, null);
		g.setComposite(AlphaComposite.SrcOver);
		
		int x_d = (int)(width-144)/2;
		int y_d = (int)(height - 79)/2;
		if (p.equals(this.gui.getGui().getRisk().getCurrentPlayer())){
			g.drawImage(this.current_turn_order_icon,x_d,y_d,null);
		}
		else{
			g.drawImage(this.turn_order_icon,x_d,y_d,null);
		}
		
		BufferedImage name = this.nameplates.getSubimage(p.getID()*102+4, 4, 94, 67);
		g.drawImage(name, x_d+44, y_d+6, null);
		
		int coin = p.getCoins();
		int crowns = p.getCrowns();
		
		Font f = new Font("Arial", Font.BOLD, 20);
		g.setFont(f);
		g.setColor(Color.BLACK);
		
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector c = f.createGlyphVector(frc, Integer.toString(coin));
		Rectangle2D cb = c.getVisualBounds();
		int box_x = (int) cb.getWidth()/2;
		int box_y = (int) cb.getHeight()/2;
		
		int coin_x = (int)(20 - cb.getX()-box_x+x_d);
		int coin_y = (int)(20 - cb.getY()-box_y+y_d);
		g.drawString(Integer.toString(coin), coin_x, coin_y);
		
		c = f.createGlyphVector(frc, Integer.toString(crowns));
		cb = c.getVisualBounds();
		
		box_x = (int) cb.getWidth()/2;
		box_y = (int) cb.getHeight()/2;
		int crown_x = (int)(20-cb.getX()-box_x+x_d);
		int crown_y = (int)(59-cb.getY()-box_y+y_d);
		g.drawString(Integer.toString(crowns), crown_x, crown_y);
		
		return corr_back;
	}
	
	public void setTurnOrder(ArrayList<Player> a){
		this.turn_order = a;
	}
}
