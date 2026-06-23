package com.github.eduoliveiradev.tools_java_challenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentMethodRequest(
        @NotBlank
        String tipo,
        @Positive
        Integer parcelas
) {
}