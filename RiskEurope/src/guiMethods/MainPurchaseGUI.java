package guiMethods;

import java.awt.BorderLayout;
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

public class MainPurchaseGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private GameGUI gui;

	private JPanel contentPane;
	private ArrayList<UnitPurchaseGUI> panels;
	
	private JLabel available;
	private JLabel purchased;
	
	private BufferedImage unit_img;
	private Player player;
	
	private int curr_available;
	private int init_available;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public MainPurchaseGUI(GameGUI gui, Player player, int available_coin) {
		this.gui = gui;
		this.unit_img = this.gui.getGameInfo().getGuiUnitImage();
		this.player = player;
		this.panels = new ArrayList<UnitPurchaseGUI>();
		
		this.curr_available = available_coin;
		this.init_available = available_coin;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		ArrayList<Unit> reserve = this.player.getUnitReserve();
		int[] count = genUnitCount(reserve);
		count[4] = 3;
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1,0,5,5));
		for (int i = 0; i < 5; i ++){
			UnitPurchaseGUI p = new UnitPurchaseGUI(this, i, count[i], genUnitImage(i));
			this.panels.add(p);
			center.add(p);
		}
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(1,0,5,5));
		
		available = new JLabel("Available Coins: "+this.curr_available);
		purchased = new JLabel("Purchase Amount: "+0);
		JButton confirm = new JButton("Confirm purchase");
		confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				confirmSelection();
			}
		});
		bottom.add(available);
		bottom.add(purchased);
		bottom.add(confirm);
		
		
		contentPane.add(center, BorderLayout.CENTER);
		contentPane.add(bottom, BorderLayout.SOUTH);
		pack();
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public int getInitAvailable(){
		return this.init_available;
	}
	
	public int getAvailableCoin(){
		return this.curr_available;
	}
	
	public void setAvailableCoin(int i){
		this.curr_available = i;
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public int[] genUnitCount(ArrayList<Unit> a){
		int[] count = new int[5];
		for (Unit u : a){
			count[u.getType()]++;
		}
		return count;
	}
	
	public BufferedImage genUnitImage(int type){
		return this.unit_img.getSubimage(type*128, 128*this.player.getID(), 128, 128);
	}
	
	public void updateCoinLabels(){
		this.available.setText("Available Coins: "+this.curr_available);
		this.purchased.setText("Purchase Amount: "+(this.init_available - this.curr_available));
	}
	
	public int[] selectUnitCount(){
		int[] count = new int[5];
		for (UnitPurchaseGUI p : this.panels){
			count[p.getUnitType()] = p.getNumSelected();
		}
		return count;
	}
	
	public void confirmSelection(){
		int[] selected = selectUnitCount();
		this.gui.getGuiAction().selectGuiOption(selected);
		this.gui.getGuiAction().getNextGuiAction();
		dispose();
	}
}
