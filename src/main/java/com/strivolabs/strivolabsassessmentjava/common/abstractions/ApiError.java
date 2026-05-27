package com.strivolabs.strivolabsassessmentjava.common.abstractions;

public record ApiError(String code, String description) {

    public static final ApiError NONE = null;
}