package net.preparatusopos.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;

@FacesComponent(
	"net.preparatusopos.faces.ToolkitPanelField"
)
public class ToolkitPanelField
extends UIPanel
{
	protected enum PropertyKeys
	{
		styleClass,
		errorClass
	}
	
	public ToolkitPanelField()
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
	
	public String getErrorClass()
	{
		return (String) getStateHelper().eval(PropertyKeys.errorClass);
	}

	public void setErrorClass(String errorClass)
	{
		getStateHelper().put(PropertyKeys.errorClass, errorClass);
	}
}
