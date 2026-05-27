package com.strivolabs.strivolabsassessmentjava.common.abstractions;

import java.time.OffsetDateTime;

public interface Deletable {

    OffsetDateTime getDeletedAt();

    String getDeletedBy();

    void softDelete(String deletedBy);

    boolean isDeleted();

}