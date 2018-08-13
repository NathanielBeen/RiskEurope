package actions;

import java.util.ArrayList;

import gameMethods.GameInfo;
import gameMethods.GoldCityTerritory;
import gameMethods.Player;
import gameMethods.RiskEurope;
import gameMethods.Territory;
import unitsCombat.Unit;

public class Placement implements Action{
	private RiskEurope risk;
	private GameInfo info;
	
	private Player player;
	private Territory start;
	private ArrayList<Unit> start_units;
	private Territory second;
	private ArrayList<Unit> second_units;

	/***********************
	 * Constructor         *
	 ***********************/
	public Placement(RiskEurope risk, Player p){
		this.risk = risk;
		this.info = risk.getInfo();
		
		this.player = p;
		this.start = null;
		this.start_units = null;
		this.second = null;
		this.second_units = null;
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
		if (this.start == null){
			info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genStartOptions());
		}
		else if (this.start_units == null){
			info.setSelectionState(GameInfo.SELECT_UNITS);
			this.risk.setCurrentOptions(genUnitOptions());
		}
		else if (this.second == null){
			info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genSecondTerrOptions());
		}
		else{
			info.setSelectionState(GameInfo.CONFIRM_ACTION);
		}
	}
	
	@Override
	public void selectOption(Object o){
		if (this.start == null && o instanceof Territory){
			this.start = (Territory) o;
		}
		else if (this.start_units == null && o instanceof ArrayList<?>){
			this.start_units = (ArrayList<Unit>) o;
			this.second_units = genSecondUnits();
			//place castle, units on terr
		}
		else if (this.second == null && o instanceof Territory){
			this.second = (Territory) o;
			//place units on second terr
		}
	}
	
	@Override
	public void resolveAction(){
		int[] count = new int[4];
		count[0] = 10;
		this.player.removeReserveUnitsByCount(count);
		
		this.start.setOwner(this.player);
		this.start.setHasCastle(true);
		this.start.setDefendingArmy(this.start_units);
		
		this.second.setOwner(this.player);
		this.second.setDefendingArmy(this.second_units);
		
		this.risk.getNextAction();
		
	}
	
	@Override
	public void resetAction(){
		this.start = null;
		this.start_units = null;
		this.second = null;
		this.second_units = null;
	}
	/***********************
	 * Option Methods      *
	 ***********************/
	public ArrayList<Object> genStartOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t : this.risk.getTerritoryList()){
			if (t instanceof GoldCityTerritory && t.getOwner() == null){
				o.add(t);
			}
		}
		
		return o;
	}
	
	public ArrayList<Object> genUnitOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		o.add(1);
		o.add(9);
		for (int i = 0; i < 10; i++){
			o.add(new Unit(this.player.getID(), 0));
		}
		return o;
	}
	
	public ArrayList<Object> genSecondTerrOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t : this.start.getNeighbors()){
			if (t.getOwner() == null){
				o.add(t);
			}
		}
		return o;
	}
	
	public ArrayList<Unit> genSecondUnits(){
		ArrayList<Unit> u = new ArrayList<Unit>();
		int size = 10 - this.start_units.size();
		for (int i = 0; i < size; i++){
			u.add(new Unit(this.player.getID(), 0));
		}
		return u;
	}

	/***********************
	 * Get and Set Methods *
	 ***********************/
	public Territory getStart(){
		return this.start;
	}
	
	public ArrayList<Unit> getStartUnits(){
		return this.start_units;
	}
	
	public Territory getSecond(){
		return this.second;
	}
	
	public ArrayList<Unit> getSecondUnits(){
		return this.second_units;
	}
}
