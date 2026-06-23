package com.github.eduoliveiradev.tools_java_challenge.dto.response;

public record TransactionResponse(
        String cartao,
        String id,
        DescriptionResponse descricao,
        PaymentMethodResponse formaPagamento
) {
}
