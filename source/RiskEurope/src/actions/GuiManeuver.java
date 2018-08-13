package actions;

import java.util.ArrayList;

import guiMethods.GameGUI;
import guiMethods.GuiTerritory;
import unitsCombat.Unit;

public class GuiManeuver implements GuiAction{
	private Maneuver m;
	private GameGUI gui;
	private String label;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiManeuver(Maneuver m, GameGUI gui){
		this.m = m;
		this.gui = gui;
		this.label = "";
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void getNextGuiAction(){
		if (m.getHome() == null){
			this.label = "Select a territory to manuever from";
		}
		else if (m.getUnits() == null){
			this.label = "Select units to manuever from "+m.getHome().getName();
		}
		else if (m.getDestination() == null){
			this.label = "Select a destination territory";
		}
		else{
			this.label = "Confirm action";
		}
		m.getNextAction();
		this.gui.changeGuiState(this.label);
	}
	
	@Override
	public void selectGuiOption(Object o){
		Object a = null;
		if (m.getHome() == null && o instanceof GuiTerritory){
			this.gui.getMap().setHomeTerritory((GuiTerritory) o);
			a = ((GuiTerritory) o).getTerritory();
		}
		else if (m.getUnits() == null && o instanceof int[]){
			ArrayList<Unit> units = this.gui.getRisk().convertCountToTerritoryUnits(m.getPlayer(), m.getHome(), (int[]) o);
			this.gui.getMap().setSelectedUnits(units);
			a = units;
		}
		else if (m.getDestination() == null && o instanceof GuiTerritory){
			this.gui.getMap().moveGuiUnits((GuiTerritory) o);
			a = ((GuiTerritory) o).getTerritory();
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
		getAction().selectOption(a);
	}
	
	@Override
	public void resetGuiAction(){
		this.gui.getMap().resetMap();
		m.resetAction();
		getNextGuiAction();
	}
	
	@Override
	public Action getAction(){
		return m;
	}
}
