package org.example.rms.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EmailServiceImp implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(MimeMessage message) {
        mailSender.send(message);
    }

    public MimeMessage createMimeMessage(String from, String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        return message;
    }


}
