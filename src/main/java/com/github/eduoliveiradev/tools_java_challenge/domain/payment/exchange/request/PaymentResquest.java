package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PaymentResquest(
        @Valid
        @NotNull(message = "O campo transacao é obrigatório")
        TransactionRequest transacao
) {
}
