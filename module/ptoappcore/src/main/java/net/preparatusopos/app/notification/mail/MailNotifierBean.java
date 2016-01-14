package net.preparatusopos.app.notification.mail;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import net.preparatusopos.app.notification.Notifier;
import net.preparatusopos.app.notification.NotifierException;
import net.preparatusopos.app.notification.NotifierMessage;

@Remote(
	Notifier.class
)
@Stateless(
	mappedName="ejb/MailNotifier"
)
public class MailNotifierBean
implements Notifier
{
	private static final String MAIL_PASSWORD_PROPERTY_NAME = "mailPassword";
	
	@Resource(
		lookup="properties/PTOSettings"
	)
	private Properties settings;
	
	@Resource(
		lookup="mail/PTOMailSession"
	)
	private Session mailSession;
	
	public MailNotifierBean()
	{
		mailSession = null;
	}
	
	@Override
	public void messageSend(NotifierMessage message)
	throws NotifierException
	{
		try
		{
			String mailPasswd = settings.getProperty(
					MAIL_PASSWORD_PROPERTY_NAME);
			
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(mailSession.getProperty("mail.from"));
			msg.setSubject(message.getSubjectText());
			msg.setRecipients(RecipientType.TO, message.getTarget());
			msg.setText(message.getBodyText());
			Transport.send(msg, mailSession.getProperty("mail.user"),
					mailPasswd);
		}
		catch (MessagingException exception)
		{
			throw new NotifierException(exception.getMessage());
		}
	}
}
