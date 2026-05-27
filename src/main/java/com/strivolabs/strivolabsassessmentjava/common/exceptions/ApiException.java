package com.strivolabs.strivolabsassessmentjava.common.exceptions;

import org.springframework.http.HttpStatus;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final String title;
    private final ApiError error;
    private final HttpStatus statusCode;

    protected ApiException(
            String title,
            String message,
            ApiError error,
            HttpStatus statusCode) {
        super(message);
        this.title = title;
        this.error = error;
        this.statusCode = statusCode;
    }

    public static ApiException notFound(ApiError error) {
        return new ApiException("Not Found", "The requested resource was not found.", error, HttpStatus.NOT_FOUND);
    }

    public static ApiException notFound(ApiError error, String message) {
        return new ApiException("Not Found", message, error, HttpStatus.NOT_FOUND);
    }

    public static ApiException badRequest(ApiError error) {
        return new ApiException("Bad Request", "Invalid request parameters.", error, HttpStatus.BAD_REQUEST);
    }

    public static ApiException badRequest(ApiError error, String message) {
        return new ApiException("Bad Request", message, error, HttpStatus.BAD_REQUEST);
    }

    public static ApiException conflict(ApiError error) {
        return new ApiException("Conflict", "A conflict occurred with the current state of the resource.", error,
                HttpStatus.CONFLICT);
    }

    public static ApiException conflict(ApiError error, String message) {
        return new ApiException("Conflict", message, error, HttpStatus.CONFLICT);
    }

    public static ApiException unauthorized(ApiError error) {
        return new ApiException("Unauthorized", "Authentication required.", error, HttpStatus.UNAUTHORIZED);
    }

    public static ApiException unauthorized(ApiError error, String message) {
        return new ApiException("Unauthorized", message, error, HttpStatus.UNAUTHORIZED);
    }

    public static ApiException forbidden(ApiError error) {
        return new ApiException("Forbidden", "Insufficient permissions.", error, HttpStatus.FORBIDDEN);
    }

    public static ApiException forbidden(ApiError error, String message) {
        return new ApiException("Forbidden", message, error, HttpStatus.FORBIDDEN);
    }

}