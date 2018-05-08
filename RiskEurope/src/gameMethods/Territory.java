package gameMethods;

import java.util.ArrayList;

import unitsCombat.Unit;

public class Territory {
	private GameHash hash;

	private String name;
	private int ID;
	private ArrayList<Connector> neighbors;
	private Player owner;
	private boolean is_disputed;
	private boolean has_castle;
	
	private ArrayList<Unit> defending_army;
	private Player attacker;
	private ArrayList<Unit> attacking_army;
	
	/*************************
	 * Constructor           *
	 *************************/
	public Territory(String name, int ID){
		this.name = name;
		this.ID = ID;
		this.neighbors = new ArrayList<Connector>();
		this.owner = null;
		this.is_disputed = false;
		this.has_castle = false;
		this.defending_army = new ArrayList<Unit>();
		this.attacker = null;
		this.attacking_army = new ArrayList<Unit>();
	}
	
	/*************************
	 * Get and Set Methods   *
	 *************************/
	public String getName(){
		return this.name;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public ArrayList<Territory> getNeighbors(){
		ArrayList<Territory> n = new ArrayList<Territory>();
		for (Connector c : this.neighbors){
			n.add(c.getOtherTerritory(this));
		}
		return n;
	}
	
	public Player getOwner(){
		return this.owner;
	}
	
	public void setOwner(Player p){
		if (this.owner != null){
			this.owner.removeTerritory(this);
		}
		this.owner = p;
		if (p != null){
			p.addTerritory(this);
		}
	}
	
	public boolean IsDisputed(){
		return this.is_disputed;
	}
	
	public void setIsDisputed(boolean b){
		this.is_disputed = b;
	}

	public boolean hasCastle(){
		return this.has_castle;
	}
	
	public void setHasCastle(boolean b){
		this.has_castle = b;
	}
	
	public ArrayList<Unit> getDefendingArmy(){
		return this.defending_army;
	}
	
	public void setDefendingArmy(ArrayList<Unit> army){
		this.defending_army = army;
	}
	
	public Player getAttacker(){
		return this.attacker;
	}
	
	public void setAttacker(Player p){
		this.attacker = p;
	}
	
	public ArrayList<Unit> getAttackingArmy(){
		return this.attacking_army;
	}
	
	public void setAttackingArmy(ArrayList<Unit> army){
		this.attacking_army = army;
	}
	
	/*************************
	 * Connection Methods    *
	 *************************/
	public void addNeighbor(Connector c){
		this.neighbors.add(c);
	}
	
	public boolean getIsWater(Territory t){
		for (Connector c : this.neighbors){
			if (c.getOtherTerritory(this).equals(t)){
				if (c.isWaterConnection()){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return true;
	}
	
	/*************************
	 * Army Methods          *
	 *************************/
	public void changeOwner(Player p){
		this.owner.removeTerritory(this);
		this.owner = p;
		p.addTerritory(this);
	}
	
	public void addArmy(ArrayList<Unit> new_army){
		if(new_army.get(0).getPlayerID() == this.owner.getID()){
			this.defending_army.addAll(new_army);
		}
		else if (this.is_disputed && new_army.get(0).getPlayerID() == this.attacker.getID()){
			this.attacking_army.addAll(new_army);
		}
		else if (!this.is_disputed){
			this.attacking_army = new_army;
			this.is_disputed = true;
			this.attacker = hash.getPlayer(new_army.get(0).getPlayerID());
		}
	}
	
	public void removeArmy(ArrayList<Unit> to_remove){
		if (is_disputed && this.attacker.getID() == to_remove.get(0).getPlayerID()){
			this.attacking_army.removeAll(to_remove);
		}
		else if (this.owner.getID() == to_remove.get(0).getPlayerID()){
			this.defending_army.removeAll(to_remove);
		}
	}
	
	public void emptyAttackArmy(){
		this.attacking_army = new ArrayList<Unit>();
		this.attacker = null;
	}
	
	public void emptyDefenceArmy(){
		this.defending_army = new ArrayList<Unit>();
		this.owner = null;
	}
	
	public boolean hasSiegeUnit(){
		for (Unit u : this.defending_army){
			if (u.getType() == 3){
				return true;
			}
		}
		return false;
	}
	
	public int getSiegeUnitNum(){
		int count = 0;
		for (Unit u : this.defending_army){
			if (u.getType() == 3){
				count++;
			}
		}
		return count;
	}
	
	/*************************
	 * Other Methods         *
	 *************************/
	public int taxTerritory(){
		return 1;
	}
	
	public void buildCastle(){
		this.has_castle = true;
	}
}
