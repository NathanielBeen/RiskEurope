package guiMethods;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import gameMethods.CityTerritory;
import gameMethods.Connector;
import gameMethods.GameHash;
import gameMethods.GoldCityTerritory;
import gameMethods.Player;
import gameMethods.Territory;

public class GameInit {
	private GameGUI gui;
	private GameHash hash;
	
	private ArrayList<Territory> territories = new ArrayList<Territory>();
	private ArrayList<GuiTerritory> gui_territories = new ArrayList<GuiTerritory>();
	
	/**************************
	 * Constructor            *
	 **************************/
	public GameInit(GameGUI gui, GameHash hash){
		this.gui = gui;
		this.hash = hash;
		initTerritories();
		initConnectors();
		initPlayers();
	}
	
	/**************************
	 * Init Methods           *
	 **************************/
	public void initTerritories(){
		InputStream stream = getClass().getResourceAsStream("/resources/terr_info.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try{
			String line = null;
			while ((line = reader.readLine()) != null){
				String[] info = line.split("\\|");
				String name = info[0];
				int ID = Integer.valueOf(info[1]);
				
				int x_cor = Integer.valueOf(info[2]);
				int y_cor = Integer.valueOf(info[3]);
				int army_x = Integer.valueOf(info[4]);
				int army_y = Integer.valueOf(info[5]);
				int center_x = Integer.valueOf(info[6]);
				int center_y = Integer.valueOf(info[7]);
				
				String type = info[8];
				String url = "/resources/territoryImages/"+name+".png";
				BufferedImage img = ImageIO.read(getClass().getResource(url));
				
				switch(type){
				case "t":
					Polygon bounds = genBounds(info[9]);
					
					Territory t = new Territory(name, ID);
					GuiTerritory g = new GuiTerritory(this.gui.getGameInfo(), t, img, x_cor, y_cor, center_x, center_y, army_x, army_y, bounds);
			
					this.territories.add(t);
					this.hash.addGuiTerritory(g);
					this.gui_territories.add(g);
					break;
				case "c":
					String city_name = info[9];
					int value = Integer.valueOf(info[10]);
					bounds = genBounds(info[11]);
					
					t = new CityTerritory(name, ID, city_name, value);
					g = new GuiTerritory(this.gui.getGameInfo(), t, img, x_cor, y_cor, center_x, center_y, army_x, army_y, bounds);
					
					this.territories.add(t);
					this.hash.addGuiTerritory(g);
					this.gui_territories.add(g);
					break;
				case "g":
					city_name = info[9];
					value = Integer.valueOf(info[10]);
					int city_card = Integer.valueOf(info[11]);
					bounds = genBounds(info[12]);
					
					t = new GoldCityTerritory(name, ID, city_name, value, city_card);
					g = new GuiTerritory(this.gui.getGameInfo(), t, img, x_cor, y_cor, center_x, center_y, army_x, army_y, bounds);
					
					this.territories.add(t);
					this.hash.addGuiTerritory(g);
					this.gui_territories.add(g);
					break;
				default:
					break;
				}
			}
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		this.gui.getRisk().setTerritoryList(this.territories);
		this.gui.getMap().setGuiTerritories(this.gui_territories);
	}
	
	public void initConnectors(){
		InputStream stream = getClass().getResourceAsStream("/resources/terr_connections.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try{
			String line = null;
			int home_id = 1;
			while ((line = reader.readLine()) != null){
				String[] connections = line.split("\\|");
				for (String s : connections){
					String[] d = s.split(",");
					Territory home_terr = this.hash.getTerritory(home_id);
					Territory dest_terr = this.hash.getTerritory(Integer.valueOf(d[0]));
					
					if (!home_terr.getNeighbors().contains(dest_terr)){
						new Connector(home_terr, dest_terr, Boolean.valueOf(d[1]));
					}
				}
				home_id++;
			}
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Polygon genBounds(String info){
		String[] values = info.split(",");
		int[] x_vals = new int[values.length/2];
		int[] y_vals = new int[values.length/2];
		
		for (int i = 0; i < values.length; i += 2){
			x_vals[i/2] = Integer.valueOf(values[i]);
			y_vals[i/2] = Integer.valueOf(values[i+1]);
		}
		
		return new Polygon(x_vals, y_vals, values.length/2);
	}
	
	public void initPlayers(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		InputStream stream = getClass().getResourceAsStream("/resources/player_info.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try{
			String line = null;
			while ((line = reader.readLine()) != null){
				String[] info = line.split("\\|");
				String name = info[0];
				int ID = Integer.valueOf(info[1]);
				int red = Integer.valueOf(info[2]);
				int green = Integer.valueOf(info[3]);
				int blue = Integer.valueOf(info[4]);
				Color c = new Color(red,green,blue);
				players.add(new Player(name,c,ID));
			}
		}
		catch(NumberFormatException | IOException e){
			e.printStackTrace();
		}
		
		this.gui.getRisk().setPlayers(players);
	}
}
