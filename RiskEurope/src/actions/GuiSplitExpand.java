package actions;

import java.util.ArrayList;

import guiMethods.GameGUI;
import guiMethods.GuiTerritory;
import unitsCombat.Unit;

public class GuiSplitExpand implements GuiAction{
	private SplitExpand e;
	private GameGUI gui;
	private String label;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiSplitExpand(SplitExpand e, GameGUI gui){
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
			this.label = "Select a territory to split expand from";
		}
		else if (e.getFirstUnits() == null){
			this.label = "Select the first units to move from "+e.getHome().getName();
		}
		else if (e.getFirstDestination() == null){
			this.label = "Select a destination to move the frist units to";
		}
		else if (e.getSecondUnits() == null){
			this.label = "Select the second units to move from "+e.getHome().getName();
		}
		else if (e.getSecondDestination() == null){
			this.label = "Select a second detination to move the second units to";
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
		else if (e.getFirstUnits() == null && o instanceof int[]){
			ArrayList<Unit> units = this.e.getUnitsByCount((int[]) o);
			this.gui.getMap().setSelectedUnits(units);
			a = units;
		}
		else if (e.getFirstDestination() == null && o instanceof GuiTerritory){
			this.gui.getMap().moveGuiUnits((GuiTerritory) o);
			this.gui.getMap().setSelectedUnits(null);
			a = ((GuiTerritory) o).getTerritory();
		}
		else if (e.getSecondUnits() == null && o instanceof int[]){
			ArrayList<Unit> units = this.e.getUnitsByCount((int[]) o);
			this.gui.getMap().setSelectedUnits(units);
			a = units;
		}
		else if (e.getSecondDestination() == null && o instanceof GuiTerritory){
			this.gui.getMap().moveGuiUnits((GuiTerritory) o);
			this.gui.getMap().setSelectedUnits(null);
			a = ((GuiTerritory) o).getTerritory();
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
		e.selectOption(a);
		this.gui.repaint();
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
