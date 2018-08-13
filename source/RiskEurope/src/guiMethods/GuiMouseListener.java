package guiMethods;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import gameMethods.CityTerritory;
import gameMethods.GameInfo;
import gameMethods.Territory;

public class GuiMouseListener extends MouseAdapter{
	private GameGUI gui;
	private Point drag_point;
	
	/*********************
	 * Constructor       *
	 *********************/
	public GuiMouseListener(GameGUI gui){
		this.gui = gui;
		this.drag_point = null;
	}
	
	/*********************
	 * Mouse Methods     *
	 *********************/	
	@Override
	public void mousePressed(MouseEvent e){
		Component c = SwingUtilities.getDeepestComponentAt(e.getComponent(), e.getX(), e.getY());
		if (c.equals(this.gui.getMap())){
			if (SwingUtilities.isLeftMouseButton(e)){
				Point p = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this.gui.getMap()));
				dispatchPressToMap(p);
			}
			else{
				this.drag_point = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this.gui.getMap()));
			}
		}
		else if (c.equals(this.gui.getSide().getCardPanel())){
			Point p = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), c));
			dispatchPressToCardPanel(p);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		Component c = SwingUtilities.getDeepestComponentAt(e.getComponent(), e.getX(), e.getY());
		if (c.equals(this.gui.getMap())){
			Point p = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this.gui.getMap()));

			dispatchMousePositionToMap(p);
		}
		else if (c instanceof CurrentCardPanel){
			Point p = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), c));
			
			dispatchMousePositionToCardPanel(p);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		if (SwingUtilities.isRightMouseButton(e) && this.drag_point != null){
			Point ep = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this.gui.getMap()));
			double dx = ep.getX() - this.drag_point.getX();
			double dy = ep.getY() - this.drag_point.getY();
			this.drag_point = ep;
			this.gui.getMap().dragMap((int) dx, (int) dy);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		if (this.drag_point != null){
			this.drag_point = null;
		}
	}
	
	/*********************
	 * Map handlers      *
	 *********************/	
	public void dispatchPressToMap(Point p){
		if (getGameState() == GameInfo.PLACEMENT){
			GuiTerritory g = this.gui.getMap().getTerritoryFromPoint(p);
			this.gui.getMap().setSelectedTerritory(g);
			this.gui.revalidate();
			this.gui.repaint();
		}
		else if (getSelectionState() == GameInfo.SELECT_TERRITORY){
			GuiTerritory g = this.gui.getMap().getTerritoryFromPoint(p);
			this.gui.getMap().setSelectedTerritory(g);
			if (this.gui.getRisk().validateSelection(g.getTerritory())){
				this.gui.getGuiAction().selectGuiOption(g);
				this.gui.getGuiAction().getNextGuiAction();
			}
			this.gui.revalidate();
			this.gui.repaint();
		}
		else if (getSelectionState() == GameInfo.SELECT_TAX){
			GuiTerritory g = this.gui.getMap().getTerritoryFromPoint(p);
			if (g == null){return; }
			if (this.gui.getRisk().validateSelection(g.getTerritory())){
				this.gui.getMap().setSelectedTerritory(g);
				
				ArrayList<Territory> a = ((CityTerritory) g.getTerritory()).genTaxTerritories();
				ArrayList<GuiTerritory> ag = new ArrayList<GuiTerritory>();
				for (Territory t : a){
					ag.add(this.gui.getHash().getGuiFromTerritory(t));
				}
				this.gui.getMap().setSelectedTaxTerritories(ag);
				this.gui.getGuiAction().selectGuiOption(g);
				this.gui.getGuiAction().getNextGuiAction();
			}
			this.gui.revalidate();
			this.gui.repaint();
		}
	}
	
	public void dispatchMousePositionToMap(Point p){
		GuiTerritory g = this.gui.getMap().getTerritoryFromPoint(p);
		if (g != null && !g.equals(this.gui.getMap().getHighlightedTerritory())){
			this.gui.getMap().setHighlightedTerritory(g);
			this.gui.getMap().repaint();
		}
		else if (g == null && this.gui.getMap().getHighlightedTerritory() != null){
			this.gui.getMap().setHighlightedTerritory(null);
			this.gui.getMap().repaint();
		}
	}
	
	/*********************
	 * Card handlers     *
	 *********************/	
	public void dispatchMousePositionToCardPanel(Point p){
		CurrentCardPanel panel = this.gui.getSide().getCardPanel();
		int[] location = getLocationByPoint(p);
		int card = location[0];
		int action = location[1];
		
		if (card == panel.getCardHighlighted() && action == panel.getActionHighlighted()){ return;}
		else{
			panel.setCardHighlighted(card);
			panel.setActionHighlighted(action);
			this.gui.repaint();
		}
		
	}
	
	public void dispatchPressToCardPanel(Point p){
		if (this.gui.getGameInfo().getSelectionState() != GameInfo.SELECT_ACTION){ return;}
		
		CurrentCardPanel panel = this.gui.getSide().getCardPanel();
		int[] location = getLocationByPoint(p);
		int card = location[0];
		int action = location[1];
		
		if (card == panel.getCardSelected() && action == panel.getActionSelected()){ return;}
		else{
			panel.setCardAndActionSelected(card,action);
			this.gui.repaint();
		}
	}
	
	public int[] getLocationByPoint(Point p){
		CurrentCardPanel panel = this.gui.getSide().getCardPanel();
		Rectangle rect;
		int card;
		int action;
		
		if (panel.getFirstRectangle().contains(p)){
			rect = panel.getFirstRectangle();
			card = 1;
		}
		else if (panel.getSecondRectangle().contains(p)){
			rect = panel.getSecondRectangle();
			card = 2;
		}
		else{
			rect = null;
			card = 0;
		}

		if (this.gui.getGameInfo().getSelectionState() == GameInfo.SELECT_ACTION && rect != null){
			int x = (int)(p.getX()-rect.getMinX());
			int y = (int)(p.getY()-rect.getMinY());
			
			if (x < 10 || x > 140){
				action = 0;
			}
			else if (y < 10 || y > 190){
				action = 0;
			}
			else if (y < 70){
				action = 1;
			}
			else if (y < 130){
				action = 2;
			}
			else if (y < 190){
				action = 3;
			}
			else{
				action = 0;
			}
		}
		else{
			action = 0;
		}
		
		return new int[]{card,action};
	}
	
	/*********************
	 * Game State        *
	 *********************/	
	public int getGameState(){
		return this.gui.getGameInfo().getGameState();
	}
	
	public int getSelectionState(){
		return this.gui.getGameInfo().getSelectionState();
	}
}
