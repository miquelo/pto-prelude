package net.preparatusopos.app.core.file;

import java.net.URI;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import net.preparatusopos.app.domain.FileStorage;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;

@Remote(
	FileStorage.class
)
@Stateless(
	name="ejb/FileStorageBean"
)
@DeclareRoles({
	"ADMIN",
	"MEMBER"
})
public class FileStorageBean
implements FileStorage
{
	@EJB(
		lookup="ejb/PrivateFileStorageBean"
	)
	private PrivateFileStorage fileStorage;

	public FileStorageBean()
	{
		fileStorage = null;
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public StoredFile find(String name)
	throws FileStorageException
	{
		return fileStorage.find(name);
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public URI createPublicRef(StoredFile storedFile)
	throws FileStorageException
	{
		return fileStorage.createPublicRef(storedFile);
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public void submit(StoredFile storedFile)
	throws FileStorageException
	{
		fileStorage.submit(storedFile);
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public void forget(StoredFile storedFile)
	throws FileStorageException
	{
		fileStorage.forget(storedFile);
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public boolean remove(StoredFile storedFile)
	throws FileStorageException
	{
		return fileStorage.remove(storedFile);
	}
}
