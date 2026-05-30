package com.strivolabs.strivolabsassessmentjava.users.domainevents;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEventHandler;
import com.strivolabs.strivolabsassessmentjava.common.dtos.UserDto;
import com.strivolabs.strivolabsassessmentjava.emails.EmailTemplate;
import com.strivolabs.strivolabsassessmentjava.security.JwtService;
import com.strivolabs.strivolabsassessmentjava.security.enums.TokenPurpose;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedDomainEventHandler implements DomainEventHandler<UserCreatedDomainEvent> {

    private final UserRepository users;
    private final JwtService jwt;
    private final JavaMailSender mail;

    @Value("${security.email-confirmation-token-expiration-ms}")
    private Long emailConfirmationTokenExpirationInMs;
    @Value("${confirm-email-endpoint}")
    private String confirmEmailEndpoint;

    @Override
    @Async
    public void handle(UserCreatedDomainEvent event) {
        try {

            UserDto user = users.findDtoByEmail(event.email()).orElseThrow();

            sendConfirmationEmail(event, user);
        } catch (NoSuchElementException ex) {
            log.error("User with email: {} not found", event.email(), ex);
        } catch (MessagingException ex) {
            log.error("Failed to send welcome email to {}", event.email(), ex);
        }
    }

    private void sendConfirmationEmail(UserCreatedDomainEvent event, UserDto user) throws MessagingException {
        Date now = new Date();
        String token = jwt.generateOnetimeToken(
                user,
                TokenPurpose.EMAIL_CONFIRMATION,
                now,
                new Date(now.getTime() + emailConfirmationTokenExpirationInMs));

        String encodedEmail = URLEncoder.encode(event.email(), StandardCharsets.UTF_8);
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        String confirmationLink = String.format(
                confirmEmailEndpoint + "email=%s&token=%s",
                encodedEmail,
                encodedToken);

        MimeMessage mimeMessage = mail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(event.email());
        helper.setSubject("Verify Your Strivo Labs Account");
        helper.setFrom("noreply@strivolabs.com");
        helper.setText(EmailTemplate.getConfirmationEmail(event.firstName(), confirmationLink), true);

        mail.send(mimeMessage);
    }

    @Override
    public Class<UserCreatedDomainEvent> getEventType() {
        return UserCreatedDomainEvent.class;
    }
}