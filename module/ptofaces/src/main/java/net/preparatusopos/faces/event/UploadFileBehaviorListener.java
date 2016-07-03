package net.preparatusopos.faces.event;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorListener;

public interface UploadFileBehaviorListener
extends BehaviorListener
{
	public void processFileUpload(UploadFileBehaviorEvent event)
	throws AbortProcessingException;
}
