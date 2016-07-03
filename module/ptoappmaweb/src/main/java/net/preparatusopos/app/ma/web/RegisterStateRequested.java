package net.preparatusopos.app.ma.web;


public class RegisterStateRequested
extends RegisterState
{
	private static final String STATE_NAME = "requested";
	
	private String mailAddress;
	
	public RegisterStateRequested(RegisterBean owner)
	{
		super(owner);
	}

	@Override
	public String getStateName()
	{
		return STATE_NAME;
	}
	
	@Override
	public String getMailAddress()
	{
		return mailAddress;
	}

	@Override
	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}

	@Override
	public void init()
	{
	}
}