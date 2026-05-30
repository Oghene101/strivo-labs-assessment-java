package com.strivolabs.strivolabsassessmentjava.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.hashing")
public record HashingProperties(List<KeyConfig> keys) {

    public record KeyConfig(
            String keyId,
            String secret,
            boolean isActive) {
    }
}