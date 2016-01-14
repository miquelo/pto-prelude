package net.preparatusopos.app.core.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import net.preparatusopos.app.core.model.Member;
import net.preparatusopos.app.domain.NoSuchMemberException;
import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;
import net.preparatusopos.security.auth.sql.PTOSQLRealmConnection;
import net.preparatusopos.security.auth.sql.PTOSQLRealmSettings;

public class Util
{
	private static final String SECURE_ALGORITHM = "SHA1PRNG";
	private static final String HASH_ALGORITHM = "SHA-1";
	
	private static final String SCHEMA = "PTOAPP";
	private static final String VW_MEMBERGROUP = "VW_MEMBERGROUP";
	private static final String TB_MEMBERCRED = "TB_MEMBERCRED";
	
	private static SecureRandom random = null;
	
	private Util()
	{
	}
	
	public static Logger getLogger()
	{
		return Logger.getLogger(" net.preparatusopos.app.domain");
	}
	
	public static PTORealmConnection getRealmConnection(
			DataSource realmDataSource)
	throws PTORealmConnectionException
	{
		try
		{
			PTOSQLRealmSettings settings = new PTOSQLRealmSettings();
			settings.setSchema(SCHEMA);
			settings.setGroupViewName(VW_MEMBERGROUP);
			settings.setCredTableName(TB_MEMBERCRED);
			Connection connection = realmDataSource.getConnection();
			return new PTOSQLRealmConnection(settings, connection);
		}
		catch (SQLException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Data base connection could not be established");
			throw new PTORealmConnectionException(msg.toString(), exception);
		}
	}
	
	public static byte[] createToken()
	{
		try
		{
			if (random == null)
				random = SecureRandom.getInstance(SECURE_ALGORITHM);
			String randomNum = String.valueOf(random.nextInt());
			MessageDigest sha1 = MessageDigest.getInstance(HASH_ALGORITHM);
			return sha1.digest(randomNum.getBytes());
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}
	
	public static String normalizeMailAddress(String mailAddr)
	{
		String[] parts = mailAddr.split("@");
		if (parts.length < 2)
		{
			StringBuilder msg = new StringBuilder();
			msg.append(mailAddr).append(" is not a valid mail address");
			throw new IllegalArgumentException(msg.toString());
		}
		
		String username = parts[0].replace(".", "");
		return String.format("%s@%s", username, parts[1]);
	}
	
	public static Member findMember(EntityManager em, PTOPrincipal principal)
	throws NoSuchMemberException
	{
		Member member = em.find(Member.class, principal.getUserID());
		if (member == null)
			throw new NoSuchMemberException(principal.getUserID());
		return member;
	}
}