package com.strivolabs.strivolabsassessmentjava.auth.forgotpassword;

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

@RestController("forgotPasswordEndpoint")
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/forgot-password")
@Tag(name = "auth")
public final class Endpoint {

    private final ForgotPassword.Handler handler;

    @PostMapping
    @Operation(summary = "Initiate password reset", description = "Accepts a user's email address and triggers a password reset sequence if the account exists.")
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
    public ResponseEntity<StandardResponse<Void>> forgotPassword(
            @Valid @RequestBody Request request) {
        ForgotPassword.CommandImpl command = Mapper.toCommand(request);
        Void result = handler.handle(command);

        StandardResponse<Void> response = StandardResponse.success(result,
                "If the email address matches an active account, password reset instructions will been sent.", HttpStatus.OK);

        return ResponseEntity.ok(response);
    }
}
