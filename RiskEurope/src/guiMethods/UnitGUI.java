package guiMethods;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class UnitGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private int unit_type;
	private int num_units;
	
	BufferedImage unit_img;
	JComboBox<Integer> num_selected;

	/***********************
	 * Constructor         *
	 ***********************/
	public UnitGUI(int unit_type, int num_units, BufferedImage unit_img) {
		this.unit_type = unit_type;
		this.num_units = num_units;
		this.unit_img = unit_img;
		
		setLayout(new GridBagLayout());
		
		JLabel label = genTopLabel();
		
		JLabel icon = new JLabel();
		ImageIcon i = new ImageIcon(this.unit_img);
		icon.setIcon(i);
		icon.setBorder(BorderFactory.createLoweredBevelBorder());
		
		JPanel info_panel = genInfoPanel();
		
		Integer[] n = new Integer[this.num_units + 1];
		for (int j = 0; j < this.num_units+1; j++){
			n[j] = j;
		}
		this.num_selected = new JComboBox<Integer>(n);
		this.num_selected.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e){
				if (e.getStateChange() == ItemEvent.SELECTED){
					Integer selected = (Integer) e.getItem();
					selectionChange(selected);
				}
			}
		});
		this.num_selected.setEditable(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		add(label,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		add(icon,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
		add(info_panel,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 3;
		add(this.num_selected,c);
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public int getUnitType(){
		return this.unit_type;
	}
	
	public int getNumSelected(){
		return (int) this.num_selected.getSelectedItem();
	}
	
	public JComboBox<Integer> getComboBox(){
		return this.num_selected;
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public void resetSelection(){
		this.num_selected.setSelectedItem(0);
	}
	
	public JLabel genTopLabel(){
		String type_string;
		switch(unit_type){
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
		
		return new JLabel(type_string, SwingConstants.CENTER);
	}
	
	public JPanel genInfoPanel(){
		JPanel info = new JPanel();
		return info;
	}
	
	public void selectionChange(int selected){
		
	}
}
