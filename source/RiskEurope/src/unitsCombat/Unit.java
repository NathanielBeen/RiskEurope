package unitsCombat;

public class Unit {
	private int player_id;
	private int type;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Unit(int id, int type){
		this.player_id = id;
		this.type = type;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public int getPlayerID(){
		return this.player_id;
	}
	
	public int getType(){
		return this.type;
	}
}
