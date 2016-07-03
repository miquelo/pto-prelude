package net.preparatusopos.faces.event;

import java.net.URI;

import javax.activation.MimeType;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesListener;

public class UploadFileBehaviorEvent
extends BehaviorEvent
{
	private static final long serialVersionUID = 1L;
	
	public static final String EVENT_NAME = "uploadFile";
	
	private int index;
	private MimeType contentType;
	private long completedSize;
	private long totalSize;
	
	public UploadFileBehaviorEvent(UIComponent component, Behavior behavior, int
			index, URI ref, MimeType contentType, long completedSize, long
			totalSize)
	{
		super(component, behavior);
		this.index = index;
		this.contentType = contentType;
		this.completedSize = completedSize;
		this.totalSize = totalSize;
	}

	public int getIndex()
	{
		return index;
	}

	public MimeType getContentType()
	{
		return contentType;
	}

	public long getCompletedSize()
	{
		return completedSize;
	}
	
	public long getTotalSize()
	{
		return totalSize;
	}

	@Override
	public boolean isAppropriateListener(FacesListener listener)
	{
		return listener instanceof UploadFileBehaviorListener;
	}
	
	@Override
	public void processListener(FacesListener listener)
	{
		((UploadFileBehaviorListener) listener).processFileUpload(this);
	}
	
	@Override
	public String toString()
	{
		return String.format("UploadFileEvent[%d]", index);
	}
}
