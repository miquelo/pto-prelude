package net.preparatusopos.faces.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.behavior.ClientBehavior;

public class ClientBehaviorsMap
{
	private Map<String, List<ClientBehavior>> clientBehaviors;
	
	public ClientBehaviorsMap()
	{
		clientBehaviors = new HashMap<>();
	}
	
	public Map<String, List<ClientBehavior>> getClientBehaviors()
	{
		return clientBehaviors;
	}
	
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
