package net.preparatusopos.resource.tools.file.google.drive;

import net.preparatusopos.util.resource.AbstractConnectionFactory;

public class GoogleDriveFileStorageLocalConnectionFactory
extends AbstractConnectionFactory<GoogleDriveFileStorageConnection>
implements GoogleDriveFileStorageConnectionFactory
{
	private static final long serialVersionUID = 1L;
	
	public GoogleDriveFileStorageLocalConnectionFactory()
	{
	}
	
	@Override
	protected void configure(GoogleDriveFileStorageConnection connection)
	{
	}
}