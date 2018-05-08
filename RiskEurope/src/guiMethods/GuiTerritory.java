package guiMethods;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.Territory;
import unitsCombat.Unit;

public class GuiTerritory {
	private GameInfo info;
	private Territory territory;
	
	private Player owner;
	private Player attacker;
	
	private ArrayList<Unit> attacker_units;
	private ArrayList<Unit> defender_units;
	private BufferedImage army_icon;
	private BufferedImage selected_army_icon;
	
	private BufferedImage terr_img;
	private BufferedImage option_img;
	private BufferedImage highlight_img;
	private BufferedImage select_img;
	private int img_x;
	private int img_y;
	private int center_x;
	private int center_y;
	private int army_x;
	private int army_y;
	private boolean has_castle;
	
	private Polygon bounds;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiTerritory(GameInfo info, Territory t, BufferedImage img, int img_x, int img_y, 
						int center_x, int center_y, int army_x, int army_y, Polygon bounds){
		this.info = info;
		this.territory = t;
		
		this.owner = t.getOwner();
		this.attacker = t.getAttacker();
		
		this.attacker_units = new ArrayList<Unit>(t.getAttackingArmy());
		this.defender_units = new ArrayList<Unit>(t.getDefendingArmy());
		this.army_icon = genCombinedImage(false);
		this.selected_army_icon = genCombinedImage(true);
		
		this.terr_img = img;
		this.option_img = genOptionImage();
		this.highlight_img = genHighlightImage();
		this.select_img = genSelectionImage();
		this.img_x = img_x;
		this.img_y = img_y;
		this.center_x = center_x;
		this.center_y = center_y;
		this.army_x = army_x;
		this.army_y = army_y;
		this.has_castle = t.hasCastle();
		
		this.bounds = bounds;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public Territory getTerritory(){
		return this.territory;
	}
	
	public boolean hasArmy(){
		if (this.attacker_units.isEmpty() && this.defender_units.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}
	
	public BufferedImage getArmyIcon(){
		return this.army_icon;
	}
	
	public BufferedImage getSelectedArmyIcon(){
		return this.selected_army_icon;
	}
	
	public BufferedImage getTerrImg(){
		return this.terr_img;
	}
	
	public BufferedImage getOptionImg(){
		return this.option_img;
	}
	
	public BufferedImage getHighlightImage(){
		return this.highlight_img;
	}
	
	public BufferedImage getSelectionImage(){
		return this.select_img;
	}
	
	public boolean pointinTerritory(Point p){
		if (this.bounds.contains(p)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int getImageX(){
		return this.img_x;
	}
	
	public int getImageY(){
		return this.img_y;
	}
	
	public int getCenterX(){
		return this.center_x;
	}
	
	public int getCenterY(){
		return this.center_y;
	}
	
	public int getArmyX(){
		return this.army_x;
	}
	
	public int getArmyY(){
		return this.army_y;
	}
	
	public boolean getHasCastle(){
		return this.has_castle;
	}
	
	public void setHasCastle(boolean b){
		this.has_castle = b;
	}
	
	public Polygon getBounds(){
		return this.bounds;
	}
	/***********************
	 * Image Refresh       *
	 ***********************/
	public void resetTerritory(){
		this.owner = this.territory.getOwner();
		this.attacker = this.territory.getAttacker();
		this.attacker_units = new ArrayList<Unit>(this.territory.getAttackingArmy());
		this.defender_units = new ArrayList<Unit>(this.territory.getDefendingArmy());
		this.has_castle = this.territory.hasCastle();
		
		updateArmyIcons();
	}
	
	public void updateArmyIcons(){
		this.army_icon = genCombinedImage(false);
		this.selected_army_icon = genCombinedImage(true);
	}
	
	/***********************
	 * Image Creation      *
	 ***********************/
	public BufferedImage genCombinedImage(boolean selected){
		BufferedImage img = new BufferedImage(128, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		if (!this.defender_units.isEmpty()){
			int[] count = genUnitCount(this.defender_units);
			BufferedImage def = armyImage(count, this.owner.getID(), selected);
			g.setComposite(AlphaComposite.Src);
			g.drawImage(def, 0, 0, null);
		}
		if (!this.attacker_units.isEmpty()){
			int[] count = genUnitCount(this.attacker_units);
			BufferedImage att = armyImage(count, this.attacker.getID(), selected);
			g.setComposite(AlphaComposite.Src);
			g.drawImage(att, 64, 0, null);
		}
		
		return img;
	}
	
	public int[] genUnitCount(ArrayList<Unit> army){
		int[] count = new int[4];
		for (Unit u : army){
			count[u.getType()]++;
		}
		return count;
	}
	
	public BufferedImage armyImage(int[] count, int id, boolean selected){
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setComposite(AlphaComposite.Src);
		
		int x = 0;
		int y = 0;
		int i = 0;
		for (int n : count){
			if (n != 0){
				int x_cor = 32*i;
				if (selected){
					x_cor += 128;
				}
				BufferedImage unit_icon = this.info.getMapUnitImage().getSubimage(x_cor, 32*id, 32, 32);
				g.drawImage(unit_icon, x, y, null);
				g.setComposite(AlphaComposite.SrcOver);
				if (n > 10){
					n = 10;
				}
				BufferedImage num_icon = this.info.getMapUnitImage().getSubimage((n-1)*32, 128, 32, 32);
				g.drawImage(num_icon, x, y, null);
				x += 32;
				if (x > 32){
					x = 0;
					y += 32;
				}
			}
			i++;
		}
		return img;
	}
	
	/***********************
	 * Army Add/Remove     *
	 ***********************/
	public void addGuiArmy(ArrayList<Unit> u, Player p){
		if (p.equals(this.owner) || this.owner == null){
			this.owner = p;
			this.defender_units.addAll(u);
			updateArmyIcons();
		}
		else if (p.equals(this.attacker)){
			this.attacker_units.addAll(u);
			updateArmyIcons();
		}
		else if (this.attacker == null){
			this.attacker = p;
			this.attacker_units = u;
			updateArmyIcons();
		}
		else{
			System.out.print("error in adding Gui army");
		}
	}
	
	public void removeGuiArmy(ArrayList<Unit> u, Player p){
		if (p.equals(this.owner)){
			this.defender_units.removeAll(u);
			updateArmyIcons();
		}
		else if (p.equals(this.attacker)){
			this.attacker_units.removeAll(u);
			updateArmyIcons();
		}
		else{
			System.out.print("error in deleting gui army");
		}
	}
	
	/**************************
	 * Terr Image Methods     *
	 **************************/
	public BufferedImage colorImage(int rgb){
		BufferedImage img = new BufferedImage(this.terr_img.getWidth(), this.terr_img.getHeight(), this.terr_img.getType());
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(this.terr_img, 0, 0, null);
		
		int r = (rgb>>16) & 0xff;
		int g = (rgb>>8) & 0xff;
		int b = rgb & 0xff;
		
		for (int y = 0; y < img.getHeight(); y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x, y);
				int a = (p>>24) & 0xff;
				int new_p = (a<<24) | (r<<16) | (g<<8) | b;
				img.setRGB(x, y, new_p);
			}
		}
		
		return img;
	}
	
	public BufferedImage genOptionImage(){
		int rgb = new Color(127,201,255).getRGB();
		return colorImage(rgb);
	}
	
	public BufferedImage genHighlightImage(){
		int rgb = new Color(192,192,192).getRGB();
		return colorImage(rgb);
	}
	
	public BufferedImage genSelectionImage(){
		int rgb = new Color(64,64,64).getRGB();
		return colorImage(rgb);
	}
}
