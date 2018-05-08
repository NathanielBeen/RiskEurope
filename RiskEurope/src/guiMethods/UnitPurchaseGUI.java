package guiMethods;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UnitPurchaseGUI extends UnitGUI{
	private static final long serialVersionUID = 1L;
	private MainPurchaseGUI gui;
	private int unit_type;
	private int cost;
	private int prev_value;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public UnitPurchaseGUI(MainPurchaseGUI gui, int unit_type, int num_units, BufferedImage unit_img){
		super(unit_type, num_units, unit_img);
		this.gui = gui;
		this.prev_value = 0;
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public JLabel genTopLabel(){
		String type_string;
		switch(getUnitType()){
		case 0:
			type_string = "Footmen - 1 Coin";
			this.cost = 1;
			break;
		case 1:
			type_string = "Archer - 2 Coins";
			this.cost = 2;
			break;
		case 2:
			type_string = "Knight - 3 Coins";
			this.cost = 3;
			break;
		case 3:
			type_string = "Seige - 10 Coins";
			this.cost = 10;
			break;
		case 4:
			type_string = "Castle - 12 Coins";
			this.cost = 12;
			break;
		default:
			type_string = "";
			this.cost = 0;
			break;
		}
		
		return new JLabel(type_string);
	}
	
	@Override
	public JPanel genInfoPanel(){
		String attack_str;
		String rank_str;
		String dice_str;
		String other_str;
		switch(getUnitType()){
			case 0:
				attack_str = "Hits in general combat";
				rank_str = "Attacks with general";
				dice_str = "Attacks with 1 die";
				other_str = "";
				break;
			case 1:
				attack_str = "Hits on a 5+";
				rank_str = "Attacks Second";
				dice_str = "Attacks with 1 die";
				other_str = "";
				break;
			case 2:
				attack_str = "Hits on a 3+";
				rank_str = "Attacks Third";
				dice_str = "Attacks with 1 die";
				other_str = "";
				break;
			case 3:
				attack_str = "Hits on a 3+";
				rank_str = "Attacks First";
				dice_str = "Attacks with 2 dice";
				other_str = "Allows attacks on castles";
				break;
			case 4:
				attack_str = "Can reroll one set of dice in combat";
				rank_str = "Forces other side to have a Seige Weapon";
				dice_str = "Allows production of units in territory";
				other_str = "";
				break;
			default:
				attack_str = "";
				rank_str = "";
				dice_str = "";
				other_str = "";
				break;
		}
		JPanel info = new JPanel();
		info.setLayout(new GridLayout(0,1,5,5));
		
		JLabel attack = new JLabel(attack_str);
		attack.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		JLabel rank = new JLabel(rank_str);
		rank.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		JLabel dice = new JLabel(dice_str);
		dice.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		JLabel other = new JLabel(other_str);
		other.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		info.add(attack);
		info.add(rank);
		info.add(dice);
		info.add(other);
		
		return info;
	}
	
	@Override
	public void selectionChange(int selected){
		int diff = selected - this.prev_value;
		if (diff < 0){
			this.gui.setAvailableCoin(this.gui.getAvailableCoin() + this.cost*(-diff));
			this.gui.updateCoinLabels();
			this.prev_value = selected;
		}
		else if (this.gui.getAvailableCoin() - this.cost*diff >= 0){
			this.gui.setAvailableCoin(this.gui.getAvailableCoin() - this.cost*diff);
			this.gui.updateCoinLabels();
			this.prev_value = selected;
		}
		else{
			getComboBox().setSelectedItem(this.prev_value);
		}
	}
}
