package com.strivolabs.strivolabsassessmentjava.common.abstractions;

public interface QueryHandler<TQuery, TResult> {
    TResult handle(TQuery query);
}