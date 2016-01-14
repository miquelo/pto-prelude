package net.preparatusopos.security.auth;

public class PTOCredentialDuplicatedException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private PTOCredential credential;
	
	public PTOCredentialDuplicatedException(PTOCredential credential)
	{
		super(newMessage(credential));
		this.credential = credential;
	}
	
	public PTOCredential getCredential()
	{
		return credential;
	}
	
	private static String newMessage(PTOCredential credential)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Credential ").append(credential).append(" already exists");
		return msg.toString();
	}
}
