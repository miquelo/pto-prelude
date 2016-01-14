package net.preparatusopos.app.notification;

public interface Notifier
{
	public void messageSend(NotifierMessage message)
	throws NotifierException;
}
