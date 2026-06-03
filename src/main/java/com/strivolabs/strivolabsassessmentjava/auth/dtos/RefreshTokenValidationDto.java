package com.strivolabs.strivolabsassessmentjava.auth.dtos;

import java.util.UUID;

public record RefreshTokenValidationDto(UUID userId, UUID sessionId) {

}
