package com.strivolabs.strivolabsassessmentjava.auth.refreshtoken;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "RefreshTokenRequest")
public final record Request(
        @NotBlank(message = "Access Token is required") //
        String accessToken,
        
        @NotBlank(message = "Refresh Token is required") //
        String refreshToken) {

}
