package net.preparatusopos.tools.file;

import java.io.IOException;
import java.net.URI;

public interface FileStorageConnection
extends AutoCloseable
{
	public String getProviderName();
	
	public URI createPublicRef(StoredFile storedFile)
	throws FileStorageException;
	
	public StoredFile store(StoredFileSpecs specs)
	throws FileStorageException;
	
	public boolean remove(StoredFile storedFile)
	throws FileStorageException;
	
	@Override
	public void close()
	throws IOException;
}
