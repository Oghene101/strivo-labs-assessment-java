package com.strivolabs.strivolabsassessmentjava.users.domainevents;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEventHandler;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedDomainEventHandler implements DomainEventHandler<UserCreatedDomainEvent> {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void handle(UserCreatedDomainEvent event) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(event.email());
            helper.setSubject("Welcome to Strivo Labs");
            helper.setFrom("noreply@strivolabs.com");
            helper.setText("""
                        <html>
                            <body>
                                <h1>Welcome %s!</h1>
                                <p>Your account has been created successfully.</p>
                            </body>
                        </html>
                    """.formatted(event.firstName()), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            log.error("Failed to send welcome email to {}", event.email(), ex);
        }
    }

    @Override
    public Class<UserCreatedDomainEvent> getEventType() {
        return UserCreatedDomainEvent.class;
    }
}