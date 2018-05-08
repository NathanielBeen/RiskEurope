package gameMethods;

import java.awt.Color;
import java.util.ArrayList;

import actions.Action;
import unitsCombat.Unit;

public class Player {
	String name;
	private Color color;
	private int ID;
	private ArrayList<Territory> owned_territories;
	private ArrayList<Unit> unit_reserve;
	
	private int crown_cards;
	private int crowns;
	private int coin;
	private int turn_order;
	
	private Card first_selection;
	private Card second_selection;
	private ArrayList<Card> hand;
	private ArrayList<Card> discard;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Player(String s, Color c, int ID){
		this.name = s;
		this.color = c;
		this.ID = ID;
		this.owned_territories = new ArrayList<Territory>();
		this.unit_reserve = genUnitReserve();
		this.crown_cards = 0;
		this.crowns = 0;
		this.coin = 20;
		this.turn_order = ID;
		this.first_selection = null;
		this.second_selection = null;
		this.hand = genHand();
		this.discard = new ArrayList<Card>();
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public String getName(){
		return this.name;
	}
	
	public void setName(String s){
		this.name = s;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public ArrayList<Territory> getOwnedTerritories(){
		return this.owned_territories;
	}
	
	public ArrayList<Unit> getUnitReserve(){
		return this.unit_reserve;
	}
	
	public int getCrownCards(){
		return this.crown_cards;
	}
	
	public void setCrownCards(int i){
		this.crown_cards = i;
	}
	
	public int getCrowns(){
		return this.crowns;
	}
	
	public int getCoins(){
		return this.coin;
	}
	
	public void setCoins(int i){
		this.coin = i;
	}
	
	public int getTurnOrder(){
		return this.turn_order;
	}
	
	public void setTurnOrder(int i){
		this.turn_order = i;
	}
	
	public Card getFirstSelection(){
		return this.first_selection;
	}
	
	public void setFirstSelection(Card c){
		this.first_selection = c;
	}
	
	public Card getSecondSelection(){
		return this.second_selection;
	}
	
	public void setSecondSelection(Card c){
		this.second_selection = c;
	}
	
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	
	public ArrayList<Card> getDiscard(){
		return this.discard;
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public void addTerritory(Territory t){
		this.owned_territories.add(t);
	}
	
	public void removeTerritory(Territory t){
		this.owned_territories.remove(t);
	}
	
	public void calcNumCrowns(){
		int crowns = this.crown_cards;
		for (Territory t : this.owned_territories){
			if (t instanceof GoldCityTerritory){
				crowns ++;
			}
		}
		this.crowns = crowns;
	}
	
	/***********************
	 * Hand Methods        *
	 ***********************/
	public void playCards(Card one, Card two){
		if (this.hand.contains(one) && this.hand.contains(two)){
			this.first_selection = one;
			this.second_selection = two;
			this.hand.remove(one);
			this.hand.remove(two);
			if (this.hand.isEmpty()){
				resetHand();
			}
		}
		else{
			System.out.print("error playing round cards");
		}
	}
	
	public void useFirstCard(){
		this.discard.add(this.first_selection);
		this.first_selection = null;
	}
	
	public void useSecondCard(){
		this.discard.add(this.second_selection);
		this.second_selection = null;
	}
	
	public void resetHand(){
		this.hand = this.discard;
		this.discard = new ArrayList<Card>();
	}
	
	public ArrayList<Card> genHand(){
		ArrayList<Card> a = new ArrayList<Card>();
		a.add(new Card(this, Action.NONE, Action.TAX, Action.SPEND));
		a.add(new Card(this, Action.FORTIFY, Action.EXPAND, Action.MANEUVER));
		a.add(new Card(this, Action.KING_ME, Action.TAX, Action.SPEND));
		a.add(new Card(this, Action.SIEGE_ASSAULT, Action.EXPAND, Action.MANEUVER));
		a.add(new Card(this, Action.NONE, Action.SPLIT_EXPAND, Action.MANEUVER));
		a.add(new Card(this, Action.NONE, Action.SPLIT_EXPAND, Action.MANEUVER));
		a.add(new Card(this, Action.NONE, Action.TAX, Action.SPEND));
		a.add(new Card(this, Action.FORTIFY, Action.EXPAND, Action.MANEUVER));
		
		return a;
	}
	
	/***********************
	 * Reserve Methods     *
	 ***********************/
	public ArrayList<Unit> genUnitReserve(){
		ArrayList<Unit> o = new ArrayList<Unit>();
		for (int i = 1; i <= 35; i++){
			o.add(new Unit(this.ID, 0));
		}
		for (int i = 1; i <= 12; i++){
			o.add(new Unit(this.ID, 1));
			o.add(new Unit(this.ID, 2));
		}
		for (int i = 1; i <= 4; i++){
			o.add(new Unit(this.ID, 3));
		}
		
		return o;
	}
	
	public void addUnitsToReserve(ArrayList<Unit> unit){
		this.unit_reserve.addAll(unit);
	}
	
	public void removeUnitsFromReserve(ArrayList<Unit> unit){
		this.unit_reserve.removeAll(unit);
	}
	
	public ArrayList<Unit> getReserveUnitsByCount (int[] count){
		ArrayList<Unit> a = new ArrayList<Unit>();
		ArrayList<Unit> reserve = new ArrayList<Unit>(this.unit_reserve);
		
		for (int i = 0; i < count.length; i++){
			for (int j = 0; j < count[i]; j++){
				for (Unit u : reserve){
					if (u.getType() == i){
						a.add(u);
						reserve.remove(u);
						break;
					}
				}
			}
		}
		
		return a;
	}
	
	public ArrayList<Unit> removeReserveUnitsByCount (int[] count){
		ArrayList<Unit> a = new ArrayList<Unit>();
		
		int index = 0;
		for (int i : count){
			for (int j = 0; j < i; j++){
				for (Unit u : this.unit_reserve){
					if (u.getType() == index){
						a.add(u);
						this.unit_reserve.remove(u);
						break;
					}
				}
			}
			index ++;
		}
		
		return a;
	}
	
	public int[] getReserveCount(){
		int[] count = {0,0,0,0};
		for (Unit u : this.unit_reserve){
			count[u.getType()]++;
		}
		return count;
	}
}
