package actions;

import java.util.ArrayList;

import gameMethods.CityTerritory;
import gameMethods.Territory;
import guiMethods.GameGUI;
import guiMethods.GuiTerritory;

public class GuiTax implements GuiAction{
	private Tax t;
	private GameGUI gui;
	private String label;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiTax(Tax t, GameGUI gui){
		this.t = t;
		this.gui = gui;
		this.label = "";
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void getNextGuiAction(){
		if (t.getHome() == null){
			this.label = "Select a city to tax";
		}
		else{
			this.label = "Confirm action";
		}
		t.getNextAction();
		this.gui.changeGuiState(this.label);
	}
	
	@Override
	public void selectGuiOption(Object o){
		Object a = null;
		if (t.getHome() == null && o instanceof GuiTerritory){
			this.gui.getMap().setHomeTerritory((GuiTerritory) o);
			a = ((GuiTerritory) o).getTerritory();
			if (a instanceof CityTerritory){
				ArrayList<Territory> terr = ((CityTerritory) a).genTaxTerritories();
				ArrayList<GuiTerritory> gui_tax = new ArrayList<GuiTerritory>();
				
				for (Territory t : terr){
					gui_tax.add(this.gui.getHash().getGuiFromTerritory(t));
				}
				
				this.gui.getMap().setSelectedTaxTerritories(gui_tax);
			}
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
		getAction().selectOption(a);
		this.gui.repaint();
	}
	
	@Override
	public void resetGuiAction(){
		this.gui.getMap().resetMap();
		t.resetAction();
		getNextGuiAction();
	}
	
	@Override
	public Action getAction(){
		return this.t;
	}
}
