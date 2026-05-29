package com.strivolabs.strivolabsassessmentjava.common.security;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.common.security.dtos.HashKeyResponse;

@Component
public class HashingKeyStore {

    private final List<HashKeyResponse> keys;

    public HashingKeyStore(HashingProperties properties) {
        if (properties.keys() == null) {
            throw new InvalidOperationException("No HMAC keys configured.");
        }

        List<HashKeyResponse> parsedKeys = properties.keys().stream()
                .map(k -> new HashKeyResponse(
                        k.keyId(),
                        Base64.getDecoder().decode(k.secret()),
                        k.isActive()))
                .toList();

        this.keys = List.copyOf(parsedKeys);
        validate();
    }

    public HashKeyResponse getActiveKey() {
        return keys.stream()
                .filter(HashKeyResponse::isActive)
                .reduce((a, b) -> {
                    throw new InvalidOperationException("Exactly one HMAC key must be active.");
                })
                .orElseThrow(() -> new InvalidOperationException("No active HMAC key found."));
    }

    public HashKeyResponse getByKeyId(String keyId) {
        return keys.stream()
                .filter(k -> k.keyId().equals(keyId))
                .findFirst()
                .orElseThrow(() -> new InvalidOperationException("HMAC KeyId not found: " + keyId));
    }

    private void validate() {
        if (keys.isEmpty()) {
            throw new InvalidOperationException("No HMAC keys configured.");
        }

        long activeCount = keys.stream().filter(HashKeyResponse::isActive).count();
        if (activeCount != 1) {
            throw new InvalidOperationException("Exactly one HMAC key must be active. Found: " + activeCount);
        }

        // Check for duplicate key IDs
        boolean hasDuplicates = keys.stream()
                .collect(Collectors.groupingBy(HashKeyResponse::keyId, Collectors.counting()))
                .values().stream()
                .anyMatch(count -> count > 1);

        if (hasDuplicates) {
            throw new InvalidOperationException("Duplicate HMAC KeyId detected.");
        }
    }

    // Custom runtime exception helper matching C# behavior
    public static class InvalidOperationException extends RuntimeException {
        public InvalidOperationException(String message) {
            super(message);
        }
    }
}