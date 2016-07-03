package net.preparatusopos.resource.tools.file.google.drive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.activation.MimeType;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.security.auth.Subject;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import javax.xml.bind.DatatypeConverter;

import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;
import net.preparatusopos.tools.file.StoredFileSpecs;
import net.preparatusopos.util.resource.AbstractManagedConnection;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

public class GoogleDriveFileStorageManagedConnection
extends AbstractManagedConnection<GoogleDriveFileStorageConnection>
implements XAResource
{
	public static final String PROVIDER_NAME = "googleDrive";
	
	private static final String EIS_PRODUCT_NAME = "GoogleDriveFileStorage";
	private static final String EIS_PRODUCT_VERSION = "0.1";
	private static final int MAX_CONNECTIONS = 20;
	
	private HttpTransport transport;
	private JacksonFactory jacksonFact;
	private String folderId;
	
	private String applicationName;
	private String folderName;
	private GoogleCredential credential;
	private GoogleCredential scopedCredential;
	
	public GoogleDriveFileStorageManagedConnection()
	{
		transport = null;
		jacksonFact = JacksonFactory.getDefaultInstance();
		folderId = null;
		
		applicationName = null;
		folderName = null;
		credential = null;
		scopedCredential = null;
	}
	
	public void setApplicationName(String applicationName)
	{
		this.applicationName = applicationName;
	}
	
	public void setFolderName(String folderName)
	{
		this.folderName = folderName;
	}

	public void setCredential(String credential)
	throws FileStorageException
	{
		try (InputStream input = new ByteArrayInputStream(
				DatatypeConverter.parseBase64Binary(credential)))
		{
			this.credential = GoogleCredential.fromStream(input);
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}

	public void connect()
	throws FileStorageException
	{
		try
		{
			transport = new NetHttpTransport();
			
			GoogleCredential scoped = getScopedCredential();
			Drive service = getService(scoped);
			
			for (File file : service.files().list().execute().getFiles())
			{
				if (folderName.equals(file.getName()))
					folderId = file.getId();
			}
			if (folderId == null)
			{
				File metadata = new File();
				metadata.setName(folderName);
				metadata.setMimeType("application/vnd.google-apps.folder");
				
				File folder = service.files().create(metadata)
					.setFields("id")
					.execute();
				folderId = folder.getId();
			}
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}
	
	public String getProviderName()
	{
		return PROVIDER_NAME;
	}
	
	public URI createPublicRef(StoredFile storedFile)
	throws FileStorageException
	{
		try
		{
			String name = storedFile.getName();
			return createRef(findFile(name, "webContentLink"));
		}
		catch (URISyntaxException exception)
		{
			throw new FileStorageException(exception);
		}
	}

	public StoredFile store(StoredFileSpecs specs)
	throws FileStorageException
	{
		try
		{
			GoogleCredential scoped = getScopedCredential();
			Drive service = getService(scoped);
			
			File metadata = new File();
			metadata.setParents(Arrays.asList(folderId));
			metadata.setName(specs.getName());
			MimeType contentType = specs.getContentType();
			if (contentType != null)
				metadata.setMimeType(contentType.toString());
			
			File newFile = service.files().create(metadata)
				.setFields("id")
				.execute();
			
			StoredFile storedFile = StoredFile.create(getProviderName(),
					specs.getName());
			Map<String, String> providerData = storedFile.getProviderData();
			providerData.put("id", newFile.getId());
			providerData.put("accessToken", scoped.getAccessToken());
			
			Permission permission = new Permission();
			permission.setType("anyone");
			permission.setRole("reader");
			service.permissions().create(newFile.getId(), permission)
				.execute();
			
			return storedFile;
			
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}
	
	public boolean remove(StoredFile storedFile)
	throws FileStorageException
	{
		try
		{
			String name = storedFile.getName();
			
			Drive service = getService(getScopedCredential());
			boolean deleted = false;
			
			for (File file : service.files().list().setFields(
					"file(name, parents)").execute().getFiles())
			{
				List<String> parents = file.getParents();
				if (name.equals(file.getName()) && parents.contains(folderId))
				{
					service.files().delete(file.getId());
					deleted = true;
				}
			}
			return deleted;
		}
		catch (IOException exception)
		{
			return false;
		}
	}
	
	/*
	 * Implements: javax.resource.spi.ManagedConnectionMetaData
	 */
	
	@Override
	public String getEISProductName()
	throws ResourceException
	{
		return EIS_PRODUCT_NAME;
	}

	@Override
	public String getEISProductVersion()
	throws ResourceException
	{
		return EIS_PRODUCT_VERSION;
	}

	@Override
	public int getMaxConnections()
	throws ResourceException
	{
		return MAX_CONNECTIONS;
	}
	
	/*
	 * Implements: javax.resource.spi.ManagedConnection
	 */
	
	@Override
	public LocalTransaction getLocalTransaction()
	throws ResourceException
	{
		throw new ResourceException("LocalTransaction is not supported");
	}
	
	@Override
	public XAResource getXAResource()
	throws ResourceException
	{
		return this;
	}
	
	@Override
	public void destroy()
	throws ResourceException
	{
	}
	
	/*
	 * Implements: net.preparatusopos.resource.security.external.
	 * AbstractManagedConnection
	 */
	
	@Override
	public int matchScore(Subject subject, ConnectionRequestInfo cxRequestInfo)
	{
		return -1;
	}

	/*
	 * Implements: javax.transaction.xa.XAResource
	 */
	
	@Override
	public int getTransactionTimeout()
	throws XAException
	{
		return 0;
	}
	
	@Override
	public boolean setTransactionTimeout(int seconds)
	throws XAException
	{
		return false;
	}
	
	@Override
	public void start(Xid xid, int flags)
	throws XAException
	{
	}
	
	@Override
	public void end(Xid xid, int flags)
	throws XAException
	{
	}
	
	@Override
	public int prepare(Xid xid)
	throws XAException
	{
		return XA_RDONLY;
	}
	
	@Override
	public void forget(Xid xid)
	throws XAException
	{
	}
	
	@Override
	public void commit(Xid xid, boolean onePhase)
	throws XAException
	{
	}
	
	@Override
	public void rollback(Xid xid)
	throws XAException
	{
	}

	@Override
	public Xid[] recover(int flag)
	throws XAException
	{
		return null;
	}
	
	@Override
	public boolean isSameRM(XAResource xares)
	throws XAException
	{
		return xares instanceof GoogleDriveFileStorageManagedConnection;
	}
	
	/*
	 * Private methods
	 */
	
	private Drive getService(GoogleCredential scoped)
	{
		return new Drive.Builder(transport, jacksonFact, scoped)
			.setApplicationName(applicationName)
			.build();
	}
	
	private GoogleCredential getScopedCredential()
	throws FileStorageException
	{
		try
		{
			if (scopedCredential == null)
			{
				scopedCredential = this.credential.createScoped(
						Arrays.asList(DriveScopes.DRIVE));
				scopedCredential.refreshToken();
			}
			return scopedCredential;
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}
	
	private File findFile(String name, String fields)
	throws FileStorageException
	{
		try
		{
			Drive service = getService(getScopedCredential());
			FileList fileList = service.files().list()
				.setFields(String.format("files(name, parents, %s)", fields))
				.execute();
			for (File file : fileList.getFiles())
				if (file.getParents().contains(folderId)
						&& name.equals(file.getName()))
					return file;
			return null;
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}
	
	private static URI createRef(File file)
	throws URISyntaxException
	{
		URI uri = new URI(file.getWebContentLink());
		
		String sep = "";
		StringBuilder query = new StringBuilder();
		for (String param : uri.getQuery().split("&"))
		{
			if (!"export=download".equals(param))
				query.append(sep).append(param);
			sep = "&";
		}
		return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(),
				query.toString(), uri.getFragment());
	}
}
