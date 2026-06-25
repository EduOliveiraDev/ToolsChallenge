package com.github.eduoliveiradev.tools_java_challenge.service;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentStatus;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentType;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.Payment;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.PaymentMapper;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.DescriptionRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentMethodRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.TransactionRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.DescriptionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentMethodResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.TransactionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory.PaymentFactory;
import com.github.eduoliveiradev.tools_java_challenge.exception.PaymentNotFoundException;
import com.github.eduoliveiradev.tools_java_challenge.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentFactory paymentFactory;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    private LocalDateTime dataHora;
    private PaymentResquest paymentRequest;
    private Payment payment;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        dataHora = LocalDateTime.of(2026, 6, 25, 10, 30);

        paymentRequest = new PaymentResquest(
                new TransactionRequest(
                        "1234567890123456",
                        new DescriptionRequest(
                                "99.90",
                                dataHora,
                                "Loja Teste"
                        ),
                        new PaymentMethodRequest(
                                "AVISTA",
                                1
                        )
                )
        );

        payment = new Payment(
                "1234567890123456",
                1L,
                "99.90",
                dataHora,
                "Loja Teste",
                "1234567890",
                "147258369",
                PaymentStatus.AUTORIZADO,
                PaymentType.AVISTA,
                1
        );

        paymentResponse = new PaymentResponse(
                new TransactionResponse(
                        "1234567890123456",
                        1L,
                        new DescriptionResponse(
                                "99.90",
                                dataHora,
                                "Loja Teste",
                                "1234567890",
                                "147258369",
                                PaymentStatus.AUTORIZADO
                        ),
                        new PaymentMethodResponse(
                                "AVISTA",
                                1
                        )
                )
        );
    }

    @Test
    @DisplayName("Deve criar pagamento com sucesso")
    void deveCriarPagamentoComSucesso() {
        when(paymentFactory.createPayment(paymentRequest)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponse(payment)).thenReturn(paymentResponse);

        PaymentResponse resultado = paymentService.create(paymentRequest);

        assertEquals(paymentResponse, resultado);
        verify(paymentFactory).createPayment(paymentRequest);
        verify(paymentRepository).save(payment);
        verify(paymentMapper).toResponse(payment);
    }

    @Test
    @DisplayName("Deve propagar erro ao criar pagamento quando factory falhar")
    void devePropagarErroAoCriarPagamentoQuandoFactoryFalhar() {
        when(paymentFactory.createPayment(paymentRequest))
                .thenThrow(new IllegalArgumentException("Tipo de pagamento inválido"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.create(paymentRequest)
        );

        assertEquals("Tipo de pagamento inválido", exception.getMessage());
        verify(paymentFactory).createPayment(paymentRequest);
        verify(paymentRepository, never()).save(any());
        verify(paymentMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve retornar todos os pagamentos com sucesso")
    void deveRetornarTodosPagamentosComSucesso() {
        Payment payment2 = new Payment(
                "9876543210987654",
                2L,
                "150.00",
                dataHora,
                "Loja Teste 2",
                "0987654321",
                "963369741",
                PaymentStatus.AUTORIZADO,
                PaymentType.AVISTA,
                1
        );

        PaymentResponse paymentResponse2 = new PaymentResponse(
                new TransactionResponse(
                        "9876543210987654",
                        2L,
                        new DescriptionResponse(
                                "150.00",
                                dataHora,
                                "Loja Teste 2",
                                "0987654321",
                                "963369741",
                                PaymentStatus.AUTORIZADO
                        ),
                        new PaymentMethodResponse(
                                "AVISTA",
                                1
                        )
                )
        );

        when(paymentRepository.findAll()).thenReturn(List.of(payment, payment2));
        when(paymentMapper.toResponse(payment)).thenReturn(paymentResponse);
        when(paymentMapper.toResponse(payment2)).thenReturn(paymentResponse2);

        List<PaymentResponse> resultado = paymentService.getAllPayments();

        assertEquals(2, resultado.size());
        assertEquals(paymentResponse, resultado.get(0));
        assertEquals(paymentResponse2, resultado.get(1));
        verify(paymentRepository).findAll();
        verify(paymentMapper).toResponse(payment);
        verify(paymentMapper).toResponse(payment2);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver pagamentos cadastrados")
    void deveLancarExcecaoQuandoNaoHouverPagamentosCadastrados() {
        when(paymentRepository.findAll()).thenReturn(List.of());

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.getAllPayments()
        );

        assertEquals("Nenhum pagamento encontrado", exception.getMessage());
        verify(paymentRepository).findAll();
        verify(paymentMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve retornar pagamento por id com sucesso")
    void deveRetornarPagamentoPorIdComSucesso() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(payment)).thenReturn(paymentResponse);

        PaymentResponse resultado = paymentService.getPaymentById(1L);

        assertEquals(paymentResponse, resultado);
        verify(paymentRepository).findById(1L);
        verify(paymentMapper).toResponse(payment);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagamento por id não existir")
    void deveLancarExcecaoQuandoPagamentoPorIdNaoExistir() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.getPaymentById(1L)
        );

        assertEquals("Pagamento não encontrado", exception.getMessage());
        verify(paymentRepository).findById(1L);
        verify(paymentMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve cancelar pagamento com sucesso")
    void deveCancelarPagamentoComSucesso() {
        Payment canceledPayment = payment.changeStatus(PaymentStatus.CANCELADO);
        PaymentResponse canceledResponse = new PaymentResponse(
                new TransactionResponse(
                        "1234567890123456",
                        1L,
                        new DescriptionResponse(
                                "99.90",
                                dataHora,
                                "Loja Teste",
                                "1234567890",
                                "147258369",
                                PaymentStatus.CANCELADO
                        ),
                        new PaymentMethodResponse(
                                "AVISTA",
                                1
                        )
                )
        );

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(canceledPayment)).thenReturn(canceledPayment);
        when(paymentMapper.toResponse(canceledPayment)).thenReturn(canceledResponse);

        PaymentResponse resultado = paymentService.cancelPayment(1L);

        assertEquals(canceledResponse, resultado);
        verify(paymentRepository).findById(1L);
        verify(paymentRepository).save(canceledPayment);
        verify(paymentMapper).toResponse(canceledPayment);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar pagamento inexistente")
    void deveLancarExcecaoAoCancelarPagamentoInexistente() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.cancelPayment(1L)
        );

        assertEquals("Pagamento não encontrado", exception.getMessage());
        verify(paymentRepository).findById(1L);
        verify(paymentRepository, never()).save(any());
        verify(paymentMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve retornar pagamento cancelado por id com sucesso")
    void deveRetornarPagamentoCanceladoPorIdComSucesso() {
        Payment canceledPayment = payment.changeStatus(PaymentStatus.CANCELADO);
        PaymentResponse canceledResponse = new PaymentResponse(
                new TransactionResponse(
                        "1234567890123456",
                        1L,
                        new DescriptionResponse(
                                "99.90",
                                dataHora,
                                "Loja Teste",
                                "1234567890",
                                "147258369",
                                PaymentStatus.CANCELADO
                        ),
                        new PaymentMethodResponse(
                                "AVISTA",
                                1
                        )
                )
        );

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(canceledPayment));
        when(paymentMapper.toResponse(canceledPayment)).thenReturn(canceledResponse);

        PaymentResponse resultado = paymentService.getPaymentCanceledById(1L);

        assertEquals(canceledResponse, resultado);
        verify(paymentRepository).findById(1L);
        verify(paymentMapper).toResponse(canceledPayment);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagamento cancelado por id não existir")
    void deveLancarExcecaoQuandoPagamentoCanceladoPorIdNaoExistir() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.getPaymentCanceledById(1L)
        );

        assertEquals("Pagamento não encontrado", exception.getMessage());
        verify(paymentRepository).findById(1L);
        verify(paymentMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagamento não estiver cancelado")
    void deveLancarExcecaoQuandoPagamentoNaoEstiverCancelado() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.getPaymentCanceledById(1L)
        );

        assertEquals("Pagamento não está cancelado", exception.getMessage());
        verify(paymentRepository).findById(1L);
        verify(paymentMapper, never()).toResponse(any());
    }
}


