package gameMethods;

public class Card {
	private Player player;
	private int free_action;
	private int upper_action;
	private int lower_action;
	
	private boolean has_free;
	private boolean action_used;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Card(Player player, int free, int upper, int lower){
		this.player = player;
		this.free_action = free;
		this.upper_action = upper;
		this.lower_action = lower;
		if (this.free_action != 0){
			this.has_free = true;
		}
		else{
			this.has_free = false;
		}
		this.action_used = false;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public Player getPlayer(){
		return this.player;
	}
	
	public int getFreeAction(){
		return this.free_action;
	}
	
	public boolean hasFreeAction(){
		return this.has_free;
	}
	
	public void setHasFreeAction(boolean b){
		this.has_free = b;
	}
	
	public int getUpperAction(){
		return this.upper_action;
	}
	
	public int getLowerAction(){
		return this.lower_action;
	}
	
	public boolean getActionUsed(){
		return this.action_used;
	}
	
	public void setActionUsed(boolean b){
		this.action_used = b;
	}
	
	public int getActionByLoctationInt(int i){
		switch(i){
		case 1:
			return this.free_action;
		case 2:
			return this.upper_action;
		case 3:
			return this.lower_action;
		default:
			return 0;
		}
	}
}
