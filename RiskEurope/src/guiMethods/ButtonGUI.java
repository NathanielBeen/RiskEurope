package guiMethods;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import actions.GuiPlacement;
import gameMethods.GameInfo;
import gameMethods.Player;

public class ButtonGUI extends JPanel{

	private static final long serialVersionUID = 1L;
	private GameGUI gui;
	private BufferedImage back;
	
	private JButton selection;
	private JButton reset;
	private JButton action;
	private JButton skip;
	
	private String select_text;
	
	/***********************
	 * Constructor         *
	 ***********************/
	public ButtonGUI(GameGUI gui){
		this.gui = gui;
		this.back = this.gui.getGameInfo().getBlackBack();
		
	 	setLayout(new GridLayout(2,2,5,5));
		setBorder(new EmptyBorder(5,5,5,5));
		this.select_text = "Make Selection";
		
		this.selection = new JButton(select_text);
		this.selection.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				selectButton();
			}
		});
		add(this.selection);
		
		this.reset = new JButton("Reset action");
		this.reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				resetButton();
			}
		});
		add(this.reset);
		
		this.action = new JButton("Confirm Action");
		this.action.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				confirmButton();
			}
		});
		add(this.action);
		
		this.skip = new JButton("Skip action");
		this.skip.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				skipButton();
			}
		});
		add(this.skip);
	}
	
	/***********************
	 * Overridden Methods  *
	 ***********************/
	@Override
	public void paintComponent(Graphics g){
		BufferedImage b = this.back.getSubimage(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(b, 0, 0, null);
	}
	
	/***********************
	 * Button Methods      *
	 ***********************/
	public void selectButton(){
		if (this.gui.getGameInfo().getSelectionState() == GameInfo.SELECT_ACTION){
			CurrentCardPanel panel = this.gui.getSide().getCardPanel();
			int action = panel.getActionSelected();
			if ( action == 0){return; }
			
			this.gui.selectGuiActionFromCard(action-1);
			this.gui.getMap().updateMap();
		}
		else if (this.gui.getGameInfo().getSelectionState() == GameInfo.SELECT_TERRITORY || 
				 this.gui.getGameInfo().getSelectionState() == GameInfo.SELECT_TAX){
			GuiTerritory t = this.gui.getMap().getSelectedTerritory();
			if (t == null){
				return;
			}
			if (this.gui.getRisk().validateSelection(t.getTerritory())){
				this.gui.getGuiAction().selectGuiOption(t);
				this.gui.getGuiAction().getNextGuiAction();
				this.gui.getMap().updateMap();
			}
		}
		
		this.gui.repaint();
	}
	
	public void resetButton(){
		this.gui.getGuiAction().resetGuiAction();
		this.gui.getMap().updateMap();
	}
	
	public void confirmButton(){
		if (this.gui.getGameInfo().getSelectionState() == GameInfo.CONFIRM_ACTION){
			this.gui.resolveAction();
		}
		this.gui.getMap().updateMap();
		this.gui.repaint();
	}
	
	public void skipButton(){
		if (this.gui.getGameInfo().getGameState() == GameInfo.PLACEMENT){
			return;
		}
		
		this.gui.getMap().setTerritoryOptions(new ArrayList<GuiTerritory>());
		this.gui.getRisk().getNextAction();
		this.gui.setGuiAction(null);
		this.gui.getMap().resetMap();
		this.gui.getMap().updateMap();
		this.gui.changeGuiState("");
	}
}
