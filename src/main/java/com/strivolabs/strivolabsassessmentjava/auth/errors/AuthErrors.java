package com.strivolabs.strivolabsassessmentjava.auth.errors;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;

public final class AuthErrors {

    private AuthErrors() {

    }

    public static final ApiError BAD_REQUEST = new ApiError("Auth.BadRequest", "Invalid request");

    public static final ApiError PASSWORD_REUSE = new ApiError("Auth.PasswordReuse",
            "Cannot reuse a previously used password");

    public static final ApiError INVALID_TOKEN_OR_EMAIL = new ApiError("Auth.InvalidTokenOrEmail",
            "Invalid token or email provided");

    public static final ApiError EMAIL_ALREADY_CONFIRMED = new ApiError("Auth.EmailAlreadyConfirmed",
            "Your email has already been confirmed");

    public static final ApiError INVALID_TOKEN = new ApiError("Auth.InvalidToken",
            "The provided token is invalid or expired");

    public static final ApiError INVALID_TOKEN_OR_PASSWORD = new ApiError("Auth.InvalidTokenOrPassword",
            "Invalid token or password provided");

    public static final ApiError INVALID_EMAIL_OR_PASSWORD = new ApiError("Auth.InvalidEmailOrPassword",
            "Invalid email or password provided");

    public static final ApiError accountLocked(String date) {
        return new ApiError("Auth.AccountLocked", "Account locked until " + date);
    }
}
