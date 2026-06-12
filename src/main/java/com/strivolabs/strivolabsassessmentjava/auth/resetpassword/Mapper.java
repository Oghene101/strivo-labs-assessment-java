package com.strivolabs.strivolabsassessmentjava.auth.resetpassword;

public final class Mapper {

    public static ResetPassword.CommandImpl toCommand(Request request) {
        return new ResetPassword.CommandImpl(
                request.token(),
                request.newPassword());
    }
}
