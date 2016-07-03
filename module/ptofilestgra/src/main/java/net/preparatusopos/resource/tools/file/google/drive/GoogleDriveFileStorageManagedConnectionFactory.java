package net.preparatusopos.resource.tools.file.google.drive;

import javax.resource.ResourceException;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.security.auth.Subject;

import net.preparatusopos.tools.file.FileStorageConnection;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.util.resource.AbstractConnectionFactory;
import net.preparatusopos.util.resource.AbstractManagedConnectionFactory;

/*
 * https://developers.google.com/identity/protocols/OAuth2ServiceAccount
 * https://developers.google.com/drive/v3/web/manage-uploads
 */

@ConnectionDefinition(
	connectionFactory=GoogleDriveFileStorageConnectionFactory.class,
	connectionFactoryImpl=GoogleDriveFileStorageLocalConnectionFactory.class,
	connection=FileStorageConnection.class,
	connectionImpl=GoogleDriveFileStorageConnection.class
)
public class GoogleDriveFileStorageManagedConnectionFactory
extends AbstractManagedConnectionFactory
{
	private static final long serialVersionUID = 1L;
	
	private String applicationName;
	private String folderName;
	private String credential;
	
	public GoogleDriveFileStorageManagedConnectionFactory()
	{
		applicationName = null;
		folderName = null;
		credential = null;
	}

	@ConfigProperty(
		type=String.class
	)
	public void setApplicationName(String applicationName)
	{
		this.applicationName = applicationName;
	}
	
	@ConfigProperty(
		type=String.class
	)
	public void setFolderName(String folderName)
	{
		this.folderName = folderName;
	}
	
	@ConfigProperty(
		type=String.class,
		confidential=true
	)
	public void setCredential(String credential)
	{
		this.credential = credential;
	}
	
	/*
	 * Implements: javax.resource.spi.ManagedConnectionFactory
	 */

	@Override
	public ManagedConnection createManagedConnection(Subject subject,
			ConnectionRequestInfo cxRequestInfo)
	throws ResourceException
	{
		try
		{
			GoogleDriveFileStorageManagedConnection mc =
					new GoogleDriveFileStorageManagedConnection();
			mc.setApplicationName(applicationName);
			mc.setFolderName(folderName);
			mc.setCredential(credential);
			mc.connect();
			return mc;
		}
		catch (FileStorageException exception)
		{
			throw new ResourceException(exception);
		}
	}

	/*
	 * Implements: net.preparatusopos.util.resource.AbstractConnectionFactory
	 */
	
	@Override
	protected AbstractConnectionFactory<?> createLocalConnectionFactory()
	{
		return new GoogleDriveFileStorageLocalConnectionFactory();
	}
}