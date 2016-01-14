package net.preparatusopos.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;

@FacesComponent(
	"net.preparatusopos.faces.ToolkitPanelFieldGroup"
)
public class ToolkitPanelFieldGroup
extends UIPanel
{
	protected enum PropertyKeys
	{
		styleClass
	}
	
	public ToolkitPanelFieldGroup()
	{
	}
	
	public String getStyleClass()
	{
		return (String) getStateHelper().eval(PropertyKeys.styleClass);
	}

	public void setStyleClass(String styleClass)
	{
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}
}
