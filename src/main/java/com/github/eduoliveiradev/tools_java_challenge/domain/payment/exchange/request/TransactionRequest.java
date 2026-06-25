package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
        @NotBlank(message = "O campo cartao é obrigatório")
        String cartao,
        @Valid
        @NotNull(message = "O campo descricao é obrigatório")
        DescriptionRequest descricao,
        @Valid
        @NotNull(message = "O campo formaPagamento é obrigatório")
        PaymentMethodRequest formaPagamento
) {
}
