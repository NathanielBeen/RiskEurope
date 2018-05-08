package actions;

import java.util.ArrayList;

import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.RiskEurope;
import gameMethods.Territory;
import unitsCombat.Unit;

public class SiegeAssault implements Action{
	private RiskEurope risk;
	private GameInfo info;
	
	private Player player;
	private Territory home;
	private Territory destination;
	
	ArrayList<Unit> casualties;
	
	private int num_siege;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public SiegeAssault(RiskEurope risk, Player p){
		this.risk = risk;
		this.info = risk.getInfo();
		this.player = p;
		
		this.home = null;
		this.destination = null;
		this.num_siege = 0;
		this.casualties = null;
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
		else if (this.destination == null){
			this.info.setSelectionState(GameInfo.SELECT_TERRITORY);
			this.risk.setCurrentOptions(genDestOptions());
		}
		else{
			this.info.setSelectionState(GameInfo.NO_SELECTION);
		}
	}
	
	@Override
	public void selectOption(Object o){
		if (this.home == null && o instanceof Territory){
			this.home = (Territory) o;
			this.num_siege = this.home.getSiegeUnitNum();
		}
		else if (this.destination == null && o instanceof Territory){
			this.destination = (Territory) o;
		}
	}
	
	@Override
	public void resolveAction(){
		this.destination.removeArmy(this.casualties);
		if (this.destination.getDefendingArmy().isEmpty()){
			this.destination.setOwner(null);
		}
		this.risk.getNextAction();
	}
	
	@Override
	public void resetAction(){
		this.home = null;
		this.destination = null;
		this.num_siege = 0;
	}
	/***********************
	 * Option Methods      *
	 ***********************/
	public ArrayList<Object> genHomeOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t : this.player.getOwnedTerritories()){
			if (!t.IsDisputed() && t.hasSiegeUnit()){
				ArrayList<Territory> a = t.getNeighbors();
				for (Territory n : a){
					if (!t.getIsWater(n) && !n.IsDisputed() && n.getOwner() != null && !n.getOwner().equals(this.player)){
						o.add(t);
						break;
					}
				}
			}
		}
		return o;
	}
	
	public ArrayList<Object> genDestOptions(){
		ArrayList<Object> o = new ArrayList<Object>();
		for (Territory t : this.home.getNeighbors()){
			if (!t.IsDisputed() && t.getOwner() != null && !t.getOwner().equals(this.player)){
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
	
	public Territory getDestination(){
		return this.destination;
	}
	
	public int getNumSiege(){
		return this.num_siege;
	}
	
	public ArrayList<Unit> getCasualties(){
		return this.casualties;
	}
	
	public void setCasualties(ArrayList<Unit> a){
		this.casualties = a;
	}
}
