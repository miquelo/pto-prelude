package net.preparatusopos.app.domain;

import java.io.Serializable;

/**
 * External user ID.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 *
 */
public class ExternalUID
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String userID;
	private ExternalUIDDomain domain;
	
	public ExternalUID()
	{
		userID = null;
		domain = null;
	}
	
	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public ExternalUIDDomain getDomain()
	{
		return domain;
	}

	public void setDomain(ExternalUIDDomain domain)
	{
		this.domain = domain;
	}

	@Override
	public int hashCode()
	{
		return domain == null ? 0 : domain.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ExternalUID)
		{
			ExternalUID uid = (ExternalUID) obj;
			return userID != null && userID.equals(uid.userID) &&
					domain != null && domain.equals(uid.domain);
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s@%s", userID, domain);
	}
	
	public static ExternalUID parse(String str)
	{
		ExternalUID externalUID = new ExternalUID();
		ExternalUIDDomain domain = ExternalUIDDomain.resolve(str);
		externalUID.setUserID(domain.parse(str));
		externalUID.setDomain(domain);
		return externalUID;
	}
}
