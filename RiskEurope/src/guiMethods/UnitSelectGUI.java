package guiMethods;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gameMethods.Player;
import unitsCombat.Unit;

public class UnitSelectGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private GameGUI gui;

	private JPanel contentPane;
	private ArrayList<UnitGUI> panels;
	
	private Player player;
	private BufferedImage unit_img;
	private ArrayList<Unit> units;
	private int min_selected;
	private int max_selected;

	/**
	 * Create the frame.
	 */
	public UnitSelectGUI(GameGUI gui, Player p, ArrayList<Unit> units, int min_selected, int max_selected, String label) {
		this.gui = gui;
		this.panels = new ArrayList<UnitGUI>();
		
		this.player = p;
		this.unit_img = this.gui.getGameInfo().getGuiUnitImage();
		this.units = units;
		this.min_selected = min_selected;
		this.max_selected = max_selected;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridBagLayout());
		setContentPane(contentPane);
		
		JLabel top = new JLabel(label);
		
		JPanel center = new JPanel();
		center.setLayout(new FlowLayout());
		
		int[] count = genUnitCount();
		int index = 0;
		for (int i : count){
			if (i != 0){
				BufferedImage panel_img = getUnitImage(index);
				UnitGUI unit = new UnitGUI(index, i, panel_img);
				this.panels.add(unit);
				center.add(unit);
			}
			index ++;
		}
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(1,0,5,5));
		
		JButton reset = new JButton("Reset Selection");
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				resetSelection();
			}
		});
		bottom.add(reset);
		
		JButton confirm = new JButton("Confirm Selection");
		confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				confirmSelection();
			}
		});
		bottom.add(confirm);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = .2;
		contentPane.add(top,gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = .6;
		contentPane.add(center,gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = .2;
		contentPane.add(bottom,gbc);
		pack();
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public int[] genUnitCount(){
		int[] count = new int[4];
		for (Unit u : this.units){
			int type = u.getType();
			count[type] ++;
		}
		
		return count;
	}
	
	public void resetSelection(){
		for (UnitGUI u : this.panels){
			u.resetSelection();
		}
	}
	
	
	public int[] selectUnitCount(){
		int[] count = new int[4];
		for (UnitGUI u : this.panels){
			count[u.getUnitType()] = u.getNumSelected();
		}
		return count;
	}
	
	public void confirmSelection(){
		int[] selected_units = selectUnitCount();
		int total = 0;
		for (int i : selected_units){
			total += i;
		}
		if (total > max_selected || total < min_selected){ return; }
		
		this.gui.getGuiAction().selectGuiOption(selected_units);
		this.gui.getGuiAction().getNextGuiAction();
		this.gui.getMap().updateMap();
		this.gui.repaint();
		dispose();
	}
	
	public BufferedImage getUnitImage(int type){
		return this.unit_img.getSubimage(type*128, 128*this.player.getID(), 128, 128);
	}

}
