package actions;

import java.util.ArrayList;

import guiMethods.GameGUI;
import guiMethods.GuiTerritory;
import unitsCombat.Unit;

public class GuiPlacement implements GuiAction{
	private Placement p;
	private GameGUI gui;
	private String label;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiPlacement(Placement p, GameGUI gui){
		this.p = p;
		this.gui = gui;
		this.label = "";
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void getNextGuiAction(){
		if (p.getStart() == null){
			this.label = "Select a Gold Territory to start in";
		}
		else if (p.getStartUnits() == null){
			this.label = "Select units to place in the starting territory";
		}
		else if (p.getSecond() == null){
			this.label = "select an adjacent territory to start in";
		}
		else{
			this.label = "Confirm action";
		}
		p.getNextAction();
		this.gui.changeGuiState(this.label);
	}
	
	@Override
	public void selectGuiOption(Object o){
		Object a = null;
		if (p.getStart() == null && o instanceof GuiTerritory){
			this.gui.getMap().setHomeTerritory((GuiTerritory) o);
			a = ((GuiTerritory) o).getTerritory();
		}
		else if (p.getStartUnits() == null && o instanceof int[]){
			ArrayList<Unit> au = this.p.getPlayer().getReserveUnitsByCount((int[]) o);
			this.gui.getMap().getHomeTerritory().addGuiArmy(au, p.getPlayer());
			this.gui.getMap().getHomeTerritory().setHasCastle(true);
			a = au;
		}
		else if (p.getSecond() == null && o instanceof GuiTerritory){
			((GuiTerritory) o).addGuiArmy(p.getSecondUnits(), p.getPlayer());
			a = ((GuiTerritory) o).getTerritory();
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
		getAction().selectOption(a);
	}
	
	@Override
	public void resetGuiAction(){
		this.gui.getMap().resetMap();
		p.resetAction();
		getNextGuiAction();
	}
	
	@Override
	public Action getAction(){
		return this.p;
	}
}
