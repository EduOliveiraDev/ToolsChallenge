package com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.response;

public record TransactionResponse(
        String cartao,
        String id,
        DescriptionResponse descricao,
        PaymentMethodResponse formaPagamento
) {
}
