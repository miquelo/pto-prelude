package net.preparatusopos.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

@FacesComponent(
	"net.preparatusopos.faces.ToolkitOutputResourceLink"
)
public class ToolkitOutputResourceLink
extends UIOutput
implements FacesResource
{
	protected enum PropertyKeys
	{
		library,
		name
	}
	
	public ToolkitOutputResourceLink()
	{
	}

	@Override
	public String getLibrary()
	{
		return (String) getStateHelper().eval(PropertyKeys.library);
	}

	public void setLibrary(String library)
	{
		getStateHelper().put(PropertyKeys.library, library);
	}
	
	@Override
	public String getName()
	{
		return (String) getStateHelper().eval(PropertyKeys.name);
	}

	public void setName(String name)
	{
		getStateHelper().put(PropertyKeys.name, name);
	}
}
