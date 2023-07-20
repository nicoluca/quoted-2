package org.nico.quoted.util;

public class StringUtil {
    public static boolean isCorrectEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
