package com.strivolabs.strivolabsassessmentjava.users.entities;

import java.time.OffsetDateTime;

import com.github.f4b6a3.uuid.UuidCreator;
import com.strivolabs.strivolabsassessmentjava.auth.domainevents.EmailConfirmedDomainEvent;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.EntityBase;
import com.strivolabs.strivolabsassessmentjava.users.domainevents.UserCreatedDomainEvent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "passwordHash")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class User extends EntityBase {

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 256, unique = true)
    private String email;

    @Column(name = "email_confirmed", nullable = false)
    private boolean emailConfirmed = false;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "access_failed_count", nullable = false)
    private int accessFailedCount = 0;

    @Column(name = "lockout_count", nullable = false)
    private int lockoutCount = 0;

    @Column(name = "lockout_end")
    private OffsetDateTime lockoutEnd;

    public static User create(
            String firstName,
            String lastName,
            String email,
            String passwordHash,
            String createdBy) {
        User user = new User();

        user.firstName = firstName.trim();
        user.lastName = lastName.trim();
        user.email = email.trim().toLowerCase();
        user.passwordHash = passwordHash;

        user.initializeAudit(createdBy);

        user.raise(new UserCreatedDomainEvent(
                UuidCreator.getTimeOrderedEpoch(),
                OffsetDateTime.now(),
                user.getFirstName(),
                user.getEmail()));

        return user;
    }

    public void confirmEmail(String firstName, String email, String updatedBy) {
        this.emailConfirmed = true;

        updateAudit(updatedBy);

        this.raise(new EmailConfirmedDomainEvent(
                UuidCreator.getTimeOrderedEpoch(),
                OffsetDateTime.now(),
                firstName,
                email));
    }

    public void changePassword(String newPasswordHash, String updatedBy) {
        this.passwordHash = newPasswordHash;
        updateAudit(updatedBy);
    }

    public void recordFailedAccess(String updatedBy) {
        this.accessFailedCount++;

        updateAudit(updatedBy);
    }

    public void lockUntil(OffsetDateTime until, String updatedBy) {
        this.lockoutCount++;
        this.lockoutEnd = until;
        updateAudit(updatedBy);
    }

    public void unlock(String updatedBy) {
        this.lockoutEnd = null;
        this.accessFailedCount = 0;

        updateAudit(updatedBy);
    }

    public boolean isLocked() {
        return lockoutEnd != null &&
                lockoutEnd.isAfter(OffsetDateTime.now());
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }
}