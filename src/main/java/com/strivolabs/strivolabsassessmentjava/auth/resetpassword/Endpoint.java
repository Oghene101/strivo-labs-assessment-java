package com.strivolabs.strivolabsassessmentjava.auth.resetpassword;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strivolabs.strivolabsassessmentjava.common.dtos.StandardResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController("resetPasswordEndpoint")
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/reset-password")
@Tag(name = "auth")
public final class Endpoint {

    private final ResetPassword.Handler handler;

    @PostMapping
    @Operation(summary = "Complete password reset", description = "Consumes the verification token and applies the new password securely.")
    @ApiResponse( //
            responseCode = "200", //
            description = "Password reset initiated successfully", //
            content = @Content( //
                    mediaType = "application/json", //
                    schema = @Schema( //
                            implementation = StandardResponse.class, //
                            oneOf = { Void.class }))) //
    @ApiResponse( //
            responseCode = "400", //
            description = "Invalid request payload", //
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<StandardResponse<Void>> resetPassword(
            @Valid @RequestBody Request request) {
        ResetPassword.CommandImpl command = Mapper.toCommand(request);
        Void result = handler.handle(command);

        StandardResponse<Void> response = StandardResponse.success(result,
                "Your password has been successfully reset.", HttpStatus.OK);

        return ResponseEntity.ok(response);
    }
}
