package gameMethods;

import java.util.ArrayList;

import actions.Action;
import actions.Expand;
import actions.Fortify;
import actions.KingMe;
import actions.Maneuver;
import actions.Placement;
import actions.SiegeAssault;
import actions.Spend;
import actions.SplitExpand;
import actions.Tax;
import guiMethods.GameInit;
import unitsCombat.Combat;
import unitsCombat.Unit;

public class RiskEurope {
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Territory> territories = new ArrayList<Territory>();
	private GameInfo info;
	
	private ArrayList<Player> static_turn_order = new ArrayList<Player>();
	private ArrayList<Player> turn_order = new ArrayList<Player>();
	
	private Card curr_card;
	private Action curr_action;
	private Player curr_player;
	private ArrayList<Object> curr_options;
	private ArrayList<Territory> combat_queue;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public RiskEurope(GameInfo info){
		this.info = info;
		
		this.curr_card = null;
		this.curr_action = null;
		this.curr_player = null;
		
		this.curr_options = new ArrayList<Object>();
		this.combat_queue = new ArrayList<Territory>();
		
		genTurnOrder();
	}

	/***********************
	 * Get and Set Methods *
	 ***********************/
	public GameInfo getInfo(){
		return this.info;
	}
	
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	
	public void setPlayers(ArrayList<Player> a){
		this.players = a;
	}
	
	public ArrayList<Territory> getTerritoryList(){
		return this.territories;
	}
	
	public void setTerritoryList(ArrayList<Territory> a){
		this.territories = a;
	}
	
	public Card getCurrentCard(){
		return this.curr_card;
	}
	
	public Action getCurrentAction(){
		return this.curr_action;
	}
	
	public Player getCurrentPlayer(){
		return this.curr_player;
	}
	
	public void setCurrentPlayer(Player p){
		this.curr_player = p;
	}
	
	public ArrayList<Player> getTurnOrder(){
		return this.turn_order;
	}
	
	public ArrayList<Player> getStaticTurnOrder(){
		return this.static_turn_order;
	}
	
	public ArrayList<Object> getCurrentOptions(){
		return this.curr_options;
	}
	
	public void setCurrentOptions(ArrayList<Object> o){
		this.curr_options = o;
	}
	
	public ArrayList<Territory> getCombatQueue(){
		return this.combat_queue;
	}
	
	public void setCombatQueue(ArrayList<Territory> a){
		this.combat_queue = a;
	}
	
	/***********************
	 * Turn Order Methods  *
	 ***********************/
	public void genSingleTurnOrder(){
		ArrayList<Player> single = new ArrayList<Player>();
		for (int i = 0; i < players.size(); i++){
			for (Player p : this.players){
				if (p.getTurnOrder() == i){
					single.add(p);
					break;
				}
			}
		}
		this.turn_order = new ArrayList<Player>(single);
		this.static_turn_order = new ArrayList<Player>(single);
	}
	
	public void genTurnOrder(){
		ArrayList<Player> single = new ArrayList<Player>();
		for (int i = 0; i < players.size(); i++){
			for (Player p : this.players){
				if (p.getTurnOrder() == i){
					single.add(p);
					break;
				}
			}
		}
		
		this.turn_order = new ArrayList<Player>(single);
		this.turn_order.addAll(single);
		
		this.static_turn_order = new ArrayList<Player>(single);
	}
	
	public Player getNextPlayer(){
		this.turn_order.remove(this.curr_player);
		if (this.turn_order.size() == 0){
			return null;
		}
		else{
			return this.turn_order.get(0);
		}
	}
	
	public void getNextCard(){
		Player next = getNextPlayer();
		if (next != null){
			if (next.getFirstSelection() != null){
				this.curr_card = next.getFirstSelection();
			}
			else{
				this.curr_card = next.getSecondSelection();
			}
		}
		this.curr_player = next;
	}
	
	public Player getPlayerByTurnOrder(int i){
		for (Player p : this.players){
			if (p.getTurnOrder() == i){
				return p;
			}
		}
		return null;
	}
	
