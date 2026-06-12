package com.strivolabs.strivolabsassessmentjava.auth.forgotpassword;

public final class Mapper {

    public static ForgotPassword.CommandImpl toCommand(Request request) {
        return new ForgotPassword.CommandImpl(
                request.email());
    }
}