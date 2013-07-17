package com.byob.utils.mail;

import com.google.inject.AbstractModule;

public class MailerModule extends AbstractModule {

	private final String accessKey;
	private final String secretKey;
	
	
	public MailerModule(String accessKey, String secretKey) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	@Override
	protected void configure() {
		bind(Mailer.class).toInstance(new MailerImpl(accessKey,secretKey));
	}

}
