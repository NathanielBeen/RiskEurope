package guiMethods;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gameMethods.Player;
import unitsCombat.Combat;
import unitsCombat.Unit;

public class CombatGUI extends JFrame{
	public static final int DICE = 0;
	public static final int ENTER_CASUALTY = 1;
	public static final int CASUALTY = 2;
	public static final int END_COMBAT = 3;
	
	private GameGUI gui;
	private Combat combat;
	private int gui_state;
	private int curr_rank;
	
	private int[] attacker_cas;
	private int[] defender_cas;
	private int[] hits;
	
	BufferedImage combat_img;
	BufferedImage reversed_combat_img;
	BufferedImage nameplates;
	BufferedImage unit_img;
	
	private JPanel top;
	private JPanel main;
	
	private JPanel attacker;
	private JPanel defender;
	private JPanel center;
	
	private JPanel bottom;

	/***********************
	 * Constructor         *
	 ***********************/
	public CombatGUI(GameGUI gui, Combat combat){
		this.gui = gui;
		this.combat = combat;
		this.gui_state = DICE;
		this.curr_rank = this.combat.detNextRank(Combat.FOOTMAN);
		
		this.attacker_cas = new int[4];
		this.defender_cas = new int[4];
		this.hits = new int[2];
		
		this.unit_img = this.gui.getGameInfo().getMapUnitImage();
		
		try{
			this.combat_img = ImageIO.read(getClass().getResource("/resources/combat_unit_panel.png"));
			this.reversed_combat_img = ImageIO.read(getClass().getResource("/resources/reversed_combat_unit_panel.png"));
			this.nameplates = ImageIO.read(getClass().getResource("/resources/nameplates.png"));
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		this.top = createTopPanel();
		genMainPanelComponents();
		this.main = genMainPanel();
		this.bottom = genStandardPanel();
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(top, BorderLayout.NORTH);
		contentPane.add(main, BorderLayout.CENTER);
		contentPane.add(bottom, BorderLayout.SOUTH);
		setContentPane(contentPane);
		
		pack();
	}

	public void updateGuiStatePanels(){
		getContentPane().removeAll();
		if (this.gui_state == DICE){
			this.main.removeAll();
			genMainPanelComponents();
			this.main = genMainPanel();
			this.top = createTopPanel();
		}
		else if (this.gui_state == ENTER_CASUALTY){
			this.bottom = genBeginCasualtyPanel();
		}
		else if (this.gui_state == CASUALTY){
			this.bottom = genStandardPanel();
			
			this.main.removeAll();
			casualtySelectionPanel();
			if (this.gui_state != CASUALTY){
				return;
			}
			this.center = new JPanel();
			this.main = genMainPanel();
		}
		else if (this.gui_state == END_COMBAT){
			this.main.removeAll();
			this.attacker = genVictorPanel();
			this.defender = genVictorPanel();
			this.center = new JPanel();
			this.main = genMainPanel();
			
			this.bottom = genEndPanel();
		}
		getContentPane().add(this.top, BorderLayout.NORTH);
		getContentPane().add(this.main, BorderLayout.CENTER);
		getContentPane().add(this.bottom, BorderLayout.SOUTH);
		pack();
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public GameGUI getGui(){
		return this.gui;
	}
	
	public Combat getCombat(){
		return this.combat;
	}
	
	public void setCombat(Combat c){
		this.combat = c;
	}
	
	/***********************
	 * Top Panel Creation  *
	 ***********************/
	public JPanel createTopPanel(){
		JPanel top = new JPanel();
		top.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .5;
		c.weighty = 1;
		top.add(genUnitPanel(this.combat.getAttacker()),c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = .5;
		c.weighty = 1;
		top.add(genUnitPanel(this.combat.getDefender()), c);
		
		return top;
	}
	
	public ImagePanel genUnitPanel(Player p){
		BufferedImage img = new BufferedImage(this.combat_img.getWidth(), this.combat_img.getHeight(), this.combat_img.getType());
		Graphics2D g = img.createGraphics();
		g.setComposite(AlphaComposite.SrcOver);
		
		BufferedImage player_name = this.nameplates.getSubimage(102*p.getID()+4, 4, 94, 67);
		
		ArrayList<Unit> army = new ArrayList<Unit>();
		int x_cor = 0;
		int y_cor = 9;
		int y_cor_2 = 45;
		
		if (p.equals(this.combat.getAttacker())){
			g.drawImage(this.combat_img, 0, 0, null);
			g.drawImage(player_name, 9, y_cor, null);
			army = this.combat.getAttackingUnits();
			x_cor = 116;
		}
		else if (p.equals(this.combat.getDefender())){
			g.drawImage(this.reversed_combat_img, 0, 0, null);
			g.drawImage(player_name, 162, y_cor, null);
			army = this.combat.getDefendingUnits();
			x_cor = 9;
		}
		int[] count = genUnitCount(army);
		
		drawUnitImages(g, p, x_cor, y_cor);
		drawUnitNumbers(g, x_cor, y_cor_2, count);
		
		ImagePanel unit_panel = new ImagePanel(this.gui.getGameInfo().getBackByPlayerID(p.getID()));
		unit_panel.setLayout(new BorderLayout());
		
		ImageIcon icon = new ImageIcon(img);
		JLabel center = new JLabel();
		center.setIcon(icon);
		unit_panel.add(center, BorderLayout.CENTER);
		
		return unit_panel;
	}
	
	public void drawUnitImages(Graphics2D g, Player p, int x_cor, int y_cor){
		for (int i = 0; i < 4; i++){
			BufferedImage unit = this.unit_img.getSubimage(32*i, 32*p.getID(), 32, 32);
			g.drawImage(unit, x_cor, y_cor, null);
			x_cor += 36;
		}
	}
	
	public void drawUnitNumbers(Graphics2D g, int x_cor, int y_cor, int[] unit_count){
		Font f = new Font("Arial", Font.BOLD, 20);
		g.setFont(f);
		g.setColor(Color.BLACK);
		
		FontRenderContext frc = g.getFontRenderContext();

		for (int i = 0; i < 4; i++){
			GlyphVector c = f.createGlyphVector(frc,  Integer.toString(unit_count[i]));
			Rectangle2D r = c.getVisualBounds();
			int center_x = (int) r.getWidth()/2;
			int center_y = (int) r.getHeight()/2;
			
			int text_x = (int)(16-r.getX()-center_x+x_cor);
			int text_y = (int)(16-r.getY()-center_y+y_cor);
			g.drawString(Integer.toString(unit_count[i]), text_x, text_y);
			x_cor += 36;
		}

	}
	
	public int[] genUnitCount(ArrayList<Unit> a){
		int[] count = {0,0,0,0};
		for (Unit u : a){
			count[u.getType()]++;
		}
		return count;
	}
	
	public int genTotalCount(int[] units){
		int total = 0;
		for (int i : units){
			total += i;
		}
		return total;
	}
	
	/***********************
	 * Main Panel Creation *
	 ***********************/
	public void genMainPanelComponents(){
		
		int crit = 0;
		String name = "Footman";
		switch(this.curr_rank){
			case Combat.ARCHER:
				name = "Archer";
				crit = 3;
				break;
			case Combat.CAVALRY:
				name = "Cavalry";
				crit = 5;
				break;
			case Combat.SIEGE:
				name = "Siege";
				crit = 3;
				break;
			default:
				break;
		}
		
		this.attacker = new DiceGUI(this, this.combat.getAttacker(), false, this.combat.genAttackDice(this.curr_rank), crit, name);
		this.defender = new DiceGUI(this, this.combat.getDefender(), this.combat.castleUsedThisRound(), this.combat.genDefenseDice(this.curr_rank), crit, name);
		this.center = new JPanel();
	}
	
	public JPanel genMainPanel(){
		JPanel main = new JPanel();
		main.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .4;
		c.weighty = 1;
		main.add(this.attacker,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = .2;
		c.weighty = 1;
		main.add(this.center,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = .4;
		c.weighty = 1;
		main.add(this.defender,c);
		
		return main;
	}
	
	public JPanel genVictorPanel(){
		ImagePanel panel;
		JLabel label;
		
		if (this.combat.getAttackingUnits().isEmpty()){
			panel = new ImagePanel(this.gui.getGameInfo().getBackByPlayerID(this.combat.getDefender().getID()));
			label = new JLabel("Victory for "+this.combat.getDefender().getName(), SwingConstants.CENTER);
		}
		else{
			panel = new ImagePanel(this.gui.getGameInfo().getBackByPlayerID(this.combat.getAttacker().getID()));
			label = new JLabel("Victory for "+this.combat.getAttacker().getName(), SwingConstants.CENTER);
		}
		
		panel.setLayout(new BorderLayout());
		panel.add(label);
		
		return panel;
	}
	
	/*************************
	 * Bottom Panel Creation *
	 *************************/
	public JPanel genStandardPanel(){
		return new ImagePanel(getGui().getGameInfo().getBlackBack());
	}
	
	public JPanel genBeginCasualtyPanel(){
		ImagePanel panel = new ImagePanel(getGui().getGameInfo().getBlackBack());
		panel.setLayout(new BorderLayout());
		JButton begin = new JButton("Begin Casualty Selection");
		begin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				changeToCasualty();
			}
		});
		panel.add(begin, BorderLayout.CENTER);
		return panel;
	}
	
	public void changeToCasualty(){
		this.gui_state = CASUALTY;
		updateGuiStatePanels();
	}
	
	public void casualtySelectionPanel(){
		int[] hits = this.combat.determineHits(this.combat.getAttackingRolls(), this.combat.getDefendingRolls(), this.curr_rank);
		if (hits[0] == 0 && hits[1] == 0){
			resetValues();
			
			this.curr_rank = this.combat.detNextRank(this.curr_rank);
			this.gui_state = DICE;
			updateGuiStatePanels();
			return;
		}
		if (hits[1] > genTotalCount(this.combat.getAttackingUnitCount())){
			hits[1] = genTotalCount(this.combat.getAttackingUnitCount());
		}
		if (hits[0] > genTotalCount(this.combat.getDefendingUnitCount())){
			hits[0] = genTotalCount(this.combat.getDefendingUnitCount());
		}
		if (hits[1] != 0){
			this.attacker = new CasualtyPanel(this, hits[1], this.combat.getAttacker(), this.combat.getAttackingUnitCount());
		}
		if (hits[0] != 0){
			this.defender = new CasualtyPanel(this, hits[0], this.combat.getDefender(), this.combat.getDefendingUnitCount());
		}
		this.hits = hits;
	}
	
	public JPanel genEndPanel(){
		ImagePanel panel = new ImagePanel(getGui().getGameInfo().getBlackBack());
		panel.setLayout(new BorderLayout());
		
		JButton end = new JButton("End Combat");
		end.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				endCombatGui();
			}
		});
		panel.add(end, BorderLayout.CENTER);
		
