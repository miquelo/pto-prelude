package net.preparatusopos.faces.component.behavior;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;

@FacesBehavior(
	"net.preparatusopos.faces.ToolkitUpdateMaxHeightBehavior"
)
@ResourceDependencies({
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="update-max-height.js",
		target="head"
	)
})
public class ToolkitUpdateMaxHeightBehavior
extends ClientBehaviorBase
{
	private String value;
	
	public ToolkitUpdateMaxHeightBehavior()
	{
		value = null;
	}
	
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public String getScript(ClientBehaviorContext context)
	{
		StringBuilder js = new StringBuilder();
		js.append("if (!");
		js.append("net.preparatusopos.faces.updateMaxHeight.perform(");
		js.append("element,event,");
		if (value == null)
			js.append("null");
		else
			js.append("'").append(value).append("'");
		js.append(")){return false;}");
		return js.toString();
	}
}
