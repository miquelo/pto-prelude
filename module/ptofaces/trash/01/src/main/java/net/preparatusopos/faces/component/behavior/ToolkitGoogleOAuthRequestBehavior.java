package net.preparatusopos.faces.component.behavior;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;

import net.preparatusopos.faces.util.Util;

@FacesBehavior(
	"net.preparatusopos.faces.ToolkitGoogleOAuthRequestBehavior"
)
@ResourceDependencies({
	@ResourceDependency(
		library="javax.faces",
		name="jsf.js",
		target="head"
	),
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="google-oauth.js",
		target="head"
	)
})
public class ToolkitGoogleOAuthRequestBehavior
extends ToolkitOAuthRequestBehavior
{
	public ToolkitGoogleOAuthRequestBehavior()
	{
	}
	
	@Override
	public String getScript(ClientBehaviorContext context)
	{
		StringBuilder js = new StringBuilder();
		js.append("if (!");
		js.append("net.preparatusopos.faces.googleOAuth.request(");
		js.append("this,event,'");
		js.append(Util.getClientId(context, getInput()));
		js.append("')");
		js.append("){return false;}");
		return js.toString();
	}
}
