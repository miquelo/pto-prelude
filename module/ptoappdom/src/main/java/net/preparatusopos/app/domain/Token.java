package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

public class Token
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final String SECURE_ALGORITHM = "SHA1PRNG";
	private static final String HASH_ALGORITHM = "SHA-1";
	
	private static SecureRandom secureRandom = null;
	
	private byte[] value;
	
	@ConstructorProperties({
		"value"
	})
	public Token(byte[] value)
	{
		this.value = value;
	}
	
	public boolean isEmpty()
	{
		return value == null;
	}

	public byte[] getValue()
	{
		return value;
	}
	
	@Override
	public String toString()
	{
		return DatatypeConverter.printHexBinary(value);
	}
	
	public static Token fromEmptyValue()
	{
		return new Token(null);
	}
	
	public static byte[] createValue()
	{
		try
		{
			if (secureRandom == null)
				secureRandom = SecureRandom.getInstance(SECURE_ALGORITHM);
			String randomNum = String.valueOf(secureRandom.nextInt());
			MessageDigest sha1 = MessageDigest.getInstance(HASH_ALGORITHM);
			return sha1.digest(randomNum.getBytes());
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}
	
	public static Token fromHexValue(String hexValue)
	{
		return new Token(DatatypeConverter.parseHexBinary(hexValue));
	}
}
