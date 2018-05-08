package guiMethods;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import actions.GuiSiegeAssault;
import actions.SiegeAssault;
import gameMethods.Territory;
import unitsCombat.Unit;

public class AssualtCombatGUI extends JFrame {
	private GameGUI gui;
	private GuiSiegeAssault siege;
	private JPanel contentPane;
	private JPanel main;
	private JPanel bottom;
	
	private Territory attacked_terr;
	private int hits;
	private int[] rolls;
	private int[] casualties;

	/***********************
	 * Constructor         *
	 ***********************/
	public AssualtCombatGUI(GameGUI gui, GuiSiegeAssault siege, Territory t, int num_siege) {
		this.gui = gui;
		this.siege = siege;
		this.attacked_terr = t;
		this.hits = 0;
		this.rolls = rollDice(num_siege*2);
		this.casualties = new int[4];
		
		this.bottom = new ImagePanel(this.gui.getGameInfo().getBlackBack());
		this.main = mainPanel();
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(this.main, BorderLayout.CENTER);
		setContentPane(contentPane);
		
		pack();
	}
	
	/***********************
	 * Main Panel Methods  *
	 ***********************/
	public JPanel mainPanel(){
		JPanel panel = new ImagePanel(this.gui.getGameInfo().getBlackBack());
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = .2;
		panel.add(genTopPanel(),c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = .5;
		c.weighty = .6;
		panel.add(genDicePanel(), c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = .6;
		panel.add(genCasualtyPanel(),c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = .2;
		panel.add(this.bottom,c);
		
		return panel;
	}
	
	public JPanel genTopPanel(){
		JPanel top = new JPanel();
		JLabel label = new JLabel("Siege Assualt on "+this.attacked_terr.getName());
		top.add(label);
		return top;
	}
	
	public JPanel genDicePanel(){
		JPanel dice_panel = new JPanel();
		dice_panel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		for (int i : this.rolls){
			JLabel die_label = new JLabel();
			BufferedImage die = genDieImage(i);
			die_label.setIcon(new ImageIcon(die));
			dice_panel.add(die_label);
		}
		
		return dice_panel;
	}
	
	public JPanel genCasualtyPanel(){
		return new AssualtCasualtyPanel(this, this.hits, this.attacked_terr.getOwner(), genUnitCount(this.attacked_terr.getDefendingArmy()));
	}
	
	public JPanel genConfirmPanel(){
		JPanel panel = new JPanel();
		JButton btn = new JButton("Confirm Assualt Combat");
		btn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				confirmAction();
			}
		});
		panel.add(btn, BorderLayout.CENTER);
		
		return panel;
	}
	
	public BufferedImage genDieImage(int i){
		int x = (i-1)*32;
		int y = 0;
		if (i > 2){
			y = 32;
		}
		else{
			y = 64;
		}
		return this.gui.getGameInfo().getDiceImage().getSubimage(x,y,32,32);
	}
	
	public int[] rollDice(int num_rolls){
		int[] rolls = new int[num_rolls];
		for (int i = 0; i < num_rolls; i++){
			rolls[i] = ThreadLocalRandom.current().nextInt(1,7);
			if (rolls[i] > 2){
				hits++;
			}
		}
		return rolls;
	}
	
	public void selectCasualties(int[] casualties){
		this.casualties = casualties;
		this.bottom = genConfirmPanel();
		this.main = mainPanel();
		
		getContentPane().removeAll();
		getContentPane().add(this.main,BorderLayout.CENTER);
		pack();
	}
	
	public int[] genUnitCount(ArrayList<Unit> army){
		int[] count = new int[]{0,0,0,0};
		for (Unit u : army){
			count[u.getType()]++;
		}
		return count;
	}
	
	public void confirmAction(){
		ArrayList<Unit> cas = this.gui.getRisk().convertCountToTerritoryUnits(this.attacked_terr.getOwner(), 
				              this.attacked_terr, this.casualties);
		SiegeAssault s = (SiegeAssault) this.siege.getAction();
		s.setCasualties(cas);
		this.gui.resolveAction();
		dispose();
	}
}
