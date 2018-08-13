package unitsCombat;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gameMethods.Player;
import gameMethods.Territory;

public class Combat {
	public static final int GENERAL = 0;
	
	public static final int FOOTMAN = 0;
	public static final int ARCHER = 1;
	public static final int CAVALRY = 2;
	public static final int SIEGE = 3;
	
	public static final int DRAW = 0;
	public static final int ATTACKER = 1;
	public static final int DEFENDER = 2;
	
	private Territory territory;
	private Player attacker;
	private Player defender;
	private boolean[] queue;
	
	private ArrayList<Unit> attacking_units;
	private ArrayList<Unit> defending_units;
	
	private ArrayList<Unit> attacking_casualties;
	private ArrayList<Unit> defending_casualties;
	
	private ArrayList<Integer> attacking_rolls;
	private ArrayList<Integer> defending_rolls;
	private int[] attacking_unit_count;
	private int[] defending_unit_count;
	boolean has_castle;
	
	boolean has_castle_round;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Combat(Territory t){
		this.territory = t;
		this.attacker = t.getAttacker();
		this.defender = t.getOwner();
		
		this.attacking_units = new ArrayList<Unit>(t.getAttackingArmy());
		this.defending_units = new ArrayList<Unit>(t.getDefendingArmy());
		
		this.attacking_casualties = new ArrayList<Unit>();
		this.defending_casualties = new ArrayList<Unit>();
		this.has_castle = t.hasCastle();
		
		this.attacking_rolls = new ArrayList<Integer>();
		this.defending_rolls = new ArrayList<Integer>();
		this.attacking_unit_count = genUnitCount(this.attacking_units);
		this.defending_unit_count = genUnitCount(this.defending_units);
		this.has_castle_round = this.has_castle;
		
		this.queue = createCombatQueue();
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public Territory getTerritory(){
		return this.territory;
	}
	
	public Player getAttacker(){
		return this.attacker;
	}
	
	public Player getDefender(){
		return this.defender;
	}
	
	public ArrayList<Unit> getAttackingUnits(){
		return this.attacking_units;
	}
	
	public ArrayList<Unit> getDefendingUnits(){
		return this.defending_units;
	}
	
	public ArrayList<Unit> getAttackingCasualties(){
		return this.attacking_casualties;
	}
	
	public ArrayList<Unit> getDefendingCasualties(){
		return this.defending_casualties;
	}
	
	public ArrayList<Integer> getAttackingRolls(){
		return this.attacking_rolls;
	}
	
	public void setAttackingRolls(ArrayList<Integer> a){
		this.attacking_rolls = a;
	}
	
	public ArrayList<Integer> getDefendingRolls(){
		return this.defending_rolls;
	}
	
	public void setDefendingRolls(ArrayList<Integer> a){
		this.defending_rolls = a;
	}
	
	public int[] getAttackingUnitCount(){
		return this.attacking_unit_count;
	}
	
	public int[] getDefendingUnitCount(){
		return this.defending_unit_count;
	}
	
	public boolean castleUsedThisRound(){
		return this.has_castle_round;
	}
	
	public void setCastleUsedThisRound(boolean b){
		this.has_castle_round = b;
	}
	
	/***********************
	 * Combat Methods      *
	 ***********************/	
	public boolean[] createCombatQueue(){
		boolean[] round = new boolean[]{true,false,false,false};
		this.has_castle_round = this.has_castle;
		
		if (this.attacking_unit_count[1] != 0 || this.defending_unit_count[1] != 0){
			round[1] = true;
		}
		if (this.attacking_unit_count[2] != 0 || this.defending_unit_count[2] != 0){
			round[2] = true;
		}
		if (this.attacking_unit_count[3] != 0 || this.defending_unit_count[3] != 0){
			round[3] = true;
		}
		
		return round;
	}
	
	public int detNextRank(int i){
		if (i == FOOTMAN){
			this.queue = createCombatQueue();
		}
		else{
			this.queue[i] = false;
		}
		for (int j = 3; j >= 0; j--){
			if (this.queue[j]){
				return j;
			}
		}
		System.out.print("error determining rank");
		return -1;
	}
	
	public int[] genUnitCount(ArrayList<Unit> army){
		int[] count = new int[]{0,0,0,0};
		for (Unit u : army){
			count[u.getType()]++;
		}
		return count;
	}
	
	/***********************
	 * Dice Methods        *
	 ***********************/
	public int genAttackDice(int i){
		int num;
		if (i == SIEGE){
			num = this.attacking_unit_count[i]*2;
		}
		else if (i == GENERAL){
			num = 0;
			for (int n : this.attacking_unit_count){
				num += n;
			}
			if (num > 3){
				num = 3;
			}
		}
		else{
			num = this.attacking_unit_count[i];
		}
		return num;
	}
	
	public int genDefenseDice(int i){
		int num;
		if (i == SIEGE){
			num = this.defending_unit_count[i]*2;
		}
		else if (i == GENERAL){
			num = 0;
			for (int n : this.defending_unit_count){
				num += n;
			}
			if (num > 2){
				num = 2;
			}
		}
		else{
			num = this.defending_unit_count[i];
		}
		return num;
	}
	
	public ArrayList<Integer> rollDice(int dice){
		ArrayList<Integer> roll = new ArrayList<Integer>();
		for (int i = 0; i < dice; i++){
			roll.add(ThreadLocalRandom.current().nextInt(1,7));
		}
		return roll;
	}
	
	public int[] determineHits(ArrayList<Integer> attack_roll, ArrayList<Integer> defense_roll, int i){
		int[] hits = new int[2];
		int crit = 6;
		int attack_hits = 0;
		int defense_hits = 0;
		
		switch(i){
			case ARCHER:
				crit = 3;
				break;
			case CAVALRY:
				crit = 5;
				break;
			case SIEGE:
				crit = 3;
				break;
			default:
				break;
		}
		if (i != GENERAL){
			for (int die : attack_roll){
				if (die >= crit){
					attack_hits ++;
				}
			}
			for (int die : defense_roll){
				if (die >= crit){
					defense_hits ++;
				}
			}
			
			hits[0] = attack_hits;
			hits[1] = defense_hits;
		}
		else{
			int[] general = determineGeneralHits(attack_roll, defense_roll);
			hits[0] = general[0];
			hits[1] = general[1];
		}
		
		if (hits[0] > this.attacking_units.size()){
			hits[0] = this.attacking_units.size();
		}
		if (hits[1] > this.defending_units.size()){
			hits[1] = this.defending_units.size();
		}
		return hits;
	}
	
	public int[] determineGeneralHits(ArrayList<Integer> attack_roll, ArrayList<Integer> defense_roll){
		int[] hits = {0,0};
		Integer attack_val = getLargestDieValue(attack_roll);
		Integer defense_val = getLargestDieValue(defense_roll);
		
		attack_roll.remove(attack_val);
		defense_roll.remove(defense_val);
		if (attack_val > defense_val){
			hits[0]++;
		}
		else{
			hits[1]++;
		}
		
		if (!attack_roll.isEmpty() && !defense_roll.isEmpty()){
			attack_val = getLargestDieValue(attack_roll);
			defense_val = getLargestDieValue(defense_roll);
			if (attack_val > defense_val){
				hits[0]++;
			}
			else{
				hits[1]++;
			}
			
		}
		return hits;
	}
	
	public int getLargestDieValue(ArrayList<Integer> roll){
		int value = 1;
		for (int i : roll){
			if (i > value){
				value = i;
			}
		}
		return value;
	}
	
	/***********************
	 * Casualty Methods    *
	 ***********************/
	public void takeCasualties(int[] attacker_count, int[] defender_count){
		ArrayList<Unit> round_attacker_casualties = getCasualtiesByCount(this.attacking_units, attacker_count);
		ArrayList<Unit> round_defender_casualties = getCasualtiesByCount(this.defending_units, defender_count);
		
		this.attacking_units.removeAll(round_attacker_casualties);
		this.defending_units.removeAll(round_defender_casualties);
		this.attacking_casualties.addAll(round_attacker_casualties);
		this.defending_casualties.addAll(round_defender_casualties);
		
		alterUnitCount(this.attacking_unit_count, attacker_count);
		alterUnitCount(this.defending_unit_count, defender_count);
	}
	
	public ArrayList<Unit> getCasualtiesByCount(ArrayList<Unit> army, int[] cas){
		ArrayList<Unit> adj_army = new ArrayList<Unit>(army);
		ArrayList<Unit> casualties = new ArrayList<Unit>();
		
		for (int i = 0; i < cas.length; i++){
			for (int j = 0; j < cas[i]; j++){
				for (Unit u : adj_army){
					if (u.getType() == i){
						casualties.add(u);
						adj_army.remove(u);
						break;
					}
				}
			}
		}
		int units = 0;
		for (int i : cas){
			units += i;
		}
		if (casualties.size() != units){
			System.out.print("error taking casualties");
		}
		
		return casualties;
	}
	
	public void alterUnitCount(int[] count, int[] casualties){
		for (int i = 0; i < count.length; i ++){
			count[i] = count[i] - casualties[i];
		}
	}
	
	/***********************
	 * Ending Methods      *
	 ***********************/
	public boolean determineCombatEnd(){
		boolean attack_remain = hasUnits(this.attacking_unit_count);
		boolean defence_remain = hasUnits(this.defending_unit_count);
		if (attack_remain && defence_remain){
			return false;
		}
		else if (attack_remain){
			resolveCombat(ATTACKER);
			return true;
		}
		else if (defence_remain){
			resolveCombat(DEFENDER);
			return true;
		}
		else{
			resolveCombat(DRAW);
			return true;
		}
	}
	
	public boolean hasUnits(int[] count){
		for (int i : count){
			if (i != 0){
				return true;
			}
		}
		return false;
	}
	
	public void resolveCombat(int win){
		if (!this.attacking_casualties.isEmpty()){
			this.territory.removeArmy(this.attacking_casualties);
			this.attacker.addUnitsToReserve(this.attacking_casualties);
		}
		if (!this.defending_casualties.isEmpty()){
			this.territory.removeArmy(this.defending_casualties);
			this.defender.addUnitsToReserve(this.defending_casualties);
		}
		this.territory.setIsDisputed(false);
		
		if (win == ATTACKER){
			this.territory.setOwner(this.attacker);
			this.territory.setDefendingArmy(this.territory.getAttackingArmy());
			this.territory.emptyAttackArmy();
			this.territory.setAttacker(null);
		}
		else if (win == DEFENDER){
			this.territory.emptyAttackArmy();
			this.territory.setAttacker(null);
		}
		else if (win == DRAW){
			this.territory.emptyDefenceArmy();
			this.territory.setOwner(null);
			this.territory.emptyAttackArmy();
			this.territory.setAttacker(null);
		}
	}
}
