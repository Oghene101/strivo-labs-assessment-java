package com.strivolabs.strivolabsassessmentjava.common.security.dtos;

public record TokenResponse(
                String accessToken,
                long expirationInMs,
                String refreshToken) {

}
