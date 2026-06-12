package com.strivolabs.strivolabsassessmentjava.auth.forgotpassword;

import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

public final class ForgotPassword {

        public static record CommandImpl(
                        String email) implements Command<Void> {

        }

        @Component
        @RequiredArgsConstructor
        public static class Handler implements CommandHandler<CommandImpl, Void> {

                private final UserRepository users;

                @Override
                @Transactional
                public Void handle(CommandImpl command) {

                        users.findByEmail(command.email())
                                        .ifPresent(user -> {
                                                user.forgotPassword();
                                                users.save(user);
                                        });

                        return null;
                }
        }
}
