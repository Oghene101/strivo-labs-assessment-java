package com.strivolabs.strivolabsassessmentjava.auth.confirmemail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ConfirmEmailRequest")
public final record Request(

        @NotBlank(message = "Email is required") //
        String email,

        @NotBlank(message = "Token is required") //
        String token) {

}
