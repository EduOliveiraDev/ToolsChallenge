package com.github.eduoliveiradev.tools_java_challenge.dto.response;

public record TransacaoResponse(
        String cartao,
        String id,
        DescricaoResponse descricao,
        FormaPagamentoResponse formaPagamento
) {
}
