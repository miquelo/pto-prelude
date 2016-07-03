package net.preparatusopos.faces.renderkit;

import javax.faces.render.FacesRenderer;

@FacesRenderer(
	componentFamily="javax.faces.Output",
	rendererType="net.preparatusopos.faces.ResourceIcon"
)
public class ResourceIconRenderer
extends ResourceRenderer
{
	public ResourceIconRenderer()
	{
	}

	@Override
	protected String getRel()
	{
		return "shortcut icon";
	}
}