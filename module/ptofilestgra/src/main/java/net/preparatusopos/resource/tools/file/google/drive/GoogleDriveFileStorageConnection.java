package net.preparatusopos.resource.tools.file.google.drive;

import java.net.URI;

import net.preparatusopos.tools.file.FileStorageConnection;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;
import net.preparatusopos.tools.file.StoredFileSpecs;
import net.preparatusopos.util.resource.AbstractConnection;

public class GoogleDriveFileStorageConnection
extends AbstractConnection<GoogleDriveFileStorageManagedConnection>
implements FileStorageConnection
{
	public GoogleDriveFileStorageConnection()
	{
	}
	
	@Override
	public String getProviderName()
	{
		return mc.getProviderName();
	}
	
	@Override
	public URI createPublicRef(StoredFile storedFile)
	throws FileStorageException
	{
		return mc.createPublicRef(storedFile);
	}

	@Override
	public StoredFile store(StoredFileSpecs specs)
	throws FileStorageException
	{
		return mc.store(specs);
	}

	@Override
	public boolean remove(StoredFile storedFile)
	throws FileStorageException
	{
		return mc.remove(storedFile);
	}
}