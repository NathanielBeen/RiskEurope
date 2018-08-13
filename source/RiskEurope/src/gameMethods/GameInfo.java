package gameMethods;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GameInfo {
	//game states
	private int game_state;
	public static final int GAME_START = 0;
	public static final int PLACEMENT = 1;
	public static final int INIT_CARD_SELECTION = 2;//in gamegui
	public static final int TURN = 3;
	public static final int COMBAT = 4;
	public static final int GAME_END = 5;
	
	//selection states
	private int selection_state;
	public static final int NO_SELECTION = 0;
	public static final int SELECT_ACTION = 1;//in gamegui, mouselistener
	public static final int SELECT_TERRITORY = 2;//in mouselistener
	public static final int SELECT_UNITS = 3;//in gamegui
	public static final int SELECT_TAX = 4;//in mouselistener
	public static final int CONFIRM_ACTION = 5;//in gamegui
	public static final int PURCHASE = 6;//in gamegui
	
	private BufferedImage map_unit_img;
	private BufferedImage gui_unit_img;
	private BufferedImage card_back;
	private BufferedImage empty_selection;
	
	private BufferedImage action_img;
	private BufferedImage action_highlight_img;
	private BufferedImage action_select_img;
	private BufferedImage action_option_img;
	
	private BufferedImage highlight_img;
	private BufferedImage select_img;
	private BufferedImage castle_img;
	
	private BufferedImage red_back;
	private BufferedImage blue_back;
	private BufferedImage green_back;
	private BufferedImage yellow_back;
	private BufferedImage black_back;
	
	private BufferedImage dice_img;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GameInfo(){
		this.game_state = PLACEMENT;
		this.selection_state = NO_SELECTION;
		try {
			this.map_unit_img = ImageIO.read(getClass().getResource("/resources/units.png"));
			this.gui_unit_img = ImageIO.read(getClass().getResource("/resources/panel_units.png"));
			this.card_back = ImageIO.read(getClass().getResource("/resources/card_base.png"));
			this.empty_selection = ImageIO.read(getClass().getResource("/resources/empty_selection.png"));
			
			this.action_img = ImageIO.read(getClass().getResource("/resources/card_icons.png"));
			this.action_highlight_img = ImageIO.read(getClass().getResource("/resources/action_highlight.png"));
			this.action_select_img = ImageIO.read(getClass().getResource("/resources/action_select.png"));
			this.action_option_img = ImageIO.read(getClass().getResource("/resources/action_option.png"));
			
			this.highlight_img = ImageIO.read(getClass().getResource("/resources/card_highlight.png"));
			this.select_img = ImageIO.read(getClass().getResource("/resources/card_select.png"));
			this.castle_img = ImageIO.read(getClass().getResource("/resources/map_castle.png"));
			
			this.red_back = ImageIO.read(getClass().getResource("/resources/red_back.png"));
			this.blue_back = ImageIO.read(getClass().getResource("/resources/blue_back.png"));
			this.green_back = ImageIO.read(getClass().getResource("/resources/green_back.png"));
			this.yellow_back = ImageIO.read(getClass().getResource("/resources/yellow_back.png"));
			this.black_back = ImageIO.read(getClass().getResource("/resources/black_back.png"));
			
			this.dice_img = ImageIO.read(getClass().getResource("/resources/dice.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public int getGameState(){
		return this.game_state;
	}
	
	public void setGameState(int i){
		this.game_state = i;
	}
	
	public int getSelectionState(){
		return this.selection_state;
	}
	
	public void setSelectionState(int i){
		this.selection_state = i;
	}
	
	public BufferedImage getMapUnitImage(){
		return this.map_unit_img;
	}
	
	public BufferedImage getGuiUnitImage(){
		return this.gui_unit_img;
	}
	
	public BufferedImage getCardBackImage(){
		return this.card_back;
	}
	public BufferedImage getEmptySelectionImage(){
		return this.empty_selection;
	}
	
	public BufferedImage getActionImage(){
		return this.action_img;
	}
	
	public BufferedImage getActionHighlightImage(){
		return this.action_highlight_img;
	}
	
	public BufferedImage getActionSelectImage(){
		return this.action_select_img;
	}
	
	public BufferedImage getActionOptionImage(){
		return this.action_option_img;
	}
	
	public BufferedImage getHighlightImage(){
		return this.highlight_img;
	}
	
	public BufferedImage getSelectImage(){
		return this.select_img;
	}
	
	public BufferedImage getCastleImage(){
		return this.castle_img;
	}
	
	public BufferedImage getBackByPlayerID(int id){
		switch (id){
			case 0:
				return this.red_back;
			case 1:
				return this.blue_back;
			case 2:
				return this.yellow_back;
			case 3:
				return this.green_back;
			default:
				return null;
		}
	}
	
	public BufferedImage getBlackBack(){
		return this.black_back;
	}
	
	public BufferedImage getDiceImage(){
		return this.dice_img;
	}
}
