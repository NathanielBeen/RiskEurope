package guiMethods;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import gameMethods.CityTerritory;
import unitsCombat.Unit;

public class MapGUI extends JPanel {
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
	private GameGUI gui;
	
	private BufferedImage map;
	private BufferedImage final_map;
	private BufferedImage coin;
	
	private int map_width;
	private int map_height;
	private int map_x;
	private int map_y;
	
	private ArrayList<GuiTerritory> territories = new ArrayList<GuiTerritory>();
	
	private ArrayList<GuiTerritory> terr_options = new ArrayList<GuiTerritory>();
	private GuiTerritory highlighted_terr;
	private GuiTerritory selected_terr;
	private GuiTerritory home_terr;
	private ArrayList<GuiTerritory> tax_terr = new ArrayList<GuiTerritory>();
	
	private ArrayList<Unit> selected_units = new ArrayList<Unit>();
	
	/**************************
	 * Constructor            *
	 **************************/
	public MapGUI(GameGUI gui) {
		this.gui = gui;
		this.map_x = 0;
		this.map_y = 0;
		
		try {
			this.map = ImageIO.read(new File("src/resources/risk_europe_2.png"));
			this.final_map = this.map;
			this.coin = ImageIO.read(new File("src/resources/coins.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.map_width = this.map.getWidth();
		this.map_height = this.map.getHeight();
		
		//territory initialization
		this.highlighted_terr = null;
		this.selected_terr = null;
	}
	
	/**************************
	 * Get and Set Methods    *
	 **************************/
	public void setGuiTerritories(ArrayList<GuiTerritory> a){
		this.territories = a;
	}
	
	public ArrayList<GuiTerritory> getTerritoryOptions(){
		return this.terr_options;
	}
	
	public void setTerritoryOptions(ArrayList<GuiTerritory> a){
		this.terr_options = a;
	}
	
	public GuiTerritory getHighlightedTerritory(){
		return this.highlighted_terr;
	}
	
	public void setHighlightedTerritory(GuiTerritory g){
		this.highlighted_terr = g;
		updateMap();
	}
	
	public GuiTerritory getSelectedTerritory(){
		return this.selected_terr;
	}
	
	public void setSelectedTerritory(GuiTerritory g){
		this.selected_terr = g;
		updateMap();
	}
	
	public GuiTerritory getHomeTerritory(){
		return this.home_terr;
	}
	
	public void setHomeTerritory(GuiTerritory g){
		this.home_terr = g;
		updateMap();
	}
	
	public ArrayList<Unit> getSelectedUnits(){
		return this.selected_units;
	}
	
	public void setSelectedUnits(ArrayList<Unit> a){
		this.selected_units = a;
	}
	
	public ArrayList<GuiTerritory> getSelectedTaxTerritories(){
		return this.tax_terr;
	}
	
	public void setSelectedTaxTerritories(ArrayList<GuiTerritory> a){
		this.tax_terr = a;
	}
	
	/**************************
	 * Overridden Methods     *
	 **************************/
	@Override
	public void paintComponent(Graphics g){
		getCorrectedDimensions();
		BufferedImage map_sub = this.final_map.getSubimage(map_x, map_y, this.getWidth(), this.getHeight());
		g.drawImage(map_sub, 0, 0, null);
	}

	/**************************
	 * Map Move Methods       *
	 **************************/
	public void getCorrectedDimensions(){
		if (map_x + this.getWidth() > map_width){
			map_x = map_width - this.getWidth();
		}
		if (map_x < 0){
			map_x = 0;
		}
		if (map_y + this.getHeight() > map_height){
			map_y = map_height - this.getHeight();
		}
		if (map_y < 0){
			map_y = 0;
		}
	}
	
	public void dragMap(int x, int y){
		this.map_x -= x;
		this.map_y -= y;
		repaint();
	}
	
	/**************************
	 * Selection Methods      *
	 **************************/
	public GuiTerritory getTerritoryFromPoint(Point p){
		Point adj = new Point((int)p.getX()+map_x, (int)p.getY()+map_y);
		for (GuiTerritory t : this.territories){
			if (t.pointinTerritory(adj)){
				return t;
			}
		}
		return null;
	}
	
	/**************************
	 * Reset Methods          *
	 **************************/
	public void resetMap(){
		setHomeTerritory(null);
		setSelectedTerritory(null);
		setSelectedUnits(null);
		setSelectedTaxTerritories(new ArrayList<GuiTerritory>());
		for (GuiTerritory g : this.territories){
			g.resetTerritory();
		}
		repaint();
	}
	
	/**************************
	 * Draw Methods           *
	 **************************/
	public void updateMap(){
		this.final_map = new BufferedImage(map_width, map_height, this.map.getType());
		Graphics2D g = this.final_map.createGraphics();
		g.drawImage(this.map, 0, 0, null);
		g.setComposite(AlphaComposite.SrcOver);
		
		for (GuiTerritory t : this.territories){
			if (t.hasArmy()){
				g.drawImage(t.getArmyIcon(), t.getArmyX(), t.getArmyY(), null);
			}
			if (t.getHasCastle()){
				g.drawImage(this.gui.getGameInfo().getCastleImage(), t.getCenterX(), t.getCenterY(), null);
			}
		}
		if (!this.terr_options.isEmpty()){
			for (GuiTerritory t : this.terr_options){
				BufferedImage option = t.getOptionImg();
				g.drawImage(option, t.getImageX(), t.getImageY(), null);
			}
		}
		if (this.highlighted_terr != null){
			BufferedImage highlight = this.highlighted_terr.getHighlightImage();
			g.drawImage(highlight, this.highlighted_terr.getImageX(), this.highlighted_terr.getImageY(), null);
		}
		if (this.selected_terr != null){
			BufferedImage selected = this.selected_terr.getSelectionImage();
			g.drawImage(selected, this.selected_terr.getImageX(), this.selected_terr.getImageY(), null);
		}
		if (!this.tax_terr.isEmpty()){
			for (GuiTerritory t : this.tax_terr){
				BufferedImage option = t.getHighlightImage();
				g.drawImage(option, t.getImageX(), t.getImageY(), null);
				
				int value = (t.getTerritory() instanceof CityTerritory) ? ((CityTerritory) t.getTerritory()).getValue() : 1;
				BufferedImage coin_img = getCoinImage(value);
				g.drawImage(coin_img, t.getCenterX(), t.getCenterY(), null);
			}
		}
	}
	
	/**************************
	 * Other Methods          *
	 **************************/
	public void moveGuiUnits(GuiTerritory dest){
		GuiTerritory home = getHomeTerritory();
		ArrayList<Unit> units = getSelectedUnits();
		
		home.removeGuiArmy(units, this.gui.getRisk().getCurrentAction().getPlayer());
		dest.addGuiArmy(units, this.gui.getRisk().getCurrentAction().getPlayer());
		
		this.selected_units = null;
	}
	
	public Shape drawArrowHead(Line2D.Double line){
		AffineTransform t = new AffineTransform();
		t.setToIdentity();
		
		Polygon head = new Polygon();
		head.addPoint(-5, -5);
		head.addPoint(-5, 5);
		head.addPoint(5, 0);
		
		double d = (line.y2-line.y1)/(line.x2-line.x1);
		double a = Math.atan(d);
		
		t.rotate(a);
		t.translate(line.x2, line.y2);
		
		return t.createTransformedShape(head);
		
	}
	
	public BufferedImage getCoinImage(int value){
		return this.coin.getSubimage(36*(value-1), 0, 36, 36);
	}
}
