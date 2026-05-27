package com.strivolabs.strivolabsassessmentjava.common.abstractions;

public interface CommandHandler<TCommand extends Command<TResult>, TResult> {
    TResult handle(TCommand command);
}
