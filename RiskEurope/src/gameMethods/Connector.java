package gameMethods;

public class Connector {
	private Territory terr_1;
	private Territory terr_2;
	private boolean is_water;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public Connector(Territory terr_1, Territory terr_2, boolean is_water){
		this.terr_1 = terr_1;
		this.terr_2 = terr_2;
		this.is_water = is_water;
		
		this.terr_1.addNeighbor(this);
		this.terr_2.addNeighbor(this);
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/	
	public Territory getTerr1(){
		return this.terr_1;
	}
	
	public Territory getTerr2(){
		return this.terr_2;
	}
	
	public boolean isWaterConnection(){
		return this.is_water;
	}
	
	public Territory getOtherTerritory(Territory t){
		if (terr_1.equals(t)){
			return terr_2;
		}
		else if (terr_2.equals(t)){
			return terr_1;
		}
		else{
			return null;
		}
	}
}
