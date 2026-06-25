package com.github.eduoliveiradev.tools_java_challenge.repository;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentStatus;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentType;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentRepositoryTest {

    private Payment payment(Long id) {
        return new Payment(
                "1234567890123456",
                id,
                "99.90",
                LocalDateTime.of(2026, 6, 25, 10, 30),
                "Loja Teste",
                "1234567890",
                "147258369",
                PaymentStatus.AUTORIZADO,
                PaymentType.AVISTA,
                1
        );
    }

    @Test
    @DisplayName("Deve salvar e buscar pagamento por id")
    void deveSalvarEBuscarPagamentoPorId() {
        PaymentRepository repository = new PaymentRepository();
        Payment payment = payment(1L);

        Payment salvo = repository.save(payment);

        assertAll(
                () -> assertEquals(payment, salvo),
                () -> assertTrue(repository.findById(1L).isPresent()),
                () -> assertEquals(payment, repository.findById(1L).orElseThrow())
        );
    }

    @Test
    @DisplayName("Deve retornar lista com todos os pagamentos")
    void deveRetornarListaComTodosOsPagamentos() {
        PaymentRepository repository = new PaymentRepository();
        Payment payment1 = payment(1L);
        Payment payment2 = payment(2L);

        repository.save(payment1);
        repository.save(payment2);

        List<Payment> pagamentos = repository.findAll();

        assertAll(
                () -> assertEquals(2, pagamentos.size()),
                () -> assertTrue(pagamentos.contains(payment1)),
                () -> assertTrue(pagamentos.contains(payment2))
        );
    }

    @Test
    @DisplayName("Deve retornar opcional vazio quando pagamento não existir")
    void deveRetornarOpcionalVazioQuandoPagamentoNaoExistir() {
        PaymentRepository repository = new PaymentRepository();

        assertFalse(repository.findById(999L).isPresent());
    }

    @Test
    @DisplayName("Deve substituir pagamento quando salvar mesmo id")
    void deveSubstituirPagamentoQuandoSalvarMesmoId() {
        PaymentRepository repository = new PaymentRepository();
        Payment paymentOriginal = payment(1L);
        Payment paymentAtualizado = new Payment(
                "9999999999999999",
                1L,
                "150.00",
                LocalDateTime.of(2026, 6, 25, 11, 0),
                "Outro Estabelecimento",
                "2222222222",
                "333333333",
                PaymentStatus.CANCELADO,
                PaymentType.PARCELADO_LOJA,
                3
        );

        repository.save(paymentOriginal);
        repository.save(paymentAtualizado);

        assertEquals(paymentAtualizado, repository.findById(1L).orElseThrow());
        assertEquals(1, repository.findAll().size());
    }
}

