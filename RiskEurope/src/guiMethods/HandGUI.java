package guiMethods;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import gameMethods.Card;
import gameMethods.Player;

public class HandGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private ImagePanel contentPane;
	private ImagePanel center;
	
	private GameGUI gui;
	private ArrayList<Card> hand;
	private Player player;
	
	private BufferedImage back;
	
	private BufferedImage highlight_img;
	private BufferedImage select_img;
	
	private CardGUI highlight;
	private CardGUI selected;
	private CardGUI selected_2;

	/*********************
	 * Constructor       *
	 *********************/
	public HandGUI(GameGUI gui, ArrayList<Card> hand) {
		this.gui = gui;
		this.hand = hand;
		this.player = this.gui.getRisk().getCurrentPlayer();
		
		this.back = this.gui.getGameInfo().getBackByPlayerID(this.player.getID());
		
		this.selected = null;
		this.selected_2 = null;
		
		try{
			this.highlight_img = ImageIO.read(new File("src/resources/card_highlight.png"));
			this.select_img = ImageIO.read(new File("src/resources/card_select.png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new ImagePanel(this.back);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(5, 5));
		setContentPane(contentPane);
		
		JLabel player = new JLabel(this.player.getName(), SwingConstants.CENTER);
		player.setFont(new Font(player.getFont().getName(), Font.BOLD, 18));
		player.setForeground(Color.BLACK);

		contentPane.add(player, BorderLayout.NORTH);
		
		genFrameFromHand();
		contentPane.add(this.center, BorderLayout.CENTER);
		
		JButton confirm = new JButton("select two cards");
		confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				confirmAction();
			}
		});
		contentPane.add(confirm, BorderLayout.SOUTH);
		
		HandMouseListener l = new HandMouseListener(this);
		addMouseListener(l);
		addMouseMotionListener(l);
		
		pack();
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public GameGUI getGui(){
		return this.gui;
	}
	
	public CardGUI getHighlight(){
		return this.highlight;
	}
	
	public void setHighlight(CardGUI c){
		if (this.highlight != null){
			this.highlight.setIsHighlighted(false);
		}
		this.highlight = c;
		c.setIsHighlighted(true);
		repaint();
	}
	
	public void setSelection(CardGUI c){
		if (this.selected == null){
			c.setIsFirstSelected(true);
			this.selected = c;
		}
		else if (this.selected_2 == null){
			c.setIsSecondSelected(true);
			this.selected_2 = c;
		}
		else{
			this.selected.setIsFirstSelected(false);
			this.selected = null;
			
			this.selected_2.setIsSecondSelected(false);
			this.selected_2.setIsFirstSelected(true);
			this.selected = this.selected_2;
			
			c.setIsSecondSelected(true);
			this.selected_2 = c;
		}
		repaint();
	}
	
	public BufferedImage getHighlightImage(){
		return this.highlight_img;
	}
	
	public BufferedImage getSelectImage(){
		return this.select_img;
	}
	
	public CardGUI getFirstSelection(){
		return this.selected;
	}
	
	public CardGUI getSecondSelection(){
		return this.selected_2;
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public void genFrameFromHand(){
		ImagePanel center = new ImagePanel(this.gui.getGameInfo().getBlackBack());
		center.setLayout(new GridLayout(2,4,5,5));
		center.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		for (Card card : this.hand){
			CardGUI cg = new CardGUI(card, this.gui);
			center.add(cg);
		}
		
		this.center = center;
	}
	
	public void confirmAction(){
		if (this.selected == null || this.selected_2 == null || this.selected.equals(this.selected_2)){ return;}
		else{
			this.player.playCards(this.selected.getCard(), this.selected_2.getCard());
			this.gui.getRisk().getNextAction();
			this.gui.changeGuiState("");
			dispose();
		}
	}

}
