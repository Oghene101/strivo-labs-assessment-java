package com.strivolabs.strivolabsassessmentjava.auth.signout;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController("signOutEndpoint")
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/sign-out")
@Tag(name = "auth")
public final class Endpoint {

        private final SignOut.Handler handler;

        @PostMapping
        @Operation(summary = "Sign out user", description = "Revokes user sessions and refresh tokens")
        @ApiResponse( //
                        responseCode = "204", //
                        description = "Signed out successfully", //
                        content = @Content) //
        @ApiResponse( //
                        responseCode = "401", //
                        description = "Unauthorized", //
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
        public ResponseEntity<Void> signOut() {
                handler.handle(new SignOut.CommandImpl());

                return ResponseEntity.noContent().build();
        }
}
