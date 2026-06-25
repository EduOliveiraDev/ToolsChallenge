package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response;

public record PaymentMethodResponse(
        String tipo,
        Integer parcelas
) {
}
