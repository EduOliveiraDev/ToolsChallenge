package com.github.eduoliveiradev.tools_java_challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PagamentoResquest(
        @NotBlank
        String cartao,
        @Positive
        BigDecimal valor,
        @NotBlank
        String estabelecimento,
        String tipo,
        Integer parcelas
        ) {
}
