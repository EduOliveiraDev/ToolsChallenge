package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response;

public record TransactionResponse(
        String cartao,
        Long id,
        DescriptionResponse descricao,
        PaymentMethodResponse formaPagamento
) {
}
