package net.preparatusopos.app.ma.web;

public class RegisterStateMailRequested
extends RegisterState
{
	private static final String STATE_NAME = "mail-requested";
	
	public RegisterStateMailRequested(RegisterBean owner)
	{
		super(owner);
	}

	@Override
	public String getStateName()
	{
		return STATE_NAME;
	}
	
	@Override
	public void init()
	{
	}
}