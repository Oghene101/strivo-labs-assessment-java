package com.strivolabs.strivolabsassessmentjava.roles.entities;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.EntityBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Role extends EntityBase {

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    public static Role create(
            String name,
            String createdBy) {
        Role role = new Role();

        role.name = name;

        role.initializeAudit(createdBy);

        return role;
    }
}