	/***********************
	 * Card Methods        *
	 ***********************/
	public boolean validateActionSelection(int i){
		if (i == 0 && this.curr_card.hasFreeAction() == true){
			return true;
		}
		else if ((i == 1 || i == 2) && this.curr_card.getActionUsed() == false){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void selectActionFromCard(int i){
		switch(i){
			case 0:
				int j = this.curr_card.getFreeAction();
				this.curr_action = convertIntToAction(this.curr_player, j);
				this.curr_card.setHasFreeAction(false);
				break;
			case 1:
				j = this.curr_card.getUpperAction();
				this.curr_action = convertIntToAction(this.curr_player, j);
				this.curr_card.setActionUsed(true);
				break;
			case 2:
				j = this.curr_card.getLowerAction();
				this.curr_action = convertIntToAction(this.curr_player, j);
				this.curr_card.setActionUsed(true);
				break;
			default:
				return;
		}
	}
	
	public boolean resolveIfCardUsed(){
		if (!this.curr_card.hasFreeAction() && this.curr_card.getActionUsed()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void useAndDiscardCard(){
		if (this.curr_player.getFirstSelection() != null && this.curr_player.getFirstSelection().equals(this.curr_card)){
			this.curr_player.useFirstCard();
			this.curr_card = null;
		}
		else if (this.curr_player.getSecondSelection() != null && this.curr_player.getSecondSelection().equals(this.curr_card)){
			this.curr_player.useSecondCard();
			this.curr_card = null;
		}
		else{
			System.out.print("error retrieving next card");
		}
		
	}
	
	/***********************
	 * Action Methods      *
	 ***********************/
	public Action convertIntToAction(Player p, int i){
		switch(i){
			case Action.EXPAND:
				return new Expand(this,p);
			case Action.MANEUVER:
				return new Maneuver(this,p);
			case Action.SPLIT_EXPAND:
				return new SplitExpand(this,p);
			case Action.TAX:
				return new Tax(this,p);
			case Action.SPEND:
				return new Spend(this,p);
			case Action.KING_ME:
				return new KingMe(this,p);
			case Action.FORTIFY:
				return new Fortify(this,p);
			case Action.SIEGE_ASSAULT:
				return new SiegeAssault(this,p);
			case Action.PLACEMENT:
				return new Placement(this,p);
				
			default:
				return null;
		}
	}
	
	public void resetAction(){
		this.curr_options = null;
		if (this.curr_action instanceof KingMe || this.curr_action instanceof Fortify || this.curr_action instanceof SiegeAssault){
			this.curr_card.setHasFreeAction(true);
		}
		else{
			this.curr_card.setActionUsed(false);
		}
		this.info.setSelectionState(GameInfo.SELECT_ACTION);
	}
	
	/***********************
	 * Selection Methods   *
	 ***********************/
	public boolean validateSelection(Object o){
		if ((o instanceof ArrayList<?> && this.curr_options.containsAll((ArrayList<?>) o))
		    || this.curr_options.contains(o)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void makeSelection(Object o){
		this.curr_action.selectOption(o);
		this.curr_options = null;
		this.curr_action.getNextAction();
	}
	
	public void getNextAction(){
		this.curr_action = null;
		if (this.info.getGameState() == GameInfo.PLACEMENT){
			getNextPlacement();
		}
		else if (this.info.getGameState() == GameInfo.INIT_CARD_SELECTION){
			getNextInitSelection();
		}
		else if (resolveIfCardUsed()){
			useAndDiscardCard();
			getNextCard();
			if (this.curr_player == null){
				this.info.setSelectionState(GameInfo.NO_SELECTION);
				this.info.setGameState(GameInfo.COMBAT);
				generateCombatList();
			}
			else{
				this.info.setSelectionState(GameInfo.SELECT_ACTION);
			}
		}
		else{
			this.info.setSelectionState(GameInfo.SELECT_ACTION);
		}
	}
	
	/*******************************
	 * Placement Methods           *
	 *******************************/
	public void beginPlacement(){
		genSingleTurnOrder();
		this.curr_player = getNextPlayer();
		this.info.setGameState(GameInfo.PLACEMENT);
		this.info.setSelectionState(GameInfo.NO_SELECTION);
		
		this.curr_action = new Placement(this, this.curr_player);
	}
	
	public void getNextPlacement(){
		this.curr_player = getNextPlayer();
		this.info.setSelectionState(GameInfo.NO_SELECTION);
		if (this.curr_player == null){
			beginInitCardSelection();
			this.curr_action = null;
		}
		else{
			this.curr_action = new Placement(this, this.curr_player);
		}
	}
	
	/*******************************
	 * Init Selection Methods      *
	 *******************************/
	public void beginInitCardSelection(){
		Player p = checkForEnd();
		if (p != null){
			this.info.setGameState(GameInfo.GAME_END);
			this.info.setSelectionState(GameInfo.NO_SELECTION);
		}
		else{
			genSingleTurnOrder();
			this.curr_player = getNextPlayer();
			this.info.setGameState(GameInfo.INIT_CARD_SELECTION);
			this.info.setSelectionState(GameInfo.NO_SELECTION);
		}
	}
	
	public void getNextInitSelection(){
		this.curr_player = getNextPlayer();
		if (this.curr_player == null){
			endInitSelection();
		}
	}
	
	public void endInitSelection(){
		this.info.setGameState(GameInfo.TURN);
		genTurnOrder();
		getNextActionSelection();
		this.info.setSelectionState(GameInfo.SELECT_ACTION);
	}
	
	/*******************************
	 * Action Selection Methods    *
	 *******************************/
	public void getNextActionSelection(){
		getNextCard();
		if (this.curr_player == null){
			this.curr_card = null;
			this.info.setGameState(GameInfo.COMBAT);
			this.info.setSelectionState(GameInfo.NO_SELECTION);
			//begin combat
		}
		else{
			this.info.setSelectionState(GameInfo.SELECT_ACTION);
		}
	}
	
	public ArrayList<Unit> convertCountToTerritoryUnits(Player p, Territory t, int[] c){
		ArrayList<Unit> a = new ArrayList<Unit>();
		ArrayList<Unit> tu;
		
		if (t.getOwner().equals(p)){
			tu = new ArrayList<Unit>(t.getDefendingArmy());
		}
		else{
			tu = new ArrayList<Unit>(t.getAttackingArmy());
		}

		for (int i = 0; i < c.length; i++){
			for (int j = 0; j < c[i]; j++){
				for (Unit u : tu){
					if (u.getType() == i){
						a.add(u);
						tu.remove(u);
						break;
					}
				}
			}
		}
		return a;
	}
	
	/*******************************
	 * Combat Methods              *
	 *******************************/
	public void generateCombatList(){
		ArrayList<Territory> a = new ArrayList<Territory>();
		for (Territory t : this.territories){
			if (t.IsDisputed()){
				a.add(t);
			}
		}
		this.combat_queue = a;
	}
	
	public Combat genNextCombat(){
		if (this.combat_queue.size() == 0){
			beginInitCardSelection();
			return null;
		}
		else{
			Territory t = this.combat_queue.get(0);
			return new Combat(t);
		}
	}
	
	public void emptyCombatList(){
		this.combat_queue = new ArrayList<Territory>();
	}
	
	/*******************************
	 * Ending Methods              *
	 *******************************/
	public Player checkForEnd(){
		for (Player p : this.players){
			if (p.getCrowns() > 5){
				return p;
			}
		}
		return null;
	}
}
