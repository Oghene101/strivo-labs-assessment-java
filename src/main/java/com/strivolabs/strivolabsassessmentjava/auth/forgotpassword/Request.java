package com.strivolabs.strivolabsassessmentjava.auth.forgotpassword;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ForgotPasswordRequest")
public final record Request(

        @NotBlank(message = "Email is required") //
        @Email(message = "Email must be a valid email address") //
        String email)
{
}
