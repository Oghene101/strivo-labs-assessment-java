package com.strivolabs.strivolabsassessmentjava.auth.signin;

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

@RestController("signInEndpoint")
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Tag(name = "auth")
public final class Endpoint {

        private final SignIn.Handler handler;

        @PostMapping
        @Operation(summary = "Authenticate user", description = "Authenticates a user with their credentials and returns access and refresh tokens.")
        @ApiResponse( //
                        responseCode = "200", //
                        description = "User authenticated successfully", //
                        content = @Content( //
                                        mediaType = "application/json", //
                                        schema = @Schema( //
                                                        implementation = StandardResponse.class, //
                                                        oneOf = { Response.class }))) //
        @ApiResponse( //
                        responseCode = "400", //
                        description = "Invalid request payload", //
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
        public ResponseEntity<StandardResponse<Response>> signIn(
                        @Valid @RequestBody Request request) {
                SignIn.CommandImpl command = Mapper.toCommand(request);
                Response result = handler.handle(command);

                StandardResponse<Response> response = StandardResponse.success(result,
                                "User authenticated successfully", HttpStatus.OK);

                return ResponseEntity.ok(response);
        }
}
