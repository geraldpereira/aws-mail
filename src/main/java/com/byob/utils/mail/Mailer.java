package com.byob.utils.mail;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface Mailer {

	void mail(String subject, String content, String from, String to) throws AddressException, MessagingException, IOException;
}
