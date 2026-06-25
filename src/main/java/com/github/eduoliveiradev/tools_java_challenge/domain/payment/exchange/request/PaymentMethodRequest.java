package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentMethodRequest(
        @NotBlank(message = "O campo tipo é obrigatório")
        String tipo,
        @Positive
        Integer parcelas
) {
}