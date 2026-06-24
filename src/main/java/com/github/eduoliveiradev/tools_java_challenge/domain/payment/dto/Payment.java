package com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto;

import java.time.LocalDateTime;

public record Payment(
        Long paymentId,
        String cartao,
        String id,
        String valor,
        LocalDateTime dataHora,
        String estabelecimento,
        String nsu,
        String codigoAutorizacao,
        String status,
        String tipo,
        Integer parcelas
) {
    public Long getId() {
        return paymentId;
    }
}
