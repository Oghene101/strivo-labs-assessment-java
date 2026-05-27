package com.strivolabs.strivolabsassessmentjava.users.entities;

import java.io.Serializable;
import java.util.UUID;

import lombok.AccessLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class UserRoleId implements Serializable {

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "role_id", nullable = false, updatable = false)
    private UUID roleId;

    public static UserRoleId create(
            UUID userId,
            UUID roleId) {
        UserRoleId id = new UserRoleId();
        id.userId = userId;
        id.roleId = roleId;
        return id;
    }
}