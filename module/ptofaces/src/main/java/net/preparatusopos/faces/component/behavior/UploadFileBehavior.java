package net.preparatusopos.faces.component.behavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.FacesBehavior;
import javax.json.stream.JsonGenerator;

import net.preparatusopos.faces.event.UploadFileBehaviorListener;
import net.preparatusopos.faces.model.FileUploadList;
import net.preparatusopos.faces.util.Util;

@FacesBehavior(
	"net.preparatusopos.faces.UploadFileBehavior"
)
@ResourceDependencies({
	@ResourceDependency(
		library="javax.faces",
		name="jsf.js",
		target="head"
	),
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="script/upload-file.js",
		target="head"
	),
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="script/upload-file-google-drive.js",
		target="head"
	)
})
public class UploadFileBehavior
extends ClientBehaviorBase
{
	private String input;
	private FileUploadList files;
	private Collection<String> render;
	
	public UploadFileBehavior()
	{
		input = null;
		files = null;
		render = new ArrayList<>();
	}
	
	@Override
	public Set<ClientBehaviorHint> getHints()
	{
		return Collections.emptySet();
	}

	public String getInput()
	{
		return input;
	}

	public void setInput(String input)
	{
		this.input = input;
	}
	
	public FileUploadList getFiles()
	{
		return files;
	}

	public void setFiles(FileUploadList files)
	{
		this.files = files;
	}

	public Collection<String> getRender()
	{
		return render;
	}

	public void setRender(Collection<String> render)
	{
		this.render = render;
	}

	public void addUploadFileBehaviorListener(UploadFileBehaviorListener
			listener)
	{
		super.addBehaviorListener(listener);
	}
	
	public void removeUploadFileBehaviorListener(UploadFileBehaviorListener
			listener)
	{
		super.removeBehaviorListener(listener);
	}

	@Override
	public String getScript(ClientBehaviorContext context)
	{
		StringBuilder js = new StringBuilder();
		js.append("net.preparatusopos.faces.uploadFile.upload(this,event");
		Map<String, Object> options = getOptions(context);
		if (!options.isEmpty())
		{
			js.append(",");
			Util.writeInlineJS(js, options);
		}
		js.append(")");
		return js.toString();
	}
	
	public void generate(JsonGenerator generator)
	throws IOException
	{
		getFiles().generate(generator);
	}
	
	private Map<String, Object> getOptions(ClientBehaviorContext context)
	{
		Map<String, Object> params = new HashMap<>();
		if (getInput() != null)
			params.put("input", getInput());
		if (getRender() != null)
			params.put("render", toCollectionValue(getRender()));
		return params;
	}
	
	private static String toCollectionValue(Collection<?> collection)
	{
		StringBuilder str = new StringBuilder();
		String sep = "";
		for (Object element : collection)
		{
			str.append(sep).append(element);
			sep = " ";
		}
		return str.toString();
	}
}
