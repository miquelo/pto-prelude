package net.preparatusopos.app.domain;

public class NoSuchCredentialRequestException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private String tokenHex;
	
	public NoSuchCredentialRequestException(String tokenHex)
	{
		super(newMessage(tokenHex));
		this.tokenHex = tokenHex;
	}
	
	public String getTokenHex()
	{
		return tokenHex;
	}

	private static String newMessage(String tokenHex)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Credential request not found: ").append(tokenHex);
		return msg.toString();
	}
}
