package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response;

import java.time.LocalDateTime;

public record DescriptionResponse(
        String valor,
        LocalDateTime dataHora,
        String estabelecimento,
        String nsu,
        String codigoAutorizacao,
        String status
) {
}
