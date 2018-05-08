package guiMethods;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import gameMethods.Card;
import gameMethods.Player;

public class SideGUI extends JPanel {
	private static final long serialVersionUID = 1L;

	private GameGUI gui;
	
	private Player player;
	private Card first_card;
	private Card second_card;
	private ArrayList<Player> turn_order;
	
	private TurnOrderPanel turn_order_panel;
	private CurrentCardPanel card_panel;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public SideGUI(GameGUI gui) {
		this.gui = gui;
		
		this.player = null;
		this.first_card = null;
		this.second_card = null;
		this.turn_order = this.gui.getRisk().getStaticTurnOrder();
		
		this.setLayout(new GridBagLayout());
		this.turn_order_panel = genTurnOrderPanel();
		this.card_panel = genCurrentCardPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = .5;
		add(turn_order_panel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = .5;
		add(card_panel, c);
		
	}
	
	/***********************
	 * Update Methods      *
	 ***********************/
	public void updatePanel(){
		//expand with turn order, current player title, instruction title
		this.turn_order = this.gui.getRisk().getStaticTurnOrder();
		this.player = this.gui.getRisk().getCurrentPlayer();
		this.first_card = this.player.getFirstSelection();
		this.second_card = this.player.getSecondSelection();
		
		this.turn_order_panel.setTurnOrder(this.turn_order);
		this.card_panel.setFirstCard(this.first_card);
		this.card_panel.setSecondCard(this.second_card);
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public GameGUI getGui(){
		return this.gui;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public void setPlayer(Player p){
		this.player = p;
	}
	
	public Card getFirstCard(){
		return this.first_card;
	}
	
	public void setFirstCard(Card c){
		this.first_card = c;
	}
	
	public Card getSecondCard(){
		return this.second_card;
	}
	
	public void setSecondCard(Card c){
		this.second_card = c;
	}
	
	public CurrentCardPanel getCardPanel(){
		return this.card_panel;
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public TurnOrderPanel genTurnOrderPanel(){
		TurnOrderPanel panel = new TurnOrderPanel(this.turn_order, this);
		return panel;
	}
	
	public CurrentCardPanel genCurrentCardPanel(){
		CurrentCardPanel panel = new CurrentCardPanel(this);
		return panel;
	}
	
	public JPanel genCardPanel(Card c){
		JPanel panel = new JPanel();
		if (c != null){
			panel.add(new CardGUI(c, this.gui), BorderLayout.CENTER);
		}
		return panel;
	}
}
