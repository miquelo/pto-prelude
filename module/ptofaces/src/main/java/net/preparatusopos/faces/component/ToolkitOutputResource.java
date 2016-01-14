package net.preparatusopos.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

@FacesComponent(
	"net.preparatusopos.faces.ToolkitOutputResource"
)
public class ToolkitOutputResource
extends UIOutput
{
	protected enum PropertyKeys
	{
		library,
		name,
		type,
		media
	}
	
	public ToolkitOutputResource()
	{
	}
	
	public String getLibrary()
	{
		return (String) getStateHelper().eval(PropertyKeys.library);
	}

	public void setLibrary(String library)
	{
		getStateHelper().put(PropertyKeys.library, library);
	}
	
	public String getName()
	{
		return (String) getStateHelper().eval(PropertyKeys.name);
	}

	public void setName(String name)
	{
		getStateHelper().put(PropertyKeys.name, name);
	}
	
	public String getType()
	{
		return (String) getStateHelper().eval(PropertyKeys.type);
	}
	
	public void setType(String type)
	{
		getStateHelper().put(PropertyKeys.type, type);
	}
	
	public String getMedia()
	{
		return (String) getStateHelper().eval(PropertyKeys.media);
	}
	
	public void setMedia(String media)
	{
		getStateHelper().put(PropertyKeys.media, media);
	}
}
