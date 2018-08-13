package actions;

import java.util.ArrayList;

import guiMethods.AssualtCombatGUI;
import guiMethods.GameGUI;
import guiMethods.GuiTerritory;

public class GuiSiegeAssault implements GuiAction{
	private SiegeAssault s;
	private GameGUI gui;
	private String label;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiSiegeAssault(SiegeAssault s, GameGUI gui){
		this.s = s;
		this.gui = gui;
		this.label = "";
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void getNextGuiAction(){
		if (s.getHome() == null){
			this.label = "select a territory with a siege unit";
		}
		else if (s.getDestination() == null){
			this.label = "select a territory to attack";
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
		if (s.getHome() == null && o instanceof GuiTerritory){
			this.gui.getMap().setHomeTerritory((GuiTerritory) o);
			a = ((GuiTerritory) o).getTerritory();
			getAction().selectOption(a);
		}
		else if (s.getDestination() == null && o instanceof GuiTerritory){
			a = ((GuiTerritory) o).getTerritory();
			getAction().selectOption(a);
			AssualtCombatGUI cgui = new AssualtCombatGUI(this.gui, this, s.getDestination(), s.getNumSiege());
			cgui.setVisible(true);
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
	}
	
	@Override
	public void resetGuiAction(){
		this.gui.getMap().resetMap();
		s.resetAction();
		getNextGuiAction();
	}
	
	@Override
	public Action getAction(){
		return this.s;
	}
}
