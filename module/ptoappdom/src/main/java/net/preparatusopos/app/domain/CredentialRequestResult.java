package net.preparatusopos.app.domain;

import java.io.Serializable;

public class CredentialRequestResult
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CredentialRequest request;
	
	public CredentialRequestResult()
	{
		request = null;
	}

	public CredentialRequest getRequest()
	{
		return request;
	}

	public void setRequest(CredentialRequest request)
	{
		this.request = request;
	}
}
