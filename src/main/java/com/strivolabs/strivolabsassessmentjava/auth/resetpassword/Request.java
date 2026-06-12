package com.strivolabs.strivolabsassessmentjava.auth.resetpassword;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "ResetPasswordRequest")
public final record Request(

                @NotBlank(message = "Token is required") //
                String token,

                @NotBlank(message = "New password is required") //
                @Size(min = 8, message = "New password must be at least 8 characters long") //
                @Pattern(regexp = ".*[A-Z].*", message = "New password must contain at least one uppercase letter") //
                @Pattern(regexp = ".*[a-z].*", message = "New password must contain at least one lowercase letter") //
                @Pattern(regexp = ".*[0-9].*", message = "New password must contain at least one digit") //
                @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "New password must contain at least one special character") //
                String newPassword) {
}
