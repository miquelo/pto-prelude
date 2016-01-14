package net.preparatusopos.faces.component.behavior;

import java.util.HashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;

public abstract class ToolkitOAuthRequestBehavior
extends ClientBehaviorBase
{
	private String input;
	
	public ToolkitOAuthRequestBehavior()
	{
		input = null;
	}

	@Override
	public Set<ClientBehaviorHint> getHints()
	{
		Set<ClientBehaviorHint> hints = new HashSet<>();
		hints.add(ClientBehaviorHint.SUBMITTING);
		return hints;
	}
	
	public String getInput()
	{
		return input;
	}

	public void setInput(String input)
	{
		this.input = input;
	}

	@Override
	public void decode(FacesContext context, UIComponent component)
	{
		// Why?
	}
}
