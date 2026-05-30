package com.strivolabs.strivolabsassessmentjava.common.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateTimeUtils {
    private DateTimeUtils() {
    }

    private static final DateTimeFormatter HUMAN_READABLE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, hh:mm a",
            Locale.ENGLISH);

    public static String format(OffsetDateTime date) {
        if (date == null) {
            return "";
        }
        return date.format(HUMAN_READABLE_FORMATTER);
    }
}
