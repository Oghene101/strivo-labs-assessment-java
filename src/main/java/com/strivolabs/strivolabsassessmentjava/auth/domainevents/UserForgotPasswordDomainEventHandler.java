package com.strivolabs.strivolabsassessmentjava.auth.domainevents;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
public class UserForgotPasswordDomainEventHandler implements DomainEventHandler<UserForgotPasswordDomainEvent> {

    private final UserRepository users;
    private final JwtService jwt;
    private final JavaMailSender mail;

    @Value("${security.reset-password-token-expiration-ms}")
    private Long resetPasswordTokenExpirationInMs;
    @Value("${reset-password-endpoint}")
    private String resetPasswordEndpoint;

    @Override
    public void handle(UserForgotPasswordDomainEvent event) {
        try {

            UserDto user = users.findDtoByEmail(event.email()).orElseThrow();

            sendResetPasswordEmail(user);
        } catch (NoSuchElementException ex) {
            log.error("User with email: {} not found", event.email(), ex);
            throw ex;
        } catch (MessagingException ex) {
            log.error("Failed to send reset password email to {}", event.email(), ex);
            throw new RuntimeException("Email delivery failed, rolling back outbox state", ex);
        }
    }

    private void sendResetPasswordEmail(UserDto user) throws MessagingException {
        Date now = new Date();
        String token = jwt.generateOnetimeToken(
                user,
                TokenPurpose.PASSWORD_RESET,
                now,
                new Date(now.getTime() + resetPasswordTokenExpirationInMs));

        String encodedEmail = URLEncoder.encode(user.email(), StandardCharsets.UTF_8);
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        String resetPasswordLink = String.format(
                resetPasswordEndpoint + "email=%s&token=%s",
                encodedEmail,
                encodedToken);

        MimeMessage mimeMessage = mail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(user.email());
        helper.setSubject("Reset Your Strivo Labs Password");
        helper.setFrom("noreply@strivolabs.com");
        helper.setText(EmailTemplate.getResetPasswordEmail(user.firstName(), resetPasswordLink), true);

        mail.send(mimeMessage);
    }

    @Override
    public Class<UserForgotPasswordDomainEvent> getEventType() {
        return UserForgotPasswordDomainEvent.class;
    }
}