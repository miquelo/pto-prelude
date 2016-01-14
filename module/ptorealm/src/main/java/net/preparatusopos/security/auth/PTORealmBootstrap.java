package net.preparatusopos.security.auth;

import java.beans.ConstructorProperties;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;

public class PTORealmBootstrap
{
	private PTORealmBootstrap()
	{
	}
	
	public static PTORealmManagerFactory createManagerFactory()
	{
		return new PTORealmManagerFactoryImpl();
	}
	
	public static PTOCredentialFactory createCredentialFactory()
	{
		return new PTOCredentialFactoryImpl();
	}
	
	private static char[] newEmptyPassword()
	{
		return new char[0];
	}
	
	private static class PTORealmManagerFactoryImpl
	implements PTORealmManagerFactory
	{
		public PTORealmManagerFactoryImpl()
		{
		}
		
		@Override
		public PTORealmManager getManager(PTORealmConnection connection)
		throws PTORealmManagerException
		{
			return new PTORealmManagerImpl(connection);
		}
	}
	
	private static class PTORealmManagerImpl
	implements PTORealmManager
	{
		private PTORealmConnection connection;
		private Map<PTOPrincipal, PTOManagedPrincipal> managedMap;
		
		public PTORealmManagerImpl(PTORealmConnection connection)
		{
			this.connection = connection;
			managedMap = new HashMap<PTOPrincipal, PTOManagedPrincipal>();
		}
		
		@Override
		public PTOPrincipal resolve(String name)
		throws PTORealmManagerException
		{
			try
			{
				return connection.resolve(name);
			}
			catch (PTORealmConnectionException exception)
			{
				throw new PTORealmManagerException(exception);
			}
		}
		
		@Override
		public PTOManagedPrincipal manage(PTOPrincipal principal)
		throws PTORealmManagerException
		{
			PTOManagedPrincipal managed = managedMap.get(principal);
			if (managed == null)
			{
				managed = new PTOManagedPrincipalImpl(connection, principal);
				managedMap.put(principal, managed);
			}
			return managed;
		}
		
		@Override
		public PTOPrincipal authenticate(PTOCredential credential)
		throws PTORealmManagerException
		{
			try
			{
				return connection.matching(credential);
			}
			catch (PTORealmConnectionException exception)
			{
				throw new PTORealmManagerException(exception);
			}
		}
	}
	
	private static class PTOManagedPrincipalImpl
	implements PTOManagedPrincipal
	{
		private PTORealmConnection connection;
		private PTOPrincipal principal;
		private PTOCredentialList credentialList;
		
		public PTOManagedPrincipalImpl(PTORealmConnection connection,
				PTOPrincipal principal)
		{
			this.connection = connection;
			this.principal = principal;
			credentialList = null;
		}
		
		@Override
		public Set<String> getGroups()
		throws PTORealmManagerException
		{
			try
			{
				return Collections.unmodifiableSet(connection.getGroups(
						principal));
			}
			catch (PTORealmConnectionException exception)
			{
				throw new PTORealmManagerException(exception);
			}
		}
		
		@Override
		public PTOCredentialList getCredentials()
		throws PTORealmManagerException
		{
			if (credentialList == null)
				credentialList = new PTOCredentialListImpl(connection,
						principal);
			return credentialList;
		}
	}
	
	private static class PTOCredentialListImpl
	implements PTOCredentialList
	{
		private PTORealmConnection connection;
		private PTOPrincipal principal;
		private List<PTOCredential> credentials;
		
		public PTOCredentialListImpl(PTORealmConnection connection,
				PTOPrincipal principal)
		throws PTORealmManagerException
		{
			try
			{
				this.connection = connection;
				this.principal = principal;
				credentials = this.connection.getCredentials(this.principal);
			}
			catch (PTORealmConnectionException exception)
			{
				throw new PTORealmManagerException(exception);
			}
		}
		
		@Override
		public Iterator<PTOCredential> iterator()
		{
			return credentials.iterator();
		}
		
		@Override
		public void add(PTOCredential credential)
		throws PTORealmManagerException, PTOCredentialDuplicatedException
		{
			try
			{
				connection.addCredential(principal, credential);
				credentials.add(credential);
			}
			catch (PTORealmConnectionException exception)
			{
				throw new PTORealmManagerException(exception);
			}
		}
		
		@Override
		public PTOCredential remove(int index)
		throws PTORealmManagerException
		{
			try
			{
				connection.removeCredential(principal, index);
				return credentials.remove(index);
			}
			catch (PTORealmConnectionException exception)
			{
				throw new PTORealmManagerException(exception);
			}
		}
	}
	
	private static class PTOCredentialFactoryImpl
	implements PTOCredentialFactory
	{
		public PTOCredentialFactoryImpl()
		{
		}
		
		@Override
		public PTOCredential newPassword(String username, char[] password)
		{
			return new PTOCredentialImpl(PTOCredential.TYPE_PASSWORD, username,
					password);
		}

		@Override
		public PTOCredential newCertificate(Certificate certificate)
		{
			if (certificate instanceof X509Certificate)
			{
				X509Certificate x509cert = (X509Certificate) certificate;
				X500Principal principal = x509cert.getSubjectX500Principal();
				return new PTOCredentialImpl(PTOCredential.TYPE_CERTIFICATE,
						principal.getName(), newEmptyPassword());
			}
			StringBuilder msg = new StringBuilder();
			msg.append("Cannot determine username from certificate of type ");
			msg.append(certificate.getClass());
			throw new UnsupportedOperationException(msg.toString());
		}

		@Override
		public PTOCredential newCertificate(String principalName)
		{
			return new PTOCredentialImpl(PTOCredential.TYPE_CERTIFICATE,
					principalName, newEmptyPassword());
		}

		@Override
		public PTOCredential newExternal(String userID)
		{
			return new PTOCredentialImpl(PTOCredential.TYPE_EXTERNAL, userID,
					newEmptyPassword());
		}
	}
	
	private static class PTOCredentialImpl
	implements PTOCredential
	{
		private static final long serialVersionUID = 1L;
		
		private int type;
		private String username;
		private char[] password;
		
		@ConstructorProperties({
			"type",
			"username",
			"password"
		})
		public PTOCredentialImpl(int type, String username, char[] password)
		{
			this.type = type;
			this.username = username;
			this.password = password;
		}
		
		@Override
		public int getType()
		{
			return type;
		}

		@Override
		public String getUsername()
		{
			return username;
		}

		@Override
		public char[] getPassword()
		{
			return password;
		}
	}
}
