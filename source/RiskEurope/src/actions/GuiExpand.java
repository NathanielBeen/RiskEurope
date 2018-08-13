	package actions;

import java.util.ArrayList;

import guiMethods.GameGUI;
import guiMethods.GuiTerritory;
import unitsCombat.Unit;

public class GuiExpand implements GuiAction{
	private Expand e;
	private GameGUI gui;
	private String label;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiExpand(Expand e, GameGUI gui){
		this.e = e;
		this.gui = gui;
		this.label = "";
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void getNextGuiAction(){
		if (e.getHome() == null){
			this.label = "Select a territory to expand from";
		}
		else if (e.getUnits() == null){
			this.label = "Select units to move from "+e.getHome().getName();
		}
		else if (e.getDestination() == null){
			this.label = "Select a destination territory";
		}
		else{
			this.label = "Confirm action";
		}
		e.getNextAction();
		this.gui.changeGuiState(this.label);
	}
	
	@Override
	public void selectGuiOption(Object o){
		Object a = null;
		if (e.getHome() == null && o instanceof GuiTerritory){
			this.gui.getMap().setHomeTerritory((GuiTerritory) o);
			a = ((GuiTerritory) o).getTerritory();
		}
		else if (e.getUnits() == null && o instanceof int[]){
			ArrayList<Unit> au = this.gui.getRisk().convertCountToTerritoryUnits(e.getPlayer(), e.getHome(), (int[]) o);
			this.gui.getMap().setSelectedUnits(au);
			a = au;
		}
		else if (e.getDestination() == null && o instanceof GuiTerritory){
			this.gui.getMap().moveGuiUnits((GuiTerritory) o);
			this.gui.getMap().setSelectedUnits(null);
			a = ((GuiTerritory) o).getTerritory();
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
		getAction().selectOption(a);
	}

	@Override
	public void resetGuiAction(){
		this.gui.getMap().resetMap();
		e.resetAction();
		getNextGuiAction();
	}
	
	@Override
	public Action getAction(){
		return this.e;
	}
}
