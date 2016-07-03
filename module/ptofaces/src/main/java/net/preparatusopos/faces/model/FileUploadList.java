package net.preparatusopos.faces.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.stream.JsonGenerator;

import net.preparatusopos.faces.event.UploadFileBehaviorEvent;
import net.preparatusopos.tools.file.StoredFile;

public class FileUploadList
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Map<Integer, FileUpload> map;
	
	public FileUploadList()
	{
		map = new HashMap<>();
	}
	
	public StoredFile getStoredFile(UploadFileBehaviorEvent event)
	{
		FileUpload upload = map.get(event.getIndex());
		return upload.getFile();
	}
	
	public boolean begin(UploadFileBehaviorEvent event)
	{
		return !map.containsKey(event.getIndex());
	}
	
	public boolean completed(UploadFileBehaviorEvent event)
	{
		return event.getTotalSize() == event.getCompletedSize();
	}
	
	public void push(UploadFileBehaviorEvent event, StoredFile file)
	{
		map.put(event.getIndex(), new FileUpload(file));
	}
	
	public void clear()
	{
		map.clear();
	}
	
	public void generate(JsonGenerator generator)
	throws IOException
	{
		generator.writeStartArray();
		for (Entry<Integer, FileUpload> entry : map.entrySet())
		{
			int index = entry.getKey();
			FileUpload upload = entry.getValue();
			upload.generate(generator, index);
		}
		generator.writeEnd();
	}
}
