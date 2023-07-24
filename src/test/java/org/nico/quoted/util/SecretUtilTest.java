package org.nico.quoted.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecretUtilTest {

    @Test
    void isCorrectSecret() {
        String email = "some@email.com";
        int secret = SecretUtil.getSecret(email);
        assertTrue(SecretUtil.isCorrectSecret(secret, email));
    }

    @Test
    void getSecret() {
        String email = "email";
        int secret = SecretUtil.getSecret(email);
        assertEquals(secret, SecretUtil.getSecret(email));
    }
}