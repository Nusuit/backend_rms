package io.d4tzz.newrms.service;

public interface EmailService {
    void sendEmail(String from, String to, String subject, String text);
}
