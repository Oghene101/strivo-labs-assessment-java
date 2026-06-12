package com.strivolabs.strivolabsassessmentjava.auth.changepassword;

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

@RestController("changePasswordEndpoint")
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/change-password")
@Tag(name = "auth")
public final class Endpoint {

    private final ChangePassword.Handler handler;

    @PostMapping
    @Operation(summary = "Change authenticated user password", description = "Allows a signed-in user to change their account password by validating their current password and applying a new one.")
    @ApiResponse( //
            responseCode = "200", //
            description = "Password changed successfully", //
            content = @Content( //
                    mediaType = "application/json", //
                    schema = @Schema( //
                            implementation = StandardResponse.class, //
                            oneOf = { Void.class }))) //
    @ApiResponse( //
            responseCode = "400", //
            description = "Invalid request payload", //
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", //
            description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<StandardResponse<Void>> changePassword(
            @Valid @RequestBody Request request) {
        ChangePassword.CommandImpl command = Mapper.toCommand(request);
        Void result = handler.handle(command);

        StandardResponse<Void> response = StandardResponse.success(result,
                "Your password has been successfully updated.", HttpStatus.OK);

        return ResponseEntity.ok(response);
    }
}
