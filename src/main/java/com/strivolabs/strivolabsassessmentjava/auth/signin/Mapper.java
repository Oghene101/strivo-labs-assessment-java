package com.strivolabs.strivolabsassessmentjava.auth.signin;

public final class Mapper {

    public static SignIn.CommandImpl toCommand(Request request) {
        return new SignIn.CommandImpl(
                request.email(),
                request.password());
    }
}
