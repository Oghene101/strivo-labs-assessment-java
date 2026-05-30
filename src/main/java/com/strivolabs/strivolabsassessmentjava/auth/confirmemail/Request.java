package com.strivolabs.strivolabsassessmentjava.auth.confirmemail;

import jakarta.validation.constraints.NotBlank;

public record Request(

        @NotBlank(message = "Email is required") //
        String email,

        @NotBlank(message = "Token is required") //
        String token) {

}
