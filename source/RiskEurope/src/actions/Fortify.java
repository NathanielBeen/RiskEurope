package actions;

import java.util.ArrayList;

import gameMethods.CityTerritory;
import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.RiskEurope;
import gameMethods.Territory;

public class Fortify implements Action{
	private RiskEurope risk;
	private GameInfo info;
	
	private Player player;
	private Territory home;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Fortify(RiskEurope risk, Player p){
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
			this.info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genHomeOptions());
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
		int[] count = getCount(this.home);
		this.home.addArmy(this.player.removeReserveUnitsByCount(count));
		this.risk.getNextAction();
	}
	
	@Override
	public void resetAction(){
		this.home = null;
	}
	
	/***********************
	 * Option Methods      *
	 ***********************/
	public ArrayList<Object> genHomeOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t : this.player.getOwnedTerritories()){
			if (t instanceof CityTerritory || t.hasCastle()){
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
	
	public int[] getCount(Territory t){
		if (t.hasCastle()){
			return new int[]{4,0,0,0};
		}
		
		else if (t instanceof CityTerritory){
			return new int[]{3,0,0,0};
		}
		
		else{
			return new int[]{0,0,0,0};
		}
	}
}
