package com.strivolabs.strivolabsassessmentjava.security.dtos;

public record TokenResponse(
                String accessToken,
                long expirationInMs,
                String refreshToken) {

}
