package com.github.eduoliveiradev.tools_java_challenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TransacaoRequest(
        @NotBlank
        String cartao,
        @NotBlank
        String id,
        @NotBlank
        DescricaoRequest descricao,
        @NotBlank
        FormaPagamentoRequest formaPagamento
) {
}
