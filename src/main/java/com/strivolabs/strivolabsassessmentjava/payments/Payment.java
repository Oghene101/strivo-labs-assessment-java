package com.strivolabs.strivolabsassessmentjava.payments;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;
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
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Payment extends EntityBase {

    @Column(name = "reference", nullable = false, updatable = false)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "payable_type", nullable = false, updatable = false)
    private PayableType payableType;

    @Column(name = "payable_id", nullable = false, updatable = false)
    private UUID payableId;

    @Column(name = "amount", nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, updatable = false)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gateway_response")
    private JsonNode gatewayResponse;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    @Override
    public boolean isNew() {
        return isNewEntity;
    }
}
