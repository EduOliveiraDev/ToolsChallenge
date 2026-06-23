package com.github.eduoliveiradev.tools_java_challenge.dto.response;

public record PaymentMethodResponse(
        String tipo,
        Integer parcelas
) {
}
