package guiMethods;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import gameMethods.Player;

public class AssualtCasualtyPanel extends CasualtyPanel {
	private AssualtCombatGUI assualt;
	
	public AssualtCasualtyPanel(AssualtCombatGUI gui, int num_hits, Player p, int[] unit_count) {
		super(null, num_hits, p, unit_count);
		this.assualt = gui;
	}
	
	@Override
	public void confirmSelection(){
		int[] casualties = {0,0,0,0};
		int count = 0;
		int index = 0;
		for (JComboBox<Integer> j : getComboArray()){
			casualties[index] = (int) j.getSelectedItem();
			count += (int) j.getSelectedItem();
			index++;
		}
		if (count == getNumHits()){
			this.assualt.selectCasualties(casualties);
		}
		else{
			return;
		}
	}

}
