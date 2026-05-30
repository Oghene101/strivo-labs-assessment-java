package com.strivolabs.strivolabsassessmentjava.auth.domainevents;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEventHandler;
import com.strivolabs.strivolabsassessmentjava.emails.EmailTemplate;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConfirmedDomainEventEventHandler implements DomainEventHandler<EmailConfirmedDomainEvent> {

    private final JavaMailSender mail;

    @Override
    @Async
    public void handle(EmailConfirmedDomainEvent event) {
        try {
            MimeMessage mimeMessage = mail.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(event.email());
            helper.setSubject("Welcome to Strivo Labs");
            helper.setFrom("noreply@strivolabs.com");
            helper.setText(EmailTemplate.getWelcomeEmail(event.firstName()), true);

            mail.send(mimeMessage);
        } catch (MessagingException ex) {
            log.error("Failed to send welcome email to {}", event.email(), ex);
        }
    }

    @Override
    public Class<EmailConfirmedDomainEvent> getEventType() {
        return EmailConfirmedDomainEvent.class;
    }
}