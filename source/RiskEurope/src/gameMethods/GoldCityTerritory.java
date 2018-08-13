package gameMethods;

public class GoldCityTerritory extends CityTerritory{
	private int city_card;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GoldCityTerritory(String name, int ID, String city_name, int value, int city_card){
		super(name, ID, city_name, value);
		this.city_card = city_card;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public int getCityCard(){
		return this.city_card;
	}
}
