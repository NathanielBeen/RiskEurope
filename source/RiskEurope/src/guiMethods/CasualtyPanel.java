package guiMethods;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gameMethods.Player;
import unitsCombat.Unit;

public class CasualtyPanel extends JPanel {
	private CombatGUI combat;
	private int num_hits;
	private Player player;
	private int[] unit_count;

	private BufferedImage unit_img;
	private ArrayList<JPanel> panel_array;
	private ArrayList<JComboBox<Integer>> combo_array;
	
	private JLabel selected;
	private JLabel remaining;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public CasualtyPanel(CombatGUI combat, int num_hits, Player player, int[] unit_count) {
		this.combat = combat;
		this.num_hits = num_hits;
		this.player = player;
		this.unit_count = unit_count;
		
		try {
			this.unit_img = ImageIO.read(new File("src/resources/units.png"));
		} catch (IOException e) {
			e.printStackTrace();
		};
		this.panel_array = new ArrayList<JPanel>();
		this.combo_array = new ArrayList<JComboBox<Integer>>();
		
		JLabel top = new JLabel("Select "+num_hits+" Casualties for "+player.getName());
		JPanel center = createCenterPanel();
		JPanel bottom = createBottomPanel();
		
		setLayout(new BorderLayout());
		add(top, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
	}
	
	/*************************
	 * Get and Set Methods   *
	 *************************/
	public ArrayList<JComboBox<Integer>> getComboArray(){
		return this.combo_array;
	}
	
	public int getNumHits(){
		return this.num_hits;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	/*************************
	 * Center Panel Creation *
	 *************************/
	public JPanel createCenterPanel(){
		for (int i = 0; i < this.unit_count.length; i++){
			JPanel select_panel = genSelectionPanel(i, this.unit_count[i]);
			this.panel_array.add(select_panel);
		}
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(0,2,5,5));
		for (JPanel j : this.panel_array){
			center.add(j);
		}
		
		return center;
	}
	/***********************
	 * Unit Panel Creation *
	 ***********************/
	public JPanel genSelectionPanel(int type, int max_unit){
		JPanel unit_panel = new JPanel();
		unit_panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel img = genUnitImage(type);
		JLabel label = genTypeLabel(type);
		JComboBox<Integer> combo = genComboBox(max_unit);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.weightx = .5;
		c.weighty = 1;
		unit_panel.add(img, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.weightx = .5;
		c.weighty = .5;
		unit_panel.add(label,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 1;
		c.weightx = .5;
		c.weighty = .5;
		unit_panel.add(combo,c);
		
		this.combo_array.add(combo);
		
		return unit_panel;
	}
	
	public JLabel genTypeLabel(int type){
		String type_string;
		switch(type){
		case 0:
			type_string = "Footmen";
			break;
		case 1:
			type_string = "Archer";
			break;
		case 2:
			type_string = "Knight";
			break;
		case 3:
			type_string = "Seige";
			break;
		default:
			type_string = "";
			break;
		}
		return new JLabel(type_string);
		
	}
	
	public JLabel genUnitImage(int type){
		JLabel icon_panel = new JLabel();
		
		BufferedImage unit = this.unit_img.getSubimage(32*type, 32*this.player.getID(), 32, 32);
		ImageIcon icon = new ImageIcon(unit);
		icon_panel.setIcon(icon);
		icon_panel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		return icon_panel;
	}
	
	public JComboBox<Integer> genComboBox(int num){
		Integer[] n = new Integer[num+1];
		for (int i = 0; i < num+1; i++){
			n[i] = i;
		}
		JComboBox<Integer> combo = new JComboBox<Integer>(n);
		combo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				selectionChange();
			}
		});
		
		return combo;
	}
	
	/*************************
	 * Bottom Panel Creation *
	 *************************/
	public JPanel createBottomPanel(){
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(2,2,5,5));
		
		this.selected = new JLabel("Selected: 0");
		this.remaining = new JLabel("Remaining: "+this.num_hits);
		
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				resetSelection();
			}
		});
		
		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				confirmSelection();
			}
		});
		
		bottom.add(this.selected);
		bottom.add(reset);
		bottom.add(this.remaining);
		bottom.add(confirm);
		
		return bottom;
	}
	
	/*************************
	 * Other Methods         *
	 *************************/
	public void selectionChange(){
		int num_selected = 0;
		for (JComboBox<Integer> j : this.combo_array){
			num_selected += (int) j.getSelectedItem();
		}
		
		this.selected.setText("Selected: "+num_selected);
		this.remaining.setText("Remaining: "+(this.num_hits-num_selected));
	}
	
	public void resetSelection(){
		for (JComboBox<Integer> j : this.combo_array){
			j.setSelectedItem(0);
		}
		
		this.selected.setText("Selected: 0");
		this.remaining.setText("Remaining: "+this.num_hits);
	}
	
	public void confirmSelection(){
		int[] casualties = {0,0,0,0};
		int count = 0;
		int index = 0;
		for (JComboBox<Integer> j : this.combo_array){
			casualties[index] = (int) j.getSelectedItem();
			count += (int) j.getSelectedItem();
			index++;
		}
		if (count == this.num_hits || count == getTotalCount()){
			this.combat.selectCasualties(this.player, casualties);
		}
		else{
			return;
		}
	}
	
	public int getTotalCount(){
		int total = 0;
		for (int i : this.unit_count){
			total += i;
		}
		return total;
	}
}
