package org.nico.quoted.util;

public class StringUtil {
    public static boolean isCorrectEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static String trim(String string) {
        // Trim string of leading and trailing whitespace and trailing and leading quotes
        return string
                .replaceAll("^\\s*", "")
                .replaceAll("\\s*$", "")
                .replaceAll("^\"\\s*", "")
                .replaceAll("\\s*\"$", "")
                .replaceAll("^(%22)*", "")
                .replaceAll("(%22)*$", "");
    }
}
