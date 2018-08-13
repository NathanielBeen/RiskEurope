package guiMethods;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class HandMouseListener extends MouseAdapter{
	private HandGUI hand;
	
	public HandMouseListener(HandGUI hand){
		this.hand = hand;
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		Component c = SwingUtilities.getDeepestComponentAt(e.getComponent(), e.getX(), e.getY());
		if (c instanceof CardGUI){
			this.hand.setSelection((CardGUI) c); 
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		Component c = SwingUtilities.getDeepestComponentAt(e.getComponent(), e.getX(), e.getY());
		if (c instanceof CardGUI && !(c.equals(this.hand.getHighlight()))){
			this.hand.setHighlight((CardGUI) c);
		}
	}
}
