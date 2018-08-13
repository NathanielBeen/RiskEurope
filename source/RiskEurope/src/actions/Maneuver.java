package actions;

import java.util.ArrayList;

import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.RiskEurope;
import gameMethods.Territory;
import unitsCombat.Unit;

public class Maneuver implements Action{
	
	private RiskEurope risk;
	private GameInfo info;
	
	private Player player;
	private Territory home;
	private ArrayList<Unit> units;
	private Territory destination;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Maneuver(RiskEurope risk, Player p){
		this.risk = risk;
		this.info = risk.getInfo();
		
		this.player = p;
		this.home = null;
		this.units = null;
		this.destination = null;
	}
	
	/***********************
	 * Option Methods      *
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
		else if (this.units == null){
			this.info.setSelectionState(GameInfo.SELECT_UNITS);
			this.risk.setCurrentOptions(genUnitOptions());
		}
		else if (this.destination == null){
			this.info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genDestOptions());
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
		else if (this.units == null && o instanceof ArrayList<?>){
			this.units = (ArrayList<Unit>) o;
			//remove units from the map
		}
		else if (this.destination == null && o instanceof Territory){
			this.destination = (Territory) o;
		}
	}
	
	@Override
	public void resolveAction(){
		this.home.removeArmy(this.units);
		this.destination.addArmy(this.units);
		
		this.risk.getNextAction();
	}
	
	@Override
	public void resetAction(){
		this.home = null;
		this.units = null;
		this.destination = null;
	}
	
	/***********************
	 * Option Methods      *
	 ***********************/
	public ArrayList<Object> genHomeOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t: this.player.getOwnedTerritories()){
			if (t.getDefendingArmy().size() > 1 && !t.IsDisputed()){
				boolean valid = false;
				for (Territory n : t.getNeighbors()){
					if (n.getOwner() == this.player || n.getAttacker() == this.player){
						valid = true;
						break;
					}
				}
				if (valid){
					o.add(t);
				}
			}
		}
		return o;
	}
	
	public ArrayList<Object> genUnitOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		
		o.add(1);
		o.add(this.home.getDefendingArmy().size()-1);
		
		for (Unit u : this.home.getDefendingArmy()){
			o.add(u);
		}
		return o;
	}
	
	public ArrayList<Object> genDestOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		ArrayList<Territory> n = new ArrayList<Territory>();
		
		for (Territory t : this.home.getNeighbors()){
			if (t.getOwner() == this.player){
				o.add(t);
				n.add(t);
			}
			else if (t.getAttacker() == this.player){
				o.add(t);
			}
		}
		
		for (Territory t : n){
			for (Territory nt : t.getNeighbors()){
				if ((nt.getOwner() == this.player || nt.getAttacker() == this.player) && !o.contains(nt)){
					o.add(nt);
				}
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
	
	public ArrayList<Unit> getUnits(){
		return this.units;
	}
	
	public Territory getDestination(){
		return this.destination;
	}
}
