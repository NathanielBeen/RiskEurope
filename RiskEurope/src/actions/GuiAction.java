package actions;

public interface GuiAction {
	public void getNextGuiAction();
	public void selectGuiOption(Object o);
	public void resetGuiAction();
	public Action getAction();
}
