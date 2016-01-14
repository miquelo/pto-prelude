package net.preparatusopos.security.auth;

import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Principal;

public class PTOPrincipal
implements Principal, Serializable
{
	private static final long serialVersionUID = 2421668158243060194L;
	
	private long userID;
	
	public PTOPrincipal(long userID)
	{
		this.userID = userID;
	}
	
	@Override
	public String getName()
	{
		return String.valueOf(userID);
	}
	
	public long getUserID()
	{
		return userID;
	}
	
	@Override
	public int hashCode()
	{
		return (int) (userID % 200l);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof PTOPrincipal)
		{
			PTOPrincipal principal = (PTOPrincipal) obj;
			return userID == principal.userID;
		}
		return false;
	}
	
	private void readObject(ObjectInputStream input)
	throws IOException, NotActiveException, ClassNotFoundException
	{
		userID = input.readLong();
	}
	
	private void writeObject(ObjectOutputStream output)
	throws IOException
	{
		output.writeLong(userID);
	}
}
