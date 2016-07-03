package net.preparatusopos.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;

@FacesComponent(
	"net.preparatusopos.faces.ToolkitPanelFormat"
)
public class ToolkitPanelFormat
extends UIPanel
{
	protected enum PropertyKeys
	{
		layout,
		text,
		styleClass
	}
	
	public ToolkitPanelFormat()
	{
	}
	
	public String getLayout()
	{
		return (String) getStateHelper().eval(PropertyKeys.layout);
	}

	public void setLayout(String layout)
	{
		getStateHelper().put(PropertyKeys.layout, layout);
	}
	
	public String getText()
	{
		return (String) getStateHelper().eval(PropertyKeys.text);
	}

	public void setText(String text)
	{
		getStateHelper().put(PropertyKeys.text, text);
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
