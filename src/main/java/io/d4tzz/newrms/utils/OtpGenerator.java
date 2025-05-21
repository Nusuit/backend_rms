package io.d4tzz.newrms.utils;

import java.security.SecureRandom;

public class OtpGenerator {
    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}
