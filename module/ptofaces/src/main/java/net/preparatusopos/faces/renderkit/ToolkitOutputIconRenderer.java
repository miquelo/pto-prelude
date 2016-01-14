package net.preparatusopos.faces.renderkit;

import javax.faces.render.FacesRenderer;

@FacesRenderer(
	componentFamily="javax.faces.Output",
	rendererType="net.preparatusopos.faces.ToolkitOutputIcon"
)
public class ToolkitOutputIconRenderer
extends ToolkitOutputResourceRenderer
{
	public ToolkitOutputIconRenderer()
	{
	}

	@Override
	protected String getRel()
	{
		return "shortcut icon";
	}
}