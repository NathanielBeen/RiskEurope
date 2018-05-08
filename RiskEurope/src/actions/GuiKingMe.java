package actions;

import guiMethods.GameGUI;

public class GuiKingMe implements GuiAction{
	private KingMe k;
	private GameGUI gui;
	private String label;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public GuiKingMe(KingMe k, GameGUI gui){
		this.k = k;
		this.gui = gui;
		this.label = "Confirm action";
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void getNextGuiAction(){
		k.getNextAction();
		this.gui.changeGuiState(this.label);
	}
	
	@Override
	public void selectGuiOption(Object o){
		
	}
	
	@Override
	public void resetGuiAction(){
		
	}
	
	@Override
	public Action getAction(){
		return this.k;
	}
}
