package com.strivolabs.strivolabsassessmentjava.auth.signin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "SignInRequest")
public final record Request(

                @NotBlank(message = "Email is required") //
                @Email(message = "Email must be a valid email address") //
                String email,

                @NotBlank(message = "Password is required") //
                @Size(min = 8, message = "Password must be at least 8 characters long") //
                @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter") //
                @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter") //
                @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit") //
                @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "Password must contain at least one special character") //
                String password) {
}
