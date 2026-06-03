package com.strivolabs.strivolabsassessmentjava.auth.refreshtoken;

public final class Mapper {

    public static RefreshToken.CommandImpl toCommand(Request request) {
        return new RefreshToken.CommandImpl(
                request.accessToken(),
                request.refreshToken());
    }
}
