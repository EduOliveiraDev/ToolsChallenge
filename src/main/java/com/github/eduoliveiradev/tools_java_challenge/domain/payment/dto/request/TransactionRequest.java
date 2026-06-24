package com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TransactionRequest(
        @NotBlank
        String cartao,
        @NotBlank
        String id,
        @NotBlank
        DescriptionRequest descricao,
        @NotBlank
        PaymentMethodRequest formaPagamento
) {
}
