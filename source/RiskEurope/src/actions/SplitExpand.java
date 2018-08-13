package actions;

import java.util.ArrayList;

import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.RiskEurope;
import gameMethods.Territory;
import unitsCombat.Unit;

public class SplitExpand implements Action{
	
	private RiskEurope risk;
	private GameInfo info;
	
	private Player player;
	private Territory home;
	private ArrayList<Unit> terr_units;
	private ArrayList<Unit> first_units;
	private Territory first_dest;
	private ArrayList<Unit> second_units;
	private Territory second_dest;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public SplitExpand(RiskEurope risk, Player p){
		this.risk = risk;
		this.info = risk.getInfo();
		this.player = p;
		
		this.home = null;
		this.terr_units = null;
		this.first_units = null;
		this.first_dest = null;
		this.second_units = null;
		this.second_dest = null;
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
			info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genHomeOptions());
		}
		else if (this.first_units == null){
			info.setSelectionState(GameInfo.SELECT_UNITS);
			this.risk.setCurrentOptions(genUnitOptions());
		}
		else if (this.first_dest == null){
			info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genDestOptions());
		}
		else if (this.second_units == null){
			info.setSelectionState(GameInfo.SELECT_UNITS);
			this.risk.setCurrentOptions(genUnitOptions());
		}
		else if (this.second_dest == null){
			info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genDestOptions());
		}
		else{
			info.setSelectionState(GameInfo.CONFIRM_ACTION);
		}
	}
	
	@Override
	public void selectOption(Object o){
		if (this.home == null && o instanceof Territory){
			this.home = (Territory) o;
			this.terr_units = new ArrayList<Unit>(((Territory) o).getDefendingArmy());
		}
		else if (this.first_units == null && o instanceof ArrayList<?>){
			this.first_units = (ArrayList<Unit>) o;
			//remove units from map
		}
		else if (this.first_dest == null && o instanceof Territory){
			this.first_dest = (Territory) o;
			//place units on map
		}
		else if (this.second_units == null && o instanceof ArrayList<?>){
			this.second_units = (ArrayList<Unit>) o;
			//remove units from map
		}
		else if (this.second_dest == null && o instanceof Territory){
			this.second_dest = (Territory) o;
			//place units on map
		}
	}
	
	@Override
	public void resolveAction(){
		this.home.removeArmy(this.first_units);
		this.home.removeArmy(this.second_units);
		if (this.first_dest.getOwner() == null){
			this.first_dest.setOwner(this.player);
			this.first_dest.setDefendingArmy(this.first_units);
		}
		else{
			this.first_dest.setAttacker(this.player);
			this.first_dest.setAttackingArmy(this.first_units);
			this.first_dest.setIsDisputed(true);
		}
		if (this.second_dest.getOwner() == null){
			this.second_dest.setOwner(this.player);
			this.second_dest.setDefendingArmy(this.second_units);
		}
		else{
			this.second_dest.setAttacker(this.player);
			this.second_dest.setAttackingArmy(this.second_units);
			this.second_dest.setIsDisputed(true);
		}
		this.risk.getNextAction();
	}
	
	@Override
	public void resetAction(){
		this.home = null;
		this.first_units = null;
		this.first_dest = null;
		this.second_units = null;
		this.second_dest = null;
	}
	
	/***********************
	 * Option Methods      *
	 ***********************/
	public ArrayList<Object> genHomeOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t : this.player.getOwnedTerritories()){
			if (t.getDefendingArmy().size() > 2 && !t.IsDisputed()){
				boolean valid = false;
				for (Territory n : t.getNeighbors()){
					if (n.getOwner() != this.player && !n.IsDisputed()){
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
		ArrayList<Unit> valid = this.terr_units;
		if (this.first_units == null){
			o.add(1);
			o.add(valid.size()-2);
		}
		else{
			valid.removeAll(this.first_units);
			o.add(1);
			o.add(valid.size()-1);
		}
		
		for (Unit u : valid){
			o.add(u);
		}
		return o;
	}
	
	public ArrayList<Object> genDestOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		ArrayList<Unit> units = (this.second_units == null) ? this.first_units : this.second_units;
		
		boolean has_siege = false;
		for (Unit u : units){
			if (u.getType() == 3){
				has_siege = true;
			}
		}
		
		for (Territory t : this.home.getNeighbors()){
			if (t.getOwner() != this.player && !t.IsDisputed()){
				if (!t.hasCastle() || has_siege){
					o.add(t);
				}
			}
		}
		if (this.first_dest != null){
			o.remove(this.first_dest);
		}
		return o;
	}
	
	public ArrayList<Unit> getUnitsByCount(int[] c){
		ArrayList<Unit> a = new ArrayList<Unit>();
		for (int i = 0; i < c.length; i++){
			for (int j = 0; j < c[i]; j++){
				for (Unit u : this.terr_units){
					if (u.getType() == i){
						a.add(u);
						this.terr_units.remove(u);
						break;
					}
				}
			}
		}
		return a;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public Territory getHome(){
		return this.home;
	}
	
	public ArrayList<Unit> getFirstUnits(){
		return this.first_units;
	}
	
	public Territory getFirstDestination(){
		return this.first_dest;
	}
	
	public ArrayList<Unit> getSecondUnits(){
		return this.second_units;
	}
	
	public Territory getSecondDestination(){
		return this.second_dest;
	}

}
