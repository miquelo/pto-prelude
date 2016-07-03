package net.preparatusopos.faces.component.behavior;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;

@FacesBehavior(
	"net.preparatusopos.faces.RemoveClassBehavior"
)
@ResourceDependencies({
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="script/util.js",
		target="head"
	)
})
public class RemoveClassBehavior
extends ClientBehaviorBase
{
	private String styleClass;
	private String target;
	
	public RemoveClassBehavior()
	{
		styleClass = null;
		target = null;
	}

	public String getStyleClass()
	{
		return styleClass;
	}

	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}
	
	public String getTarget()
	{
		return target;
	}

	public void setTarget(String target)
	{
		this.target = target;
	}

	@Override
	public String getScript(ClientBehaviorContext context)
	{
		StringBuilder js = new StringBuilder();
		js.append("net.preparatusopos.faces.util.removeClass(this,event,'");
		js.append(getStyleClass()).append("'");
		if (target != null)
			js.append(",'").append(target).append("'");
		js.append(")");
		return js.toString();
	}
}
