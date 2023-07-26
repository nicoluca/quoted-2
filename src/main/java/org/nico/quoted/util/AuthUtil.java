package org.nico.quoted.util;

import org.nico.quoted.exception.AuthenticationException;
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
            String email = jwtAuthentication.getToken().getClaimAsString("sub");

            if (email == null)
                throw new AuthenticationException("Unknown user email.");

            logger.info("Found user email: " + email);
            return email;
        }

        throw new AuthenticationException("Unknown authentication type.");
    }
}
