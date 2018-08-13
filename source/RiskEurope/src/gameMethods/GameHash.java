package gameMethods;

import java.util.HashMap;

import guiMethods.GuiTerritory;

public class GameHash {
	private HashMap<Integer,Player> player_hash;
	private int player_num;
	private HashMap<Integer, Territory> terr_hash;
	private int terr_num;
	private HashMap<Territory, GuiTerritory> gui_hash;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GameHash(){
		this.player_hash = new HashMap<Integer, Player>();
		this.player_num = 0;
		this.terr_hash = new HashMap<Integer, Territory>();
		this.terr_num = 0;
		this.gui_hash = new HashMap<Territory, GuiTerritory>();
	}
	
	/***********************
	 * Player Hash Methods *
	 ***********************/
	public void addPlayer(Player p){
		this.player_hash.put(p.getID(), p);
		this.player_num ++;
	}
	
	public Player getPlayer(int id){
		return this.player_hash.get(id);
	}
	
	public int getPlayerNum(){
		return this.player_num;
	}
	
	public Territory getTerritory(int id){
		return this.terr_hash.get(id);
	}
	
	public int getTerritoryNum(){
		return this.terr_num;
	}
	
	public void addGuiTerritory(GuiTerritory g){
		this.gui_hash.put(g.getTerritory(), g);
		this.terr_hash.put(g.getTerritory().getID(), g.getTerritory());
		this.terr_num++;
	}
	
	public GuiTerritory getGuiFromTerritory(Territory t){
		return this.gui_hash.get(t);
	}
}
