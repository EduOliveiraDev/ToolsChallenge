package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange;

import java.time.LocalDateTime;

public record Payment(
        String cartao,
        Long id,
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
        return id;
    }
}
