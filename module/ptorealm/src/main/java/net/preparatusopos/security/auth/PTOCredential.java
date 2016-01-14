package net.preparatusopos.security.auth;

import java.io.Serializable;

public interface PTOCredential
extends Serializable
{
	public static final int TYPE_PASSWORD = 1;
	public static final int TYPE_CERTIFICATE = 2;
	public static final int TYPE_EXTERNAL = 3;
	
	public int getType();
	
	public String getUsername();
	
	public char[] getPassword();
}
