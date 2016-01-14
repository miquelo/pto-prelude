package net.preparatusopos.app.ma.web;

public class RegisterStateCompleted
extends RegisterState
{
	private static final String STATE_NAME = "completed";
	
	public RegisterStateCompleted(RegisterBean owner)
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