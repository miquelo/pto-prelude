package net.preparatusopos.faces.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;

@FacesComponent(
	"net.preparatusopos.faces.ToolkitOAuthInit"
)
public class ToolkitOAuthInit
extends UIOutput
implements ClientBehaviorHolder
{
	protected enum PropertyKeys
	{
		authClientId,
		scopes
	}
	
	public static final String EVENT_SUCCESS = "success";
	
	private Map<String, List<ClientBehavior>> clientBehaviors;
	
	public ToolkitOAuthInit()
	{
		clientBehaviors = new HashMap<>();
	}

	public String getAuthClientId()
	{
		return (String) getStateHelper().eval(PropertyKeys.authClientId);
	}

	public void setAuthClientId(String authClientId)
	{
		getStateHelper().put(PropertyKeys.authClientId, authClientId);
	}
	
	public String getScopes()
	{
		return (String) getStateHelper().eval(PropertyKeys.scopes);
	}
	
	public void setScopes(String scopes)
	{
		getStateHelper().put(PropertyKeys.scopes, scopes);
	}
	
	@Override
	public Collection<String> getEventNames()
	{
		return Arrays.asList(EVENT_SUCCESS);
	}
	
	@Override
	public String getDefaultEventName()
	{
		return EVENT_SUCCESS;
	}
	
	@Override
	public Map<String, List<ClientBehavior>> getClientBehaviors()
	{
		return clientBehaviors;
	}
	
	@Override
	public void addClientBehavior(String eventName, ClientBehavior behavior)
	{
		List<ClientBehavior> behaviors = clientBehaviors.get(eventName);
		if (behaviors == null)
		{
			behaviors = new ArrayList<>();
			clientBehaviors.put(eventName, behaviors);
		}
		behaviors.add(behavior);
	}
}
