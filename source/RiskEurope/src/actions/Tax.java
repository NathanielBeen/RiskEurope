package actions;

import java.util.ArrayList;

import gameMethods.CityTerritory;
import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.RiskEurope;
import gameMethods.Territory;

public class Tax implements Action{
	
	private RiskEurope risk;
	private GameInfo info;
	
	private Player player;
	private Territory home;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Tax(RiskEurope risk, Player p){
		this.risk = risk;
		this.info = risk.getInfo();
		this.player = p;
		
		this.home = null;
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public Player getPlayer(){
		return this.player;
	}
	
	@Override
	public void getNextAction(){
		if (this.home == null){
			this.info.setSelectionState(GameInfo.SELECT_TAX);
			this.risk.setCurrentOptions(genHomeOptions());
			//set gui to display other territories on hover
		}
		else{
			this.info.setSelectionState(GameInfo.CONFIRM_ACTION);
		}
	}
	
	@Override
	public void selectOption(Object o){
		if (this.home == null && o instanceof Territory){
			this.home = (Territory) o;
		}
	}
	
	@Override
	public void resolveAction(){
		ArrayList<Territory> taxed = ((CityTerritory) this.home).genTaxTerritories();
		int income = 0;
		
		for (Territory t : taxed){
			if (t instanceof CityTerritory){
				income += ((CityTerritory)t).getValue();
			}
			else{
				income ++;
			}
		}
		
		this.player.setCoins(this.player.getCoins() + income);
		
		this.risk.getNextAction();
	}
	
	@Override
	public void resetAction(){
		this.home = null;
	}
	
	/***********************
	 * Options Methods     *
	 ***********************/
	public ArrayList<Object> genHomeOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t: this.player.getOwnedTerritories()){
			if (t instanceof CityTerritory && !t.IsDisputed()){
				o.add(t);
			}
		}
		return o;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public Territory getHome(){
		return this.home;
	}
}
