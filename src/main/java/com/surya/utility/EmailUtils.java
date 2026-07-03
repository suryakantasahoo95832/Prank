package com.surya.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	JavaMailSender sender;

	public boolean sendEmail(String to, String subject, String body) {

		MimeMessage mimeMessage = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		try {
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			sender.send(mimeMessage);

			return true;
		} catch (MessagingException e) {

			e.printStackTrace();
		}

		return false;
	}

	public boolean recoveryMail(String to, String sub, String body) {

		MimeMessage mimeMessage = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		try {
			helper.setTo(to);
			helper.setSubject(sub);
			helper.setText(body, true);
			sender.send(mimeMessage);
			
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return false;
	}
}









