package com.strivolabs.strivolabsassessmentjava.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.strivolabs.strivolabsassessmentjava.security.dtos.HashKeyResponse;
import com.strivolabs.strivolabsassessmentjava.security.dtos.HashResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashingService {

    private final HashingKeyStore keyStore;

    public HashResponse compute(String input) {
        String normalized = normalize(input);
        HashKeyResponse key = keyStore.getActiveKey();
        String hash = computeInternal(normalized, key.secret());

        return new HashResponse(hash, key.keyId());
    }

    public HashResponse compute(String input, String keyId) {
        String normalized = normalize(input);
        HashKeyResponse key = keyStore.getByKeyId(keyId);
        String hash = computeInternal(normalized, key.secret());

        return new HashResponse(hash, key.keyId());
    }

    public boolean verify(String input, String storedHash, String keyId) {
        String normalized = normalize(input);
        HashKeyResponse key = keyStore.getByKeyId(keyId);
        String computed = computeInternal(normalized, key.secret());

        byte[] storedBytes = Base64.getDecoder().decode(storedHash);
        byte[] computedBytes = Base64.getDecoder().decode(computed);

        return MessageDigest.isEqual(storedBytes, computedBytes);
    }

    private static String computeInternal(String input, byte[] secret) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hashBytes = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to calculate HMAC-SHA256 execution path", e);
        }
    }

    private static String normalize(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null or empty.");
        }
        return input.trim();
    }
}