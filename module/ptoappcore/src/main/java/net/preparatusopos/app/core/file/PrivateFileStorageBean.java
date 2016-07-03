package net.preparatusopos.app.core.file;

import java.io.IOException;
import java.net.URI;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.preparatusopos.app.core.domain.model.ManagedFile;
import net.preparatusopos.tools.file.FileStorageConnection;
import net.preparatusopos.tools.file.FileStorageConnectionFactory;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;
import net.preparatusopos.tools.file.StoredFileSpecs;

@Remote(
	PrivateFileStorage.class
)
@Stateless(
	mappedName="ejb/PrivateFileStorageBean"
)
@DeclareRoles({
	"ADMIN",
	"MEMBER"
})
public class PrivateFileStorageBean
implements PrivateFileStorage
{
	private static final String DEVICE_NAME = "google-drive-001";
	
	@Resource(
		lookup="file/PTOGoogleDriveFileStorage"
	)
	private transient FileStorageConnectionFactory fileStgGoogleDriveFact;
	
	@PersistenceContext(
		unitName="PTOApplicationUnit"
	)
	private EntityManager em;

	public PrivateFileStorageBean()
	{
		fileStgGoogleDriveFact = null;
		em = null;
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public String getProviderName(PrivateFile privateFile)
	throws FileStorageException
	{
		try (FileStorageConnection conn =
				fileStgGoogleDriveFact.getConnection())
		{
			return conn.getProviderName();
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public StoredFile find(String name)
	throws FileStorageException
	{
		ManagedFile managedFile = ManagedFile.find(em, DEVICE_NAME, name);
		return managedFile.getStoredFile(getProviderName(
				managedFile.transport()));
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public URI createPublicRef(StoredFile storedFile)
	throws FileStorageException
	{
		try (FileStorageConnection conn =
				fileStgGoogleDriveFact.getConnection())
		{
			return conn.createPublicRef(storedFile);
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public PrivateFile store(StoredFileSpecs specs)
	throws FileStorageException
	{
		try (FileStorageConnection conn =
				fileStgGoogleDriveFact.getConnection())
		{
			StoredFile storedFile = conn.store(specs);
			ManagedFile managedFile = ManagedFile.create(DEVICE_NAME,
					storedFile);
			return managedFile.transport();
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public void submit(StoredFile storedFile)
	throws FileStorageException
	{
		// Nothing to be done
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public void forget(StoredFile storedFile)
	throws FileStorageException
	{
		// Nothing to be done
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public boolean remove(StoredFile storedFile)
	throws FileStorageException
	{
		try (FileStorageConnection conn =
				fileStgGoogleDriveFact.getConnection())
		{
			String name = storedFile.getName();
			em.remove(ManagedFile.find(em, DEVICE_NAME, name));
			return conn.remove(storedFile);
		}
		catch (IOException exception)
		{
			throw new FileStorageException(exception);
		}
	}
}