		return panel;
	}
	
	/*************************
	 * Combat Methods        *
	 *************************/
	public void setRoll(Player p, ArrayList<Integer> rolls){
		if (p.equals((combat.getAttacker()))){
			this.combat.setAttackingRolls(rolls);
		}
		else if (p.equals(combat.getDefender())){
			this.combat.setDefendingRolls(rolls);
		}
		if (this.combat.getAttackingRolls().size() == this.combat.genAttackDice(this.curr_rank) && 
		    this.combat.getDefendingRolls().size() == this.combat.genDefenseDice(this.curr_rank)){
			this.gui_state = ENTER_CASUALTY;
			updateGuiStatePanels();
		}
	}
	
	public void useReroll(){
		this.combat.setCastleUsedThisRound(true);
		updateGuiStatePanels();
	}
	
	public void selectCasualties(Player p, int[] casualties){
		if (p.equals(combat.getAttacker())){
			this.attacker_cas = casualties;
		}
		else if (p.equals(combat.getDefender())){
			this.defender_cas = casualties;
		}
		if (genTotalCount(this.attacker_cas) == this.hits[1] && genTotalCount(this.defender_cas) == this.hits[0]){
			this.combat.takeCasualties(attacker_cas, defender_cas);
			boolean end = this.combat.determineCombatEnd();
			if (end){
				this.gui_state = END_COMBAT;
				updateGuiStatePanels();
			}
			else{
				resetValues();
				this.curr_rank = this.combat.detNextRank(this.curr_rank);
				this.gui_state = DICE;
				updateGuiStatePanels();
			}
		}
	}
	
	public void endCombatGui(){
		this.gui.getRisk().getCombatQueue().remove(0);
		this.gui.getMap().resetMap();
		this.gui.changeGuiState("");
		dispose();
	}
	
	public void resetValues(){
		this.hits = new int[2];
		this.attacker_cas = new int[4];
		this.defender_cas = new int[4];
		this.combat.setAttackingRolls(new ArrayList<Integer>());
		this.combat.setDefendingRolls(new ArrayList<Integer>());
	}
}
	
