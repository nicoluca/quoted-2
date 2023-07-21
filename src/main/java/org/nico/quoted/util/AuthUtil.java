package org.nico.quoted.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.logging.Logger;

public class AuthUtil {

    private static final Logger logger = Logger.getLogger(AuthUtil.class.getName());

    public static String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        logger.info("AuthUtil.getEmail() called");

        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            String jwtToken = jwtAuthentication.getToken().getTokenValue();

            String email = jwtAuthentication.getToken().getClaimAsString("sub");
            // Instant expirationDate = jwtAuthentication.getToken().getExpiresAt();

            if (email == null) {
                throw new RuntimeException("Could not find user email");
            }

            // For now, let's just return the JWT token for demonstration purposes.
            logger.info("Found user email: " + email);
            return email;
        }

        throw new RuntimeException("Could not find user email");
    }
}
