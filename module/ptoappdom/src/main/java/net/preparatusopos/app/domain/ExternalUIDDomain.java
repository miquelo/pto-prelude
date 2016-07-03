package net.preparatusopos.app.domain;

/**
 * External user ID domain.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 *
 */
public enum ExternalUIDDomain
{
	/**
	 * Google domain.
	 */
	GOOGLE("account.google.com");
	
	private String domainName;
	
	private ExternalUIDDomain(String domainName)
	{
		this.domainName = domainName;
	}
	
	/**
	 * Format the user ID.
	 * 
	 * @param userID
	 * 			The user ID to be formatted.
	 * 
	 * @return
	 * 			The formatted user ID.
	 */
	public String format(String userID)
	{
		return String.format("%s@%s", userID, domainName);
	}
	
	/**
	 * Parse user ID from user name.
	 * 
	 * @param username
	 * 			User name to be parsed.
	 * 
	 * @return
	 * 			Parsed user ID.
	 */
	public String parse(String username)
	{
		return username.replace(String.format("@%s", domainName), "");
	}
	
	@Override
	public String toString()
	{
		return domainName;
	}
	
	/**
	 * Resolve external UID type from user ID.
	 * 
	 * @param userID
	 * 			Source user ID.
	 * 
	 * @return
	 * 			Resolved external UID.
	 */
	public static ExternalUIDDomain resolve(String userID)
	{
		for (ExternalUIDDomain type : ExternalUIDDomain.values())
			if (userID.endsWith(String.format("@%s", type.domainName)))
				return type;
		
		StringBuilder msg = new StringBuilder();
		msg.append("Unknown external UID type for '");
		msg.append(userID).append("' user ID");
		throw new IllegalArgumentException(msg.toString());
	}
}
