package com.example.main.emailConfiguration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

	@Value("${spring.mail.host}")
	private String mailHost;

	@Value("${spring.mail.port}")
	private String mailPort;

	@Value("${spring.mail.username}")
	private String mailUsername;

	@Value("${spring.mail.password}")
	private String mailPassword;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailsender = new JavaMailSenderImpl();
		mailsender.setHost(mailHost);
		mailsender.setPort(Integer.parseInt(mailPort));
		mailsender.setUsername(mailUsername);
		mailsender.setPassword(mailPassword);

		Properties props = mailsender.getJavaMailProperties();

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Important: Trust the Gmail server
		props.put("mail.smtp.connectiontimeout", "5000");
		props.put("mail.smtp.timeout", "5000");
		props.put("mail.smtp.writetimeout", "5000");
		return mailsender;

	}

}
