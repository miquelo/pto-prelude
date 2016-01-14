package net.preparatusopos.app.ma.web;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

public class PanelVisibility
{
	private boolean visible;
	private boolean initial;
	
	public PanelVisibility(boolean visible)
	{
		this.visible = visible;
		initial = true;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public boolean isInitial()
	{
		return initial;
	}

	public void toggle(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		visible = !visible;
		initial = false;
	}
}