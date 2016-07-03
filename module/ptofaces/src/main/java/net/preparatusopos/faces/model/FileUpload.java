package net.preparatusopos.faces.model;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.stream.JsonGenerator;
import javax.xml.bind.DatatypeConverter;

import net.preparatusopos.tools.file.StoredFile;

public class FileUpload
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private StoredFile file;
	
	@ConstructorProperties({
		"file"
	})
	public FileUpload(StoredFile file)
	{
		this.file = file;
	}

	public StoredFile getFile()
	{
		return file;
	}

	public void generate(JsonGenerator generator, int index)
	throws IOException
	{
		generator.writeStartObject();
		
		generator.write("index", index);
		generator.write("provider", file.getProviderName());
		generator.write("name", file.getName());
		
		Calendar creationDate = Calendar.getInstance();
		creationDate.setTime(file.getCreationDate());
		generator.write("creationDate",
				DatatypeConverter.printDateTime(creationDate));
		generator.writeStartObject("data");
		Map<String, String> providerData = file.getProviderData();
		for (Entry<String, String> entry : providerData.entrySet())
			generator.write(entry.getKey(), entry.getValue());
		generator.writeEnd();
		
		generator.writeEnd();
	}
}
