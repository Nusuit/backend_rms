package org.example.rms.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailService {
    public void sendEmail(MimeMessage message);
    public MimeMessage createMimeMessage(String from, String to, String subject, String text) throws MessagingException;
}
