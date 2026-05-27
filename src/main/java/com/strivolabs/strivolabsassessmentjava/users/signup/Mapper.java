package com.strivolabs.strivolabsassessmentjava.users.signup;

public class Mapper {

    public static SignUp.CommandImpl toCommand(Request request) {
        return new SignUp.CommandImpl(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password());
    }
}
