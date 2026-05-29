package com.strivolabs.strivolabsassessmentjava.users.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "user_roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserRole implements Persistable<UserRoleId> {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserRoleId id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 150)
    private String createdBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "deleted_by", length = 150)
    private String deletedBy;

    @Transient
    protected boolean isNewEntity = true;

    public static UserRole create(
            UUID userId,
            UUID roleId,
            String createdBy) {
        UserRole userRoles = new UserRole();

        userRoles.id = UserRoleId.create(userId, roleId);
        userRoles.createdAt = OffsetDateTime.now();
        userRoles.createdBy = createdBy;

        return userRoles;
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    @PostLoad
    @PostPersist
    protected void markNotNew() {
        this.isNewEntity = false;
    }

}
