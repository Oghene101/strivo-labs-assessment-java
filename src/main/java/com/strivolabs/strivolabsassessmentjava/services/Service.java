package com.strivolabs.strivolabsassessmentjava.services;

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
@Table(name = "services")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Service extends EntityBase {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public static Service create(
            String name,
            String description,
            String createdBy) {
        Service service = new Service();

        service.name = name;
        service.description = description;
        service.isActive = true;

        service.initializeAudit(createdBy);

        return service;
    }
}
