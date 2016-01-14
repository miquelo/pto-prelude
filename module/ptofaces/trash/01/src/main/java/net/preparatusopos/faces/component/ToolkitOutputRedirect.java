package net.preparatusopos.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

@FacesComponent(
	"net.preparatusopos.faces.ToolkitOutputRedirect"
)
public class ToolkitOutputRedirect
extends UIOutput
{
	protected enum PropertyKeys
	{
		path
	}
	
	public ToolkitOutputRedirect()
	{
	}

	public String getPath()
	{
		return (String) getStateHelper().eval(PropertyKeys.path);
	}

	public void setPath(String path)
	{
		getStateHelper().put(PropertyKeys.path, path);
	}
}
