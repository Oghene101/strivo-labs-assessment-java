package com.strivolabs.strivolabsassessmentjava.auth.confirmemail;

public class Mapper {
    public static ConfirmEmail.CommandImpl toCommand(Request request) {
        return new ConfirmEmail.CommandImpl(
                request.email(),
                request.token());
    }

}
