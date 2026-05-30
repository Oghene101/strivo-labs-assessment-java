package com.strivolabs.strivolabsassessmentjava.security.dtos;

public record HashKeyResponse(String keyId, byte[] secret, Boolean isActive) {
}