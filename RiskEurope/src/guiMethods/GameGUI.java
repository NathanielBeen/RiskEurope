package guiMethods;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import actions.*;
import gameMethods.GameHash;
import gameMethods.GameInfo;
import gameMethods.Player;
import gameMethods.RiskEurope;
import gameMethods.Territory;
import unitsCombat.Combat;
import unitsCombat.Unit;

public class GameGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private GameInfo info = new GameInfo();
	private RiskEurope risk = new RiskEurope(info);
	private MapGUI map = new MapGUI(this);
	private SideGUI side = new SideGUI(this);
	private ButtonGUI buttons = new ButtonGUI(this);
	
	private GameHash hash = new GameHash();
	private GameInit init = new GameInit(this, hash);
	
	private BottomGUI bottom = new BottomGUI(this);
	
	private GuiAction curr_action;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameGUI frame = new GameGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GameGUI() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
		GridBagConstraints c = new GridBagConstraints();
		
		contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .15;
		c.weighty = 1;
		contentPane.add(side, c);

		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = .85;
		c.weighty = .95;
		contentPane.add(map, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = .75;
		c.weighty = .05;
		contentPane.add(bottom, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = .1;
		c.weighty = .05;
		contentPane.add(buttons, c);
		
		GuiMouseListener listener = new GuiMouseListener(this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		setContentPane(contentPane);
		pack();
		
		beginGuiPlacement();
		this.side.updatePanel();
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public GameInfo getGameInfo(){
		return this.info;
	}
	
	public MapGUI getMap(){
		return this.map;
	}
	
	public SideGUI getSide(){
		return this.side;
	}
	
	public ButtonGUI getButtons(){
		return this.buttons;
	}
	
	public GameHash getHash(){
		return this.hash;
	}
	
	public RiskEurope getRisk(){
		return this.risk;
	}
	
	public GuiAction getGuiAction(){
		return this.curr_action;
	}
	
	public void setGuiAction(GuiAction g){
		this.curr_action = g;
	}
	
	public void nullGuiAction(){
		this.curr_action = null;
	}
	
	/***********************
	 * Button Methods      *
	 ***********************/
	public void resolveAction(){
		this.curr_action.getAction().resolveAction();
		for (Player p : getRisk().getPlayers()){
			p.calcNumCrowns();
		}
		this.map.setHomeTerritory(null);
		this.map.resetMap();
		changeGuiState("Select next Action");
	}
	
	public void selectGuiActionFromCard(int i){
		if (!getRisk().validateActionSelection(i)) { return; }
		getRisk().selectActionFromCard(i);
		this.curr_action = createGuiActionFromAction(getRisk().getCurrentAction());
		this.side.getCardPanel().clearSelection();
		repaint();
		
		this.curr_action.getNextGuiAction();
	}
	
	public GuiAction createGuiActionFromAction(Action a){
		if (a instanceof Expand){
			return new GuiExpand((Expand) a, this);
		}
		else if (a instanceof Maneuver){
			return new GuiManeuver((Maneuver) a, this);
		}
		else if (a instanceof SplitExpand){
			return new GuiSplitExpand((SplitExpand) a, this);
		}
		else if (a instanceof Tax){
			return new GuiTax((Tax) a, this);
		}
		else if (a instanceof Spend){
			return new GuiSpend((Spend) a, this);
		}
		else if (a instanceof KingMe){
			return new GuiKingMe((KingMe) a, this);
		}
		else if (a instanceof Fortify){
			return new GuiFortify((Fortify) a, this);
		}
		else if (a instanceof SiegeAssault){
			return new GuiSiegeAssault((SiegeAssault) a, this);
		}
		else if (a instanceof Placement){
			return new GuiPlacement((Placement) a, this);
		}
		else{
			return null;
		}
	}
	
	/**************************
	 * State Specific Methods *
	 **************************/
	public void changeGuiState(String label){
		
		this.bottom.setActionString(label);
		if (this.info.getGameState() == GameInfo.COMBAT){
			Combat c = this.risk.genNextCombat();
			if (c == null){ 
				if (this.info.getGameState() != GameInfo.COMBAT){
					changeGuiState("");
				}
				return;
			}
			
			CombatGUI gui = new CombatGUI(this,c);
			gui.setVisible(true);
		}
		else if (this.info.getGameState() == GameInfo.PLACEMENT && this.info.getSelectionState() == GameInfo.NO_SELECTION){
			
			this.curr_action = createGuiActionFromAction(this.risk.getCurrentAction());
			this.curr_action.getNextGuiAction();
		}
		else if (this.info.getGameState() == GameInfo.INIT_CARD_SELECTION){
			this.curr_action = null;
			HandGUI hand = new HandGUI(this, this.risk.getCurrentPlayer().getHand());
			hand.setVisible(true);
		}
		else if (this.info.getSelectionState() == GameInfo.SELECT_UNITS){
			ArrayList<Unit> units =  new ArrayList<Unit>();
			int min = (int) this.risk.getCurrentOptions().get(0);
			int max = (int) this.risk.getCurrentOptions().get(1);
			this.risk.getCurrentOptions().remove(0);
			this.risk.getCurrentOptions().remove(0);
			
			for (Object o : this.risk.getCurrentOptions()){
				units.add((Unit) o);
			}
			UnitSelectGUI unit = new UnitSelectGUI(this, this.risk.getCurrentPlayer(), units, min, max, label);
			unit.setVisible(true);
		}
		else if (this.info.getSelectionState() == GameInfo.SELECT_ACTION){
			this.side.updatePanel();
		}
		else if (this.info.getSelectionState() == GameInfo.PURCHASE){
			MainPurchaseGUI p = new MainPurchaseGUI(this, this.risk.getCurrentPlayer(), this.risk.getCurrentPlayer().getCoins());
			p.setVisible(true);
		}
		else if (this.info.getSelectionState() == GameInfo.SELECT_TERRITORY ||
				 this.info.getSelectionState() == GameInfo.SELECT_TAX){
			ArrayList<GuiTerritory> options = new ArrayList<GuiTerritory>();
			if (this.risk.getCurrentOptions().size() != 0 && this.risk.getCurrentOptions().get(0) instanceof Territory){
				for (Object o : this.risk.getCurrentOptions()){
					options.add(this.hash.getGuiFromTerritory((Territory) o));
				}
			}
			this.map.setTerritoryOptions(options);
		}
		else if (this.info.getGameState() == GameInfo.GAME_END){
			
		}
		
		repaint();
	}
	
	/**************************
	 * Start Methods *
	 **************************/
	public void beginGuiPlacement(){
		this.risk.beginPlacement();
		this.curr_action = createGuiActionFromAction(this.risk.getCurrentAction());
		this.curr_action.getNextGuiAction();
	}
}
