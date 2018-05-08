package gameMethods;

import java.util.ArrayList;

public class CityTerritory extends Territory{
	private String city_name;
	private int value;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public CityTerritory(String name, int ID, String city_name, int value){
		super(name, ID);
		this.city_name = city_name;
		this.value = value;
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public String getCityName(){
		return this.city_name;
	}
	
	public int getValue(){
		return this.value;
	}
	
	@Override
	public int taxTerritory(){
		return value;
	}
	
	public ArrayList<Territory> genTaxTerritories(){
		ArrayList<Territory> o = new ArrayList<Territory>();
		ArrayList<Territory> q = new ArrayList<Territory>();
		o.add(this);
		q.add(this);
		
		while (!q.isEmpty()){
			ArrayList<Territory> n = q.get(0).getNeighbors();
			for (Territory t : n){
				if (t.getOwner() != null && t.getOwner().equals(getOwner()) && !t.IsDisputed() && !o.contains(t)){
					o.add(t);
					q.add(t);
				}
			}
			q.remove(0);
		}
		
		return o;
	}
}
