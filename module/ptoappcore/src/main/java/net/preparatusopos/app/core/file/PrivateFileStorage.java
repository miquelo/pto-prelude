package net.preparatusopos.app.core.file;

import net.preparatusopos.app.domain.FileStorage;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFileSpecs;

public interface PrivateFileStorage
extends FileStorage
{
	public String getProviderName(PrivateFile privateFile)
	throws FileStorageException;
	
	public PrivateFile store(StoredFileSpecs specs)
	throws FileStorageException;
}
