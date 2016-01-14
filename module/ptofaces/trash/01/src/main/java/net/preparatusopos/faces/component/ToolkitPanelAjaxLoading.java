package net.preparatusopos.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;

@FacesComponent(
	"net.preparatusopos.faces.ToolkitPanelAjaxLoading"
)
public class ToolkitPanelAjaxLoading
extends UIPanel
{
	protected enum PropertyKeys
	{
		visibleClass,
		hiddenClass
	}
	
	public ToolkitPanelAjaxLoading()
	{
	}
	
	public String getVisibleClass()
	{
		return (String) getStateHelper().eval(PropertyKeys.visibleClass);
	}

	public void setVisibleClass(String visibleClass)
	{
		getStateHelper().put(PropertyKeys.visibleClass, visibleClass);
	}
	
	public String getHiddenClass()
	{
		return (String) getStateHelper().eval(PropertyKeys.hiddenClass);
	}

	public void setHiddenClass(String hiddenClass)
	{
		getStateHelper().put(PropertyKeys.hiddenClass, hiddenClass);
	}
}
