package com.strivolabs.strivolabsassessmentjava.auth.changepassword;

public final class Mapper {

    public static ChangePassword.CommandImpl toCommand(Request request) {
        return new ChangePassword.CommandImpl(
                request.oldPassword(),
                request.newPassword());
    }
}
