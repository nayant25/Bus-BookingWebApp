package org.jsp.reservationapp.service;

import org.jsp.reservationapp.dto.EmailConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ReservationApiMailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public String sendMail(EmailConfiguration emailconfiguration) {
		SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
		simpleMailMessage.setTo(emailconfiguration.getToAddress());
		simpleMailMessage.setText(emailconfiguration.getText());
		simpleMailMessage.setSubject(emailconfiguration.getSubject());
		javaMailSender.send(simpleMailMessage);
		return "Registration succesfully and Verificationmail has been send";
	}

	

	

}
