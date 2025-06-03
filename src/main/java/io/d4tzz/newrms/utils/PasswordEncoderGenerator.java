package io.d4tzz.newrms.utils; // Hoặc package khác tùy bạn

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("123123");
        System.out.println("Hashed password for '123123': " + hashedPassword);
    }
}
