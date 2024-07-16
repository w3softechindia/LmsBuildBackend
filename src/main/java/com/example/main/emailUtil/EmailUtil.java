package com.example.main.emailUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendMail(String employeeId, String firstName, String lastName, String webMail, String webMailPassword,
			String roles, long phoneNumber,String dateOfJoin, String employeePassword, String employeeEmail) throws Exception {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mn = new MimeMessageHelper(mimeMessage);
		mn.setFrom("sricharan824@gmail.com", "W3softech");
		mn.setTo(employeeEmail);
		mn.setSubject("Welcome On Board");
		String body = String.format("<html>" + "<body>" + "<h1>Welcome On Board </h1>"
				+ "<p>We are thrilled to have you join our team. Below are your account details:</p>"
				+"<p><strong>Employee ID: " + employeeId + "</strong></p>"
				+ "<p><strong>Name : " + firstName + " " + lastName + "</strong></p>" + "<p><strong>Web Mail: "
				+ webMail + "</strong></p>" + "<p><strong>Web Mail Password: " + webMailPassword + "</strong></p>"
				+ "<p><strong>Role: " + roles + "</strong></p>" + "<p><strong>Phone Number: " + phoneNumber
				+ "<p><strong>Date of Join: " + dateOfJoin
				+ "</strong></p>" + "<p><strong>Employee Email: " + employeeEmail + "</strong></p>"
				+ "<p><strong>Employee Password: " + employeePassword + "</strong></p>"
				+ "<p>Please keep this information secure and do not share it with anyone.</p>" + "<p>Best regards,</p>"
				+ "<p>The W3 Softech Team</p>" + "</body>" + "</html>");

		mn.setText(body, true);
		javaMailSender.send(mimeMessage);
	}
}
