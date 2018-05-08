package actions;

import java.util.ArrayList;

import gameMethods.CityTerritory;
import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.RiskEurope;
import gameMethods.Territory;
import unitsCombat.Unit;

public class Spend implements Action{
	private RiskEurope risk;
	private GameInfo info;
	
	private Player player;
	ArrayList<Territory> purchased_castles;
	ArrayList<Unit> purchased_units;
	private ArrayList<Territory> territories;
	private ArrayList<ArrayList<Unit>> placed_units;
	
	private int num_castles;
	private int available_funds;
	private int num_placed;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Spend(RiskEurope risk, Player p){
		this.risk = risk;
		this.info = risk.getInfo();
		this.player = p;
		
		this.purchased_castles = new ArrayList<Territory>();
		this.purchased_units = null;
		this.territories = new ArrayList<Territory>();
		this.placed_units = new ArrayList<ArrayList<Unit>>();
		
		this.num_castles = 0;
		this.available_funds = this.player.getCoins();
		this.num_placed = 0;
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
		if (this.purchased_units == null){
			this.info.setSelectionState(GameInfo.PURCHASE);
		}
		else if (this.num_castles != 0){
			this.info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genCastleOptions());
		}
		else if (genUnitOptions().isEmpty()){
			this.info.setSelectionState(GameInfo.CONFIRM_ACTION);
		}
		else if (this.territories.size() == num_placed){
			this.info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genTerrOptions());
		}
		else if (this.territories.size() != num_placed){
			this.info.setSelectionState(GameInfo.SELECT_UNITS);
			this.risk.setCurrentOptions(genUnitOptionsWithBounds());
		}
	}
	
	@Override
	public void selectOption(Object o){
		if (this.purchased_units == null && o instanceof ArrayList<?>){
			this.purchased_units = (ArrayList<Unit>) o;
		}
		else if (this.num_castles != 0 && o instanceof Territory){
			((Territory) o).setHasCastle(true);
			this.purchased_castles.add((Territory) o);
			this.num_castles --;
			//add castle to map
		}
		else if (this.territories.size() == num_placed && o instanceof Territory){
			this.territories.add((Territory) o);
		}
		else if (this.territories.size() != num_placed && o instanceof ArrayList<?>){
			this.placed_units.add((ArrayList<Unit>) o);
			num_placed++;
			//add units to map
		}
	}
	
	@Override
	public void resolveAction(){
		for (Territory t : this.purchased_castles){
			t.setHasCastle(true);
		}
		for (int i = 0; i < this.placed_units.size(); i++){
			this.territories.get(i).addArmy(this.placed_units.get(i));
		}
		this.player.setCoins(this.player.getCoins() - genTotalUnitCost());
		this.player.removeUnitsFromReserve(this.purchased_units);
		this.risk.getNextAction();
	}
	
	@Override
	public void resetAction(){
		for (Territory t : this.purchased_castles){
			t.setHasCastle(false);
		}
		
		this.purchased_castles = new ArrayList<Territory>();
		this.purchased_units = null;
		this.territories = new ArrayList<Territory>();
		this.placed_units = new ArrayList<ArrayList<Unit>>();
		
		this.num_castles = 0;
		this.available_funds = this.player.getCoins();
		this.num_placed = 0;
	}
	/***********************
	 * Options Methods     *
	 ***********************/
	public ArrayList<Object> genCastleOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t : this.player.getOwnedTerritories()){
			if (!t.hasCastle() && !t.IsDisputed()){
				o.add(t);
			}
		}
		return o;
	}
	
	public ArrayList<Object> genTerrOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t : this.player.getOwnedTerritories()){
			if ((t instanceof CityTerritory || t.hasCastle()) && !t.IsDisputed()){
				o.add(t);
			}
		}
		return o;
	}
	
	public ArrayList<Object> genUnitOptions(){
		ArrayList<Unit> p = new ArrayList<Unit>(purchased_units);
		for (ArrayList<Unit> a : this.placed_units){
			p.removeAll(a);
		}
		
		ArrayList<Object> o = new ArrayList<Object>();
		o.addAll(p);
		return o;
	}
	
	public ArrayList<Object> genUnitOptionsWithBounds(){
		ArrayList<Unit> p = new ArrayList<Unit>(purchased_units);
		for (ArrayList<Unit> a : this.placed_units){
			p.removeAll(a);
		}
		
		ArrayList<Object> o = new ArrayList<Object>();
		o.add(1);
		o.add(p.size());
		o.addAll(p);
		return o;
	}
	
	public int genTotalUnitCost(){
		int cost = 0;
		cost += 12*this.purchased_castles.size();
		for (Unit u : this.purchased_units){
			switch(u.getType()){
				case 0:
					cost ++;
					break;
				case 1:
					cost += 2;
					break;
				case 2:
					cost += 3;
					break;
				case 3:
					cost += 10;
					break;
			}
		}
		return cost;
	}
	
	public ArrayList<Unit> getPurchasedUnitsByCount(int[] count){
		ArrayList<Unit> p = new ArrayList<Unit>(this.purchased_units);
		for (ArrayList<Unit> a : this.placed_units){
			p.removeAll(a);
		}
		ArrayList<Unit> o = new ArrayList<Unit>();
		
		for (int i = 0; i < count.length; i++){
			for (int j = 0; j < count[i]; j++){
				for (Unit u : p){
					if (u.getType() == i){
						p.remove(u);
						o.add(u);
						break;
					}
				}
			}
		}
		return o;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public ArrayList<Territory> getTerritories(){
		return this.territories;
	}
	
	public ArrayList<Unit> getPurchasedUnits(){
		return this.purchased_units;
	}
	
	public ArrayList<ArrayList<Unit>> getPlacedUnits(){
		return this.placed_units;
	}
	
	public int getNumCastles(){
		return this.num_castles;
	}
	
	public void setNumCastles(int n){
		this.num_castles = n;
	}
	
	public int getAvailableFunds(){
		return this.available_funds;
	}
	
	public int getNumPlaced(){
		return this.num_placed;
	}
}
