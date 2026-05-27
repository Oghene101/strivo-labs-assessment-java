package com.strivolabs.strivolabsassessmentjava.users.signup;

import java.net.URI;

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

@RestController("signUpEndpoint")
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Tag(name = "users")
public class Endpoint {

        private final SignUp.Handler signUpHandler;

        @PostMapping
        @Operation(summary = "Register a new user", description = "Creates a new user account")
        @ApiResponse( //
                        responseCode = "201", //
                        description = "User created successfully", //
                        content = @Content(mediaType = "application/json")) //
        @ApiResponse( //
                        responseCode = "400", //
                        description = "Invalid request payload", //
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
        @ApiResponse( //
                        responseCode = "409", //
                        description = "User already exists", //
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
        public ResponseEntity<StandardResponse<Response>> signUp(
                        @Valid @RequestBody Request request) {
                SignUp.CommandImpl command = Mapper.toCommand(request);
                Response result = signUpHandler.handle(command);

                StandardResponse<Response> response = StandardResponse.success(result,
                                "User created successfully",
                                HttpStatus.CREATED);

                return ResponseEntity.created(URI.create("/api/v1/users/" + result.userId())).body(response);
        }
}
