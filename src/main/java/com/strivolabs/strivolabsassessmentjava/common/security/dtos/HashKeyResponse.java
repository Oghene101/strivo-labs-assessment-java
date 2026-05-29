package com.strivolabs.strivolabsassessmentjava.common.security.dtos;

public record HashKeyResponse(String keyId, byte[] secret, Boolean isActive) {
}