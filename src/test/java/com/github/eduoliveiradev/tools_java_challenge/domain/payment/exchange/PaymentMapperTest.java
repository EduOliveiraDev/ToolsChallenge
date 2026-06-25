package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentStatus;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentType;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentMapperTest {

    private final PaymentMapper paymentMapper = new PaymentMapper();

    @Test
    @DisplayName("Deve converter Payment em PaymentResponse corretamente")
    void deveConverterPaymentEmPaymentResponseCorretamente() {
        LocalDateTime dataHora = LocalDateTime.of(2026, 6, 25, 10, 30);
        Payment payment = new Payment(
                "1234567890123456",
                10L,
                "99.90",
                dataHora,
                "Loja Teste",
                "1234567890",
                "147258369",
                PaymentStatus.AUTORIZADO,
                PaymentType.PARCELADO_EMISSOR,
                6
        );

        PaymentResponse response = paymentMapper.toResponse(payment);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("1234567890123456", response.transacao().cartao()),
                () -> assertEquals(10L, response.transacao().id()),
                () -> assertEquals("99.90", response.transacao().descricao().valor()),
                () -> assertEquals(dataHora, response.transacao().descricao().dataHora()),
                () -> assertEquals("Loja Teste", response.transacao().descricao().estabelecimento()),
                () -> assertEquals("1234567890", response.transacao().descricao().nsu()),
                () -> assertEquals("147258369", response.transacao().descricao().codigoAutorizacao()),
                () -> assertEquals(PaymentStatus.AUTORIZADO, response.transacao().descricao().status()),
                () -> assertEquals("PARCELADO EMISSOR", response.transacao().formaPagamento().tipo()),
                () -> assertEquals(6, response.transacao().formaPagamento().parcelas())
        );
    }
}

