package guiMethods;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import gameMethods.Player;
import unitsCombat.Combat;

public class DiceGUI extends JPanel{
	public final static int UNROLLED = 0;
	public final static int ROLLED = 1;
	
	private CombatGUI combat_gui;
	private int rolled;
	private boolean has_reroll;
	
	private Player player;
	private int num_dice;
	private int crit_value;
	private String unit_name;
	
	private BufferedImage dice_img;
	private ArrayList<Integer> rolls;
	
	private JPanel center_panel;
	private JPanel button_panel;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public DiceGUI(CombatGUI combat_gui, Player p, boolean has_reroll, int num_dice, int crit_value, String unit_name){
		this.combat_gui = combat_gui;
		this.rolled = UNROLLED;
		this.has_reroll = has_reroll;
		
		this.player = p;
		this.num_dice = num_dice;
		this.crit_value = crit_value;
		this.unit_name = unit_name;
		
		this.dice_img = combat_gui.getGui().getGameInfo().getDiceImage();
		this.rolls = new ArrayList<Integer>();
		
		this.center_panel = genCenterPanel();
		this.button_panel = genButtonPanel();
		
		setLayout(new BorderLayout());
		JLabel label = new JLabel("Rolling "+num_dice+" dice for "+unit_name+" units", SwingConstants.CENTER);
		
		add(label, BorderLayout.NORTH);
		add(this.center_panel, BorderLayout.CENTER);
		add(this.button_panel, BorderLayout.SOUTH);
	}
	
	public void updatePanels(){
		removeAll();
		JLabel label = new JLabel("Rolling "+num_dice+" dice for "+unit_name+" units", SwingConstants.CENTER);
		this.center_panel = genCenterPanel();
		this.button_panel = genButtonPanel();
		
		add(label, BorderLayout.NORTH);
		add(this.center_panel, BorderLayout.CENTER);
		add(this.button_panel, BorderLayout.SOUTH);
		revalidate();
		repaint();
	}
	
	/***********************
	 * Panel Methods       *
	 ***********************/
	public JPanel genCenterPanel(){
		ImagePanel main = new ImagePanel(this.combat_gui.getGui().getGameInfo().getBlackBack());
		main.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		
		for (int i = 0; i < num_dice; i++){
			JLabel die_label = new JLabel();
			BufferedImage die = genDieImage(i);
			die_label.setIcon(new ImageIcon(die));
			main.add(die_label);
		}
		
		return main;
	}
	
	public BufferedImage genDieImage(int i){
		int x_cor, y_cor;
		if (this.rolled == UNROLLED){
			x_cor = 0;
			y_cor = 0;
		}
		else{
			int value = this.rolls.get(i);
			x_cor = (value-1)*32;
			
			if (this.crit_value == Combat.GENERAL){
				y_cor = 96;
			}
			else{
				if (value < crit_value){
					y_cor = 64;
				}
				else{
					y_cor = 32;
				}
			}
		}
		return this.dice_img.getSubimage(x_cor, y_cor, 32, 32);
	}
	
	/***********************
	 * Button Panel Methods*
	 ***********************/
	public JPanel genButtonPanel(){
		ImagePanel image = new ImagePanel(this.combat_gui.getGui().getGameInfo().getBlackBack());
		image.setBorder(new EmptyBorder(5,5,5,5));
		image.setLayout(new GridLayout(1,0,5,5));
		
		if (this.rolled == UNROLLED && this.num_dice != 0){
			JButton roll = new JButton("Roll Dice");
			roll.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					rollDice();
				}
			});
			image.add(roll);
		}
		
		else if (this.has_reroll && this.rolled == ROLLED && this.num_dice != 0){
			JButton no_roll = new JButton("Don't Reroll");
			no_roll.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					confirmRoll();
				}
			});
			
			JButton roll = new JButton("Reroll Dice");
			roll.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					useReroll();
				}
			});
			
			image.add(no_roll);
			image.add(roll);
		}
		
		return image;
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public void rollDice(){
		this.rolls = this.combat_gui.getCombat().rollDice(num_dice);
		this.rolled = ROLLED;
		if (!this.has_reroll){
			confirmRoll();
		}
		else{
			updatePanels();
		}
	}
	
	public void useReroll(){
		this.combat_gui.getCombat().setCastleUsedThisRound(true);
		this.has_reroll = false;
		rollDice();
	}
	
	public void confirmRoll(){
		this.combat_gui.setRoll(this.player, this.rolls);
		this.has_reroll = false;
		updatePanels();
	}
}
