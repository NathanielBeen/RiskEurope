package actions;

import java.util.ArrayList;

import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.RiskEurope;

public class KingMe implements Action{
	private RiskEurope risk;
	private GameInfo info;
	
	private Player player;

	/***********************
	 * Constructor         *
	 ***********************/
	public KingMe(RiskEurope risk, Player p){
		this.risk = risk;
		this.info = risk.getInfo();
		this.player = p;
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public Player getPlayer(){
		return this.player;
	}
	
	@Override
	public void getNextAction(){
		this.info.setSelectionState(GameInfo.CONFIRM_ACTION);
	}
	
	@Override
	public void selectOption(Object o){
		
	}
	
	@Override
	public void resolveAction(){
		Player a = this.risk.getPlayerByTurnOrder(0);
		Player b = this.risk.getPlayerByTurnOrder(1);
		Player c = this.risk.getPlayerByTurnOrder(2);
		Player d = this.risk.getPlayerByTurnOrder(3);
		if (this.player.equals(a)){
			
		}
		else if (this.player.equals(b)){
			b.setTurnOrder(0);
			a.setTurnOrder(1);
		}
		else if (this.player.equals(c)){
			c.setTurnOrder(0);
			b.setTurnOrder(2);
			a.setTurnOrder(1);
		}
		else if (this.player.equals(d)){
			d.setTurnOrder(0);
			c.setTurnOrder(3);
			b.setTurnOrder(2);
			a.setTurnOrder(1);
		}
		this.risk.getNextAction();
	}
	
	@Override
	public void resetAction(){
		
	}
}
