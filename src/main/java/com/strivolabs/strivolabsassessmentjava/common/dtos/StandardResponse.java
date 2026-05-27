package com.strivolabs.strivolabsassessmentjava.common.dtos;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardResponse<TData> {

    public final boolean isSuccess;
    private final HttpStatus statusCode;
    public final String message;
    public final TData data;
    public final ApiError error;

    private StandardResponse(
            boolean isSuccess,
            HttpStatus statusCode,
            String message,
            TData data,
            ApiError error) {
        if (isSuccess && error != null) {
            throw new IllegalStateException("Cannot be successful with error");
        }
        if (!isSuccess && error == null) {
            throw new IllegalStateException("Cannot be unsuccessful without error");
        }
        if (!isSuccess && data != null) {
            throw new IllegalStateException("Failed response must not have data");
        }

        this.isSuccess = isSuccess;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    // success without data
    public static StandardResponse<Void> success(
            String message,
            HttpStatus statusCode) {
        return new StandardResponse<>(true, statusCode, message, null, ApiError.NONE);
    }

    public static StandardResponse<Void> success() {
        return success("Completed successfully", HttpStatus.OK);
    }

    // success with data
    public static <TData> StandardResponse<TData> success(
            TData data,
            String message,
            HttpStatus statusCode) {
        return new StandardResponse<>(true, statusCode, message, data, ApiError.NONE);
    }

    public static <TData> StandardResponse<TData> success(TData data) {
        return success(data, "Completed successfully", HttpStatus.OK);
    }

    @JsonGetter("statusCode")
    public int getStatusCodeValue() {
        return this.statusCode.value();
    }
}