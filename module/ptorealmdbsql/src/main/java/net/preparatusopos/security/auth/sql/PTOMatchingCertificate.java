package net.preparatusopos.security.auth.sql;

import java.security.cert.Certificate;

public class PTOMatchingCertificate
{
	private long userID;
	private Certificate certificate;
	
	public PTOMatchingCertificate(long userID, Certificate certificate)
	{
		this.userID = userID;
		this.certificate = certificate;
	}

	public long getUserID()
	{
		return userID;
	}

	public Certificate getCertificate()
	{
		return certificate;
	}
}
