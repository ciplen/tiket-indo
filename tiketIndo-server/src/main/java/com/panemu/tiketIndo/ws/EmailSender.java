package com.panemu.tiketIndo.ws;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
//import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mubin
 */
@Stateless
public class EmailSender {

	private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);
	@Resource(name = "java:jboss/mail/TiketIndo")
	private Session session;
	@Resource
	private ManagedExecutorService mes;

	public EmailSender() {
	}

	public void send(String[] recipients, String messageText, File attachment, boolean deleteAttachment, String subject) throws IOException, Exception {
		Runnable r = () -> {
			try {
				Message msg = new MimeMessage(session);
				InternetAddress[] toAddresses = new InternetAddress[recipients.length];
				for (int i = 0; i < recipients.length; i++) {
					toAddresses[i] = new InternetAddress(recipients[i]);
				}
				msg.setRecipients(Message.RecipientType.TO, toAddresses);
				msg.setSubject(subject);
				buildEmailContent(msg, messageText, attachment);
				Transport.send(msg);
				if (deleteAttachment && attachment != null) {
					attachment.delete();
				}
				logger.info("Success send email");
			} catch (Exception e) {
				logger.error("Failed to send email", e);
			}
		};
		mes.execute(r);
	}

	private void buildEmailContent(Message msg, String messageText, File tmpFile) throws Exception {
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(messageText);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		messageBodyPart = new MimeBodyPart();

		if (tmpFile != null) {
			DataSource source = new FileDataSource(tmpFile.getAbsolutePath());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("ticket_final.pdf");
		}
		multipart.addBodyPart(messageBodyPart);
		msg.setContent(multipart);
	}

	public void sendBooking(String[] recipients, String content, String url) throws IOException, Exception {
		Runnable r = () -> {
			try {
				Message msg = new MimeMessage(session);
				InternetAddress[] toAddresses = new InternetAddress[recipients.length];
				for (int i = 0; i < recipients.length; i++) {
					toAddresses[i] = new InternetAddress(recipients[i]);
				}
				msg.setRecipients(Message.RecipientType.TO, toAddresses);
				msg.setSubject("Konfirmasi Pembayaran");
				msg.setContent(content, "text/html");
				Transport.send(msg);
				logger.info("Success send email");
			} catch (Exception e) {
				logger.error("Failed to send email", e);
			}
		};
		mes.execute(r);
	}

	public void sendConfirm(String content, String url, String[] recipients) throws IOException, Exception {
		Runnable r = () -> {
			try {
				Message msg = new MimeMessage(session);
				InternetAddress[] toAddresses = new InternetAddress[recipients.length];
				for (int i = 0; i < recipients.length; i++) {
					toAddresses[i] = new InternetAddress(recipients[i]);
				}
				msg.setRecipients(Message.RecipientType.TO, toAddresses);

				msg.setSubject("Ask Ticket Order");
				msg.setContent(content, "text/html");
				Transport.send(msg);
				logger.info("Success send email");
			} catch (Exception e) {
				logger.error("Failed to send email", e);
			}
		};
		mes.execute(r);
	}

	//sendConfirm & sendBooking method should be deleted, it has the same workflow. Use the method with subject as parameters
	public void sendTemplate(String subject, String content, String url, String[] recipients) throws IOException, Exception {
		Runnable r = () -> {
			try {
				Message msg = new MimeMessage(session);
				InternetAddress[] toAddresses = new InternetAddress[recipients.length];
				for (int i = 0; i < recipients.length; i++) {
					toAddresses[i] = new InternetAddress(recipients[i]);
				}
				msg.setRecipients(Message.RecipientType.TO, toAddresses);

				msg.setSubject(subject);
				msg.setContent(content, "text/html");
				Transport.send(msg);
				logger.info("Success send email");
			} catch (Exception e) {
				logger.error("Failed to send email", e);
			}
		};
		mes.execute(r);
	}
}
