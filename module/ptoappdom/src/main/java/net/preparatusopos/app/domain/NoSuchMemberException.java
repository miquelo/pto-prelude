package net.preparatusopos.app.domain;

public class NoSuchMemberException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private long memberID;
	
	public NoSuchMemberException(long memberID)
	{
		super(newMessage(memberID));
		this.memberID = memberID;
	}
	
	public long getMemberID()
	{
		return memberID;
	}
	
	private static String newMessage(long memberID)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Member ").append(memberID).append(" does not exist");
		return msg.toString();
	}
}
