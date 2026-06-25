package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request;

import jakarta.validation.constraints.Positive;

public record PaymentMethodRequest(
        String tipo,
        @Positive
        Integer parcelas
) {
}