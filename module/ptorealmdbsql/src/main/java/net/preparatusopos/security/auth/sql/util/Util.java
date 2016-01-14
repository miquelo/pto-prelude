package net.preparatusopos.security.auth.sql.util;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

public class Util
{
	private static final String LOGGER_NAME =
			"net.preparatusopos.security.auth.database";
	
	private static final String CERTIFICATE_HASH_DIGEST_ALG = "SHA-256";
	
	private static MessageDigest certificateHashDigest;
	
	static
	{
		certificateHashDigest = null;
	}
	
	private Util()
	{
	}
	
	public static Logger getLogger()
	{
		return Logger.getLogger(LOGGER_NAME);
	}
	
	public static byte[] certificateHash(Certificate cert)
	throws NoSuchAlgorithmException
	{
		if (cert instanceof X509Certificate)
		{
			X509Certificate x509Cert = (X509Certificate) cert;
			byte[] bytes = toBytes(x509Cert.getSubjectUniqueID());
			return getCertificateHashDigest().digest(bytes);
			
		}
		StringBuilder msg = new StringBuilder();
		msg.append("Certificate hashing for cetificate of type '");
		msg.append(cert.getType());
		msg.append("' is not supported");
		throw new UnsupportedOperationException(msg.toString());
	}
	
	private static MessageDigest getCertificateHashDigest()
	throws NoSuchAlgorithmException
	{
		if (certificateHashDigest == null)
			certificateHashDigest = MessageDigest.getInstance(
					CERTIFICATE_HASH_DIGEST_ALG);
		return certificateHashDigest;
	}
	
	private static byte[] toBytes(boolean[] booleanArray)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = 0;
		int b = 0;
		for (boolean bi : booleanArray)
		{
			b = b | (bi ? 1 << i : 0);
			if (++i == 8)
			{
				baos.write(b);
				i = b = 0;
			}
		}
		return baos.toByteArray();
	}
}
