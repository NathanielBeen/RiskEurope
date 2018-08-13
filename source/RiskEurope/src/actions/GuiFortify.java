package actions;

import java.util.ArrayList;

import guiMethods.GameGUI;
import guiMethods.GuiTerritory;
import unitsCombat.Unit;

public class GuiFortify implements GuiAction{
	private Fortify f;
	private GameGUI gui;
	private String label;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiFortify(Fortify f, GameGUI gui){
		this.f = f;
		this.gui = gui;
		this.label = "";
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void getNextGuiAction(){
		if (f.getHome() == null){
			this.label = "Select a city or castle to fortify";
		}
		else{
			this.label = "Confrim action";
		}
		f.getNextAction();
		this.gui.changeGuiState(this.label);
	}
	
	@Override
	public void selectGuiOption(Object o){
		Object a = null;
		if (f.getHome() == null && o instanceof GuiTerritory){
			GuiTerritory g = (GuiTerritory) o;
			this.gui.getMap().setHomeTerritory(g);
			ArrayList<Unit> au = f.getPlayer().getReserveUnitsByCount(f.getCount(g.getTerritory()));
			(g).addGuiArmy(au, f.getPlayer());
			a = ((GuiTerritory) o).getTerritory();
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
		getAction().selectOption(a);
	}
	
	@Override
	public void resetGuiAction(){
		this.gui.getMap().resetMap();
		f.resetAction();
		getNextGuiAction();
	}
	
	@Override
	public Action getAction(){
		return this.f;
	}
}
