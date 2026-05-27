package com.strivolabs.strivolabsassessmentjava.common.abstractions;

import java.time.OffsetDateTime;

public interface Creatable {

    OffsetDateTime getCreatedAt();

    String getCreatedBy();
}