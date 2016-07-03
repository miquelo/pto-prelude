package net.preparatusopos.app.domain;

import java.net.URI;

import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;

public interface FileStorage
{
	public StoredFile find(String name)
	throws FileStorageException;
	
	public URI createPublicRef(StoredFile storedFile)
	throws FileStorageException;
	
	public void submit(StoredFile storedFile)
	throws FileStorageException;
	
	public void forget(StoredFile storedFile)
	throws FileStorageException;
	
	public boolean remove(StoredFile storedFile)
	throws FileStorageException;
}
