package com.strivolabs.strivolabsassessmentjava.auth.confirmemail;

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

@RestController("confirmEmailEndpoint")
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/confirm-email")
@Tag(name = "auth")
public class Endpoint {

        private final ConfirmEmail.Handler confirmEmailHandler;

        @PostMapping
        @Operation(summary = "Confirm user email", description = "Confirms a user's email using the confirmation token.")
        @ApiResponse( //
                        responseCode = "200", //
                        description = "Email confirmed successfully", //
                        content = @Content( //
                                        mediaType = "application/json", //
                                        schema = @Schema( //
                                                        implementation = StandardResponse.class, //
                                                        oneOf = { Void.class }))) //
        @ApiResponse( //
                        responseCode = "400", //
                        description = "Invalid request payload", //
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
        public ResponseEntity<StandardResponse<Void>> confirmEmail(
                        @Valid @RequestBody Request request) {
                ConfirmEmail.CommandImpl command = Mapper.toCommand(request);
                confirmEmailHandler.handle(command);

                StandardResponse<Void> response = StandardResponse.success("Email confirmed successfully",
                                HttpStatus.OK);

                return ResponseEntity.ok(response);
        }
}
