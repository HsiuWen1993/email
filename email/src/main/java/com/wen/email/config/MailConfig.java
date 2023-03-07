package com.wen.email.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.wen.base.exception.BusinessExcetion;
import com.wen.base.exception.MessageCode;

@Configuration
public class MailConfig {

	@Autowired
	private Environment environment;

	@Bean
	public JavaMailSender getJavaMailSender() {

		String username = environment.getProperty("EMAIL_USERNAME");
		String pwd = environment.getProperty("EMAIL_PWD");
		if (username.isBlank() || pwd.isBlank())
			throw new BusinessExcetion(MessageCode.CODE2001);

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername(username);
		mailSender.setPassword(pwd);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

}
