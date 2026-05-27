package com.strivolabs.strivolabsassessmentjava.services;

import java.math.BigDecimal;
import java.util.UUID;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.EntityBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "service_prices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ServicePrice extends EntityBase {

    @Column(name = "service_id", nullable = false, updatable = false)
    private UUID serviceId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_period", nullable = false)
    private BillingPeriod billingPeriod;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public static ServicePrice create(
            UUID serviceId,
            BigDecimal amount,
            String currencyCode,
            BillingPeriod billingPeriod,
            String createdBy) {
        ServicePrice servicePrice = new ServicePrice();

        servicePrice.serviceId = serviceId;
        servicePrice.amount = amount;
        servicePrice.currencyCode = currencyCode;
        servicePrice.billingPeriod = billingPeriod;
        servicePrice.isActive = true;

        servicePrice.initializeAudit(createdBy);

        return servicePrice;
    }
}