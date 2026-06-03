package com.strivolabs.strivolabsassessmentjava.auth.refreshtoken;

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

@RestController("refreshTokenEndpoint")
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/refresh-token")
@Tag(name = "auth")
public final class Endpoint {

    private final RefreshToken.Handler handler;

    @PostMapping
    @Operation(summary = "Refresh active session tokens", description = "Accepts an expired access token and a valid refresh token to issue a new access token and refresh token pair.")
    @ApiResponse( //
            responseCode = "200", //
            description = "Tokens rotated successfully", //
            content = @Content( //
                    mediaType = "application/json", //
                    schema = @Schema( //
                            implementation = StandardResponse.class, //
                            oneOf = { Response.class }))) //
    @ApiResponse( //
            responseCode = "400", //
            description = "Invalid request payload", //
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse( //
            responseCode = "401", //
            description = "Refresh token is expired, revoked, or structurally invalid", //
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<StandardResponse<Response>> refreshToken(
            @Valid @RequestBody Request request) {
        RefreshToken.CommandImpl command = Mapper.toCommand(request);
        Response result = handler.handle(command);

        StandardResponse<Response> response = StandardResponse.success(result,
                "Tokens refreshed successfully", HttpStatus.OK);

        return ResponseEntity.ok(response);
    }
}
