package com.strivolabs.strivolabsassessmentjava.subscriptions;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.EntityBase;
import com.strivolabs.strivolabsassessmentjava.services.BillingPeriod;

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
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Subscription extends EntityBase {

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "service_id", nullable = false, updatable = false)
    private UUID serviceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @Column(name = "subscribed_at", nullable = false, updatable = false)
    private OffsetDateTime subscribedAt;

    @Column(name = "unsubscribed_at")
    private OffsetDateTime unsubscribedAt;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "price_amount", nullable = false, updatable = false)
    private BigDecimal priceAmount;

    @Column(name = "currency_code", nullable = false, updatable = false)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_period", nullable = false, updatable = false)
    private BillingPeriod billingPeriod;

    public static Subscription create(
            UUID userId,
            UUID serviceId,
            OffsetDateTime expiresAt,
            BigDecimal priceAmount,
            String currecnyCode,
            BillingPeriod billingPeriod,
            String createdBy) {
        Subscription subscription = new Subscription();

        subscription.userId = userId;
        subscription.serviceId = serviceId;
        subscription.status = SubscriptionStatus.INITIATED;
        subscription.subscribedAt = OffsetDateTime.now();
        subscription.expiresAt = expiresAt;
        subscription.priceAmount = priceAmount;
        subscription.currencyCode = currecnyCode;
        subscription.billingPeriod = billingPeriod;

        subscription.initializeAudit(createdBy);

        return subscription;
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }
}