package com.strivolabs.strivolabsassessmentjava.common.abstractions;

import java.time.OffsetDateTime;

public interface Updatable {

    OffsetDateTime getLastUpdatedAt();

    String getLastUpdatedBy();
}