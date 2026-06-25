package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentStatus;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentType;

import java.time.LocalDateTime;

public record Payment(
        String cartao,
        Long id,
        String valor,
        LocalDateTime dataHora,
        String estabelecimento,
        String nsu,
        String codigoAutorizacao,
        PaymentStatus status,
        PaymentType tipo,
        Integer parcelas
) {
    public Long getId() {
        return id;
    }

    public Payment changeStatus(PaymentStatus status) {
        return new Payment(
                cartao,
                id,
                valor,
                dataHora,
                estabelecimento,
                nsu,
                codigoAutorizacao,
                status,
                tipo,
                parcelas
        );
    }
}
