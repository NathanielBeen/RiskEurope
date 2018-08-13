package guiMethods;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import gameMethods.Card;
import gameMethods.GameInfo;
import gameMethods.Player;

public class CurrentCardPanel extends JPanel {
	private SideGUI gui;
	
	private Card first_selection;
	private Card second_selection;
	
	private Rectangle first_location;
	private Rectangle second_location;
	
	private int card_highlighted;
	private int action_highlighted;
	private int card_selected;
	private int action_selected;
	
	BufferedImage card_highlight;
	BufferedImage card_select;
	BufferedImage action_highlight;
	BufferedImage action_select;
	BufferedImage action_option;
	BufferedImage empty_selection;

	/***********************
	 * Constructor         *
	 ***********************/
	public CurrentCardPanel(SideGUI gui) {
		this.gui = gui;
		
		this.first_selection = this.gui.getFirstCard();
		this.second_selection = this.gui.getSecondCard();
		
		this.card_highlight = this.gui.getGui().getGameInfo().getHighlightImage();
		this.card_select = this.gui.getGui().getGameInfo().getSelectImage();
		this.action_highlight = this.gui.getGui().getGameInfo().getActionHighlightImage();
		this.action_select = this.gui.getGui().getGameInfo().getActionSelectImage();
		this.action_option = this.gui.getGui().getGameInfo().getActionOptionImage();
		this.empty_selection = this.gui.getGui().getGameInfo().getEmptySelectionImage();
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void paintComponent(Graphics g){
		Player player = this.gui.getGui().getRisk().getCurrentPlayer();
		if (player == null){
			player = this.gui.getGui().getRisk().getPlayers().get(0);
		}
		BufferedImage back = this.gui.getGui().getGameInfo().getBackByPlayerID(
	             player.getID());
		BufferedImage corr_back = new BufferedImage(getWidth(), getHeight(), back.getType());
		
		Graphics2D g2 = corr_back.createGraphics();
		g2.drawImage(back.getSubimage(0, 0, getWidth(), getHeight()), 0, 0, null);
		
		//draw cards
		int x_cor = (int)(getWidth()-150)/2;
		if (x_cor <= 0){
			x_cor = 1;
		}
		int y_cor = (int)(getHeight()-400)/2;
		if (y_cor <= 0){
			y_cor = 1;
		}
		int y_cor_2 = y_cor+200;
		
		if (getHeight()-420 > 0){
			y_cor_2 = y_cor+210;
		}
		
		if (this.first_selection == null){
			g2.drawImage(this.gui.getGui().getGameInfo().getCardBackImage(), x_cor, y_cor, null);
			BufferedImage first_empty = this.empty_selection.getSubimage(0, 0, 150, 200);
			g2.setComposite(AlphaComposite.SrcOver);
			g2.drawImage(first_empty, x_cor, y_cor, null);
		}
		else{
			g2.drawImage(genCardImage(this.first_selection), x_cor, y_cor, null);
		}
		this.first_location = new Rectangle(x_cor,y_cor,150,200);
		
		if (this.second_selection == null){
			g2.drawImage(this.gui.getGui().getGameInfo().getCardBackImage(), x_cor, y_cor_2, null);
			BufferedImage second_empty = this.empty_selection.getSubimage(0, 200, 150, 200);
			g2.setComposite(AlphaComposite.SrcOver);
			g2.drawImage(second_empty, x_cor, y_cor_2, null);
		}
		else{
			g2.drawImage(genCardImage(this.second_selection), x_cor, y_cor_2, null);
		}
		this.second_location = new Rectangle(x_cor,y_cor_2,150,200);
		
		g2.setComposite(AlphaComposite.SrcOver);
		//draw options, if applicable
		if (this.gui.getGui().getGameInfo().getSelectionState() == GameInfo.SELECT_ACTION){
			if (this.first_selection != null && this.gui.getGui().getRisk().getCurrentCard().equals(this.first_selection)){
				if (this.first_selection.hasFreeAction()){
					g2.drawImage(this.action_option, x_cor+10, y_cor+10, null);
				}
				if (!this.first_selection.getActionUsed()){
					g2.drawImage(this.action_option, x_cor+10, y_cor+70, null);
					g2.drawImage(this.action_option, x_cor+10, y_cor+130, null);
				}
			}
			else if (this.second_selection != null && this.gui.getGui().getRisk().getCurrentCard().equals(this.second_selection)){
				if (this.second_selection.hasFreeAction()){
					g2.drawImage(this.action_option, x_cor+10, y_cor_2+10, null);
				}
				if (!this.second_selection.getActionUsed()){
					g2.drawImage(this.action_option, x_cor+10, y_cor_2+70, null);
					g2.drawImage(this.action_option, x_cor+10, y_cor_2+130, null);
				}
			}
		}
		
		//draw highlights,selections
		if (this.action_highlighted != 0){
			Point p = getActionPoint(this.card_highlighted, this.action_highlighted);
			g2.drawImage(this.action_highlight, (int)p.getX(), (int)p.getY(), null);
		}
		else if (this.card_highlighted != 0){
			Point p = getCardPoint(this.card_highlighted);
			g2.drawImage(this.card_highlight, (int)p.getX(), (int)p.getY(), null);
		}
		
		if (this.action_selected != 0){
			Point p = getActionPoint(this.card_selected, this.action_selected);
			g2.drawImage(this.action_select, (int)p.getX(), (int)p.getY(), null);
		}
		else if (this.card_selected != 0){
			Point p = getCardPoint(this.card_selected);
			g2.drawImage(this.card_select, (int)p.getX(), (int)p.getY(), null);
		}
		
		g.drawImage(corr_back,0,0,null);
	}
	
	/***********************
	 * Other Methods       *
	 ***********************/
	public BufferedImage genCardImage(Card c){
		BufferedImage img = new BufferedImage(150, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.drawImage(this.gui.getGui().getGameInfo().getCardBackImage(), 0, 0, null);
		
		int f = c.getFreeAction();
		int u = c.getUpperAction();
		int l = c.getLowerAction();
		g.setComposite(AlphaComposite.SrcOver);
		
		if (f != 0){
			BufferedImage free = this.gui.getGui().getGameInfo().getActionImage().getSubimage(0, (f-1)*60, 130, 60);
			g.drawImage(free, 10, 10, null);
		}
		BufferedImage upper = this.gui.getGui().getGameInfo().getActionImage().getSubimage(0, (u-1)*60, 130, 60);
		BufferedImage lower = this.gui.getGui().getGameInfo().getActionImage().getSubimage(0, (l-1)*60, 130, 60);
		g.drawImage(upper, 10, 70, null);
		g.drawImage(lower, 10, 130, null);
		
		return img;
	}
	
	public Point getActionPoint(int card, int action){
		int x = (int)first_location.getMinX()+10;
		int init_y = 0;
		
		switch(card){
			case 1:
				init_y = (int)first_location.getMinY(); 
				break;
			case 2:
				init_y = (int)second_location.getMinY();
				break;
			default:
				return null;
		}
		int y = 0;
		switch(action){
			case 1:
				y = init_y+10;
				break;
			case 2:
				y = init_y+70;
				break;
			case 3:
				y = init_y+130;
				break;
			default:
				return null;
		}
		return new Point(x,y);
	}
	
	public Point getCardPoint(int card){
		int x = (int)first_location.getMinX();
		int y = 0;
		switch(card){
		case 1:
			y = (int)first_location.getMinY();
			break;
		case 2:
			y = (int)second_location.getMinY();
			break;
		default:
			return null;
		}
		
		return new Point(x,y);
	}
	
	/***********************
	 * Get and Set Methods *
	 ***********************/
	public Card getFirstCard(){
		return this.first_selection;
	}
	
	public void setFirstCard(Card c){
		this.first_selection = c;
	}
	
	public Card getSecondCard(){
		return this.second_selection;
	}
	
	public void setSecondCard(Card c){
		this.second_selection = c;
	}
	
	public Rectangle getFirstRectangle(){
		return this.first_location;
	}
	
	public Rectangle getSecondRectangle(){
		return this.second_location;
	}
	
	public int getCardHighlighted(){
		return this.card_highlighted;
	}
	
	public void setCardHighlighted(int i){
		this.card_highlighted = i;
	}
	
	public int getActionHighlighted(){
		return this.action_highlighted;
	}
	
	public void setActionHighlighted(int i){
		this.action_highlighted = i;
	}
	
	public int getCardSelected(){
		return this.card_selected;
	}
	
	public void setCardAndActionSelected(int i, int j){
		if ((i == 1 && this.gui.getGui().getRisk().getCurrentCard().equals(this.first_selection))
		   || i == 2 && this.gui.getGui().getRisk().getCurrentCard().equals(this.second_selection)){
			   this.card_selected = i;
			   this.action_selected = j;
		}
	}
	
	public void clearSelection(){
		this.card_selected = 0;
		this.action_selected = 0;
	}
	
	public int getActionSelected(){
		return this.action_selected;
	}
}
