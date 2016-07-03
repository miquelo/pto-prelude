package net.preparatusopos.app.core.notification;

public interface Notifier
{
	public void messageSend(NotifierMessage message)
	throws NotifierException;
}
