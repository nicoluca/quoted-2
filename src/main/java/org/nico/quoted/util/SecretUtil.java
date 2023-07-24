package org.nico.quoted.util;

import org.springframework.beans.factory.annotation.Value;

public class SecretUtil {

    @Value("${quoted.secret.salt}")
    private static String salt;

    public static boolean isCorrectSecret(int secret, String email) {
        String emailSalt = email + salt;
        return secret == emailSalt.hashCode();
    }

    public static int getSecret(String email) {
        String emailSalt = email + salt;
        return emailSalt.hashCode();
    }

}
