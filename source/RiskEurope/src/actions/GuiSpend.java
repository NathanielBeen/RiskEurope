package actions;

import java.util.ArrayList;

import gameMethods.Territory;
import guiMethods.GameGUI;
import guiMethods.GuiTerritory;
import unitsCombat.Unit;

public class GuiSpend implements GuiAction{
	private Spend s;
	private GameGUI gui;
	private String label;
	
	private GuiTerritory curr_territory;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiSpend(Spend s, GameGUI gui){
		this.s = s;
		this.gui = gui;
		this.label = "";
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void getNextGuiAction(){
		if (s.getPurchasedUnits() == null){
			this.label = "Select units to purchase";
		}
		else if (s.getNumCastles() != 0){
			this.label = "Select a territory to place a castle in";
		}
		else if (s.genUnitOptions().isEmpty()){
			this.label = "Confirm Placement";
		}
		else if (s.getTerritories().size() == s.getNumPlaced()){
			this.label = "Select a territory to place units";
		}
		else if (s.getTerritories().size() != s.getNumPlaced()){
			this.label = "Select units to place in "+s.getTerritories().get(s.getNumPlaced()).getName();
		}
		else{
			this.label = "Confirm action";
		}
		s.getNextAction();
		this.gui.changeGuiState(this.label);
	}
	
	@Override
	public void selectGuiOption(Object o){
		Object a = null;
		if (s.getPurchasedUnits() == null && o instanceof int[]){
			int[] units = (int[]) o;
			s.setNumCastles(units[4]);
			int[] count = {units[0], units[1], units[2], units[3]};
			
			ArrayList<Unit> au = s.getPlayer().getReserveUnitsByCount(count);
			this.gui.getMap().setSelectedUnits(au);
			a = au;
		}
		else if (s.getNumCastles() != 0 && o instanceof GuiTerritory){
			a = ((GuiTerritory) o).getTerritory();
		}
		else if (s.getTerritories().size() == s.getNumPlaced() && o instanceof GuiTerritory){
			this.curr_territory = (GuiTerritory) o;
			a = ((GuiTerritory) o).getTerritory();
		}
		else if (s.getTerritories().size() != s.getNumPlaced() && o instanceof int[]){
			ArrayList<Unit> au = s.getPurchasedUnitsByCount((int[]) o);
			this.curr_territory.addGuiArmy(au, s.getPlayer());
			a = au;
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
		getAction().selectOption(a);
	}
	
	@Override
	public void resetGuiAction(){
		s.resetAction();
		this.gui.getMap().resetMap();
		getNextGuiAction();
	}
	
	@Override
	public Action getAction(){
		return this.s;
	}
}
