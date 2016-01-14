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
	"net.preparatusopos.faces.ToolkitToggleClass"
)
public class ToolkitToggleClass
extends UIOutput
implements ClientBehaviorHolder
{
	public static final long DEFAULT_DELAY = 20;
	
	protected enum PropertyKeys
	{
		select,
		styleClass,
		delay
	}
	
	public static final String EVENT_BEFORE_TOGGLE = "beforeToggle";
	public static final String EVENT_AFTER_TOGGLE = "afterToggle";
	public static final String EVENT_TRANSITION_END = "transitionEnd";
	
	private Map<String, List<ClientBehavior>> clientBehaviors;
	
	public ToolkitToggleClass()
	{
		clientBehaviors = new HashMap<>();
		setDelay(DEFAULT_DELAY);
	}

	public String getSelect()
	{
		return (String) getStateHelper().eval(PropertyKeys.select);
	}

	public void setSelect(String select)
	{
		getStateHelper().put(PropertyKeys.select, select);
	}
	
	public String getStyleClass()
	{
		return (String) getStateHelper().eval(PropertyKeys.styleClass);
	}
	
	public void setStyleClass(String styleClass)
	{
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}
	
	public long getDelay()
	{
		return (Long) getStateHelper().eval(PropertyKeys.delay);
	}
	
	public void setDelay(long delay)
	{
		getStateHelper().put(PropertyKeys.delay, delay);
	}
	
	@Override
	public Collection<String> getEventNames()
	{
		return Arrays.asList(EVENT_BEFORE_TOGGLE, EVENT_AFTER_TOGGLE,
				EVENT_TRANSITION_END);
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
