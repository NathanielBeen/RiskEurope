package actions;

import gameMethods.Player;

public interface Action {
	//action enum
	public static final int NONE = 0;
	public static final int EXPAND = 1;
	public static final int MANEUVER = 2;
	public static final int SPLIT_EXPAND = 3;
	public static final int TAX = 4;
	public static final int SPEND = 5;
	public static final int KING_ME = 6;
	public static final int FORTIFY = 7;
	public static final int SIEGE_ASSAULT = 8;
	public static final int PLACEMENT = 9;
	
	public Player getPlayer();
	public void getNextAction();
	public void selectOption(Object o);
	public void resolveAction();
	public void resetAction();
}
