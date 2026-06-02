package com.strivolabs.strivolabsassessmentjava.common.dtos;

import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String passwordHash) {

}
