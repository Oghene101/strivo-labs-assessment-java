package com.strivolabs.strivolabsassessmentjava.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;

@Component
public class AuthService {
    public String getSignedInUserId() throws ResponseStatusException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Authentication context is not present.");
        }
        if (authentication.getDetails() instanceof Claims claims) {
            return claims.get("sub", String.class);
        }

        throw new IllegalStateException("Authenticated user does not have JWT claims in the security context.");
    }
}