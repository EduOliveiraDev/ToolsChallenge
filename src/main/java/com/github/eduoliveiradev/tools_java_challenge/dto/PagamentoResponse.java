package com.github.eduoliveiradev.tools_java_challenge.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoResponse(
    String cartao,
    UUID id,
    BigDecimal valor,
    LocalDateTime dataHora,
    String estabelecimento,
    String nsu,
    String codigoAutorizacao,
    String status,
    String tipo,
    Integer parcelas) {
}
