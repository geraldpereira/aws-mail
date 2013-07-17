package com.byob.utils.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

final class MailerImpl implements Mailer {

	private final AmazonSimpleEmailServiceClient client;
	
	public MailerImpl(final String accessKey, final String secretKey){
		client = new AmazonSimpleEmailServiceClient(
				new BasicAWSCredentials(accessKey,secretKey));
	}
	
	public void mail(String subject, String content, String from, String to)
			throws AddressException, MessagingException, IOException {
		// JavaMail representation of the message
		Session s = Session.getInstance(new Properties(), null);
		MimeMessage msg = new MimeMessage(s);

		// Sender and recipient
		msg.setFrom(new InternetAddress(from));
		msg.setRecipient(RecipientType.TO, new InternetAddress(
				to));

		// Subject
		msg.setSubject(subject);

		// Add a MIME part to the message
		MimeMultipart mp = new MimeMultipart();

		MimeBodyPart part = new MimeBodyPart();
		part.setContent(content, "text/html");
		mp.addBodyPart(part);

		msg.setContent(mp);

		// Print the raw email content on the console
		// PrintStream out = System.out;
		// msg.writeTo(out);

		// Capture the raw message
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		msg.writeTo(out);

		RawMessage rm = new RawMessage();
		rm.setData(ByteBuffer.wrap(out.toString().getBytes()));

		// Call Amazon SES to send the message
		client.sendRawEmail(new SendRawEmailRequest().withRawMessage(rm));
	}

}
