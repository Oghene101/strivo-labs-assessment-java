package com.strivolabs.strivolabsassessmentjava.users.errors;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;

public final class UserErrors {
    private UserErrors() {
    }

    public static final ApiError CONFLICT = new ApiError("User.Conflict", "email already exists");
    
    public static final ApiError USER_ROLE_NOT_FOUND = new ApiError("User.UserRoleNotFound", "user role not found");

}
