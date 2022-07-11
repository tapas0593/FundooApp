package com.bridgelabz.fundooapp.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.bridgelabz.fundooapp.exception.UserException;

public class EmailUtil {
	private EmailUtil() {
	}

	@Autowired
	private static JavaMailSender javaMailSender;

	public static void sendEmail(String toEmail, String subject, String body) {

		String fromEmail = "sample@gmail.com";
		String password = "Password@!234";

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(prop, auth);
		send(session, fromEmail, toEmail, subject, body);
	}

	private static void send(Session session, String fromEmail, String toEmail, String subject, String body) {
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail, "Aastha"));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (Exception e) {
			throw new UserException(404, e.getMessage());
		}
	}

//	public static void sendEmail(String toEmail, String subject, String body) {
//		MimeMessage msg = javaMailSender.createMimeMessage();
//		MimeMessageHelper helper;
//		try {
//			helper = new MimeMessageHelper(msg);
//			helper.setTo(toEmail);
//			helper.setSubject(subject);
//			// helper.setText("Check attachment for image!");
//			helper.setText(body);
//		} catch (MessagingException e) {
//			throw new UserException(500, e.getMessage());	
//		}
//
////      helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
//
//		javaMailSender.send(msg);

//		SimpleMailMessage msg = new SimpleMailMessage();
//		msg.setTo(toEmail);
//
//		msg.setSubject(subject);
//		msg.setText(body);
//
//		javaMailSender.send(msg);
//
//	}

}
