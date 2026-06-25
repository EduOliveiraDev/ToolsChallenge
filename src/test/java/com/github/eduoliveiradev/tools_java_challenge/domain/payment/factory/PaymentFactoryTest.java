package com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentStatus;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentType;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.Payment;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.DescriptionRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentMethodRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.TransactionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentFactoryTest {

    private final PaymentFactory paymentFactory = new PaymentFactory();
    private final LocalDateTime dataHora = LocalDateTime.of(2026, 6, 25, 10, 30);

    private PaymentResquest request(String tipo, Integer parcelas) {
        return new PaymentResquest(
                new TransactionRequest(
                        "1234567890123456",
                        new DescriptionRequest("99.90", dataHora, "Loja Teste"),
                        new PaymentMethodRequest(tipo, parcelas)
                )
        );
    }

    @Test
    @DisplayName("Deve criar pagamento à vista")
    void deveCriarPagamentoAVista() {
        Payment pagamento = paymentFactory.createPayment(request("AVISTA", 1));

        assertAll(
                () -> assertEquals("1234567890123456", pagamento.cartao()),
                () -> assertEquals("99.90", pagamento.valor()),
                () -> assertEquals(PaymentStatus.AUTORIZADO, pagamento.status()),
                () -> assertEquals(PaymentType.AVISTA, pagamento.tipo()),
                () -> assertEquals(1, pagamento.parcelas()),
                () -> assertEquals("1234567890", pagamento.nsu()),
                () -> assertEquals("147258369", pagamento.codigoAutorizacao())
        );
    }

    @Test
    @DisplayName("Deve criar pagamento parcelado pela loja")
    void deveCriarPagamentoParceladoLoja() {
        Payment pagamento = paymentFactory.createPayment(request("PARCELADO LOJA", 3));

        assertAll(
                () -> assertEquals(PaymentType.PARCELADO_LOJA, pagamento.tipo()),
                () -> assertEquals(3, pagamento.parcelas()),
                () -> assertEquals(PaymentStatus.AUTORIZADO, pagamento.status())
        );
    }

    @Test
    @DisplayName("Deve criar pagamento parcelado pelo emissor")
    void deveCriarPagamentoParceladoEmissor() {
        Payment pagamento = paymentFactory.createPayment(request("PARCELADO EMISSOR", 6));

        assertAll(
                () -> assertEquals(PaymentType.PARCELADO_EMISSOR, pagamento.tipo()),
                () -> assertEquals(6, pagamento.parcelas()),
                () -> assertEquals(PaymentStatus.AUTORIZADO, pagamento.status())
        );
    }

    @Test
    @DisplayName("Deve aceitar tipo de pagamento em caixa baixa")
    void deveAceitarTipoEmCaixaBaixa() {
        Payment pagamento = paymentFactory.createPayment(request("avista", 1));

        assertEquals(PaymentType.AVISTA, pagamento.tipo());
    }

    @Test
    @DisplayName("Deve lançar exceção quando request for nulo")
    void deveLancarExcecaoQuandoRequestForNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentFactory.createPayment(null)
        );

        assertEquals("Payment request não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo for inválido")
    void deveLancarExcecaoQuandoTipoForInvalido() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentFactory.createPayment(request("INVALIDO", 1))
        );

        assertEquals("Tipo de pagamento inválido", exception.getMessage());
    }
}

