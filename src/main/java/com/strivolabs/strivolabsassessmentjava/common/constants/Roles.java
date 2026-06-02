package com.strivolabs.strivolabsassessmentjava.common.constants;

public final class Roles {

    private Roles() {
    }

    public static final String PREFIX = "ROLE_";

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    // Spring Security prefixes for role-based authorization rules
    public static final String ROLE_USER = PREFIX + USER;
    public static final String ROLE_ADMIN = PREFIX + ADMIN;
}
