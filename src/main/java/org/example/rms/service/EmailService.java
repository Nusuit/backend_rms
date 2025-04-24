package org.example.rms.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailService {
//    void sendEmail(MimeMessage message);
//
//    MimeMessage createMimeMessage(String from, String to, String subject, String text) throws MessagingException;

    void sendEmail(String from, String to, String subject, String text) throws MessagingException;
}
