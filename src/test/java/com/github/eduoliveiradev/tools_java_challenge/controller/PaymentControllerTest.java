package com.github.eduoliveiradev.tools_java_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentStatus;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.DescriptionRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentMethodRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.TransactionRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.DescriptionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentMethodResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.TransactionResponse;
import com.github.eduoliveiradev.tools_java_challenge.exception.PaymentNotFoundException;
import com.github.eduoliveiradev.tools_java_challenge.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@DisplayName("Testes da PaymentController")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private LocalDateTime dataHora;
    private PaymentResquest paymentRequest;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        dataHora = LocalDateTime.now();

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
    void deveProcessarPagamentoComSucesso() throws Exception {
        when(paymentService.create(any())).thenReturn(paymentResponse);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.cartao").value("1234567890123456"))
                .andExpect(jsonPath("$.transacao.id").value(1))
                .andExpect(jsonPath("$.transacao.descricao.valor").value("99.90"))
                .andExpect(jsonPath("$.transacao.descricao.status").value("AUTORIZADO"))
                .andExpect(jsonPath("$.transacao.formaPagamento.tipo").value("AVISTA"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao criar pagamento com request inválido")
    void deveRetornarErro400AoCriarPagamentoComRequestInvalido() throws Exception {
        String requestInvalido = "{}";

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve obter todos os pagamentos com sucesso")
    void deveObterTodosPagamentosComSucesso() throws Exception {
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

        List<PaymentResponse> payments = Arrays.asList(paymentResponse, paymentResponse2);
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].transacao.cartao").value("1234567890123456"))
                .andExpect(jsonPath("$[0].transacao.id").value(1))
                .andExpect(jsonPath("$[1].transacao.cartao").value("9876543210987654"))
                .andExpect(jsonPath("$[1].transacao.id").value(2));
    }

    @Test
    @DisplayName("Deve retornar erro 404 quando nenhum pagamento é encontrado")
    void deveRetornarErro404QuandoNenhumPagamentoEncontrado() throws Exception {
        when(paymentService.getAllPayments())
                .thenThrow(new PaymentNotFoundException("Nenhum pagamento encontrado"));

        mockMvc.perform(get("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Nenhum pagamento encontrado"));
    }

    @Test
    @DisplayName("Deve obter pagamento por ID com sucesso")
    void deveObterPagamentoPorIdComSucesso() throws Exception {
        when(paymentService.getPaymentById(anyLong())).thenReturn(paymentResponse);

        mockMvc.perform(get("/pagamentos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.cartao").value("1234567890123456"))
                .andExpect(jsonPath("$.transacao.id").value(1))
                .andExpect(jsonPath("$.transacao.descricao.valor").value("99.90"))
                .andExpect(jsonPath("$.transacao.descricao.status").value("AUTORIZADO"));
    }

    @Test
    @DisplayName("Deve retornar erro 404 quando pagamento por ID não é encontrado")
    void deveRetornarErro404QuandoPagamentoPorIdNaoEncontrado() throws Exception {
        when(paymentService.getPaymentById(anyLong()))
                .thenThrow(new PaymentNotFoundException("Pagamento não encontrado"));

        mockMvc.perform(get("/pagamentos/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pagamento não encontrado"));
    }

    @Test
    @DisplayName("Deve cancelar pagamento com sucesso")
    void deveCancelarPagamentoComSucesso() throws Exception {
        PaymentResponse canceledPayment = new PaymentResponse(
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

        when(paymentService.cancelPayment(anyLong())).thenReturn(canceledPayment);

        mockMvc.perform(patch("/pagamentos/estorno/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.cartao").value("1234567890123456"))
                .andExpect(jsonPath("$.transacao.id").value(1))
                .andExpect(jsonPath("$.transacao.descricao.status").value("CANCELADO"));
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao cancelar pagamento que não existe")
    void deveRetornarErro404AoCancelarPagamentoQueNaoExiste() throws Exception {
        when(paymentService.cancelPayment(anyLong()))
                .thenThrow(new PaymentNotFoundException("Pagamento não encontrado"));

        mockMvc.perform(patch("/pagamentos/estorno/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pagamento não encontrado"));
    }

    @Test
    @DisplayName("Deve obter pagamento cancelado por ID com sucesso")
    void deveObterPagamentoCanceladoPorIdComSucesso() throws Exception {
        PaymentResponse canceledPayment = new PaymentResponse(
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

        when(paymentService.getPaymentCanceledById(anyLong())).thenReturn(canceledPayment);

        mockMvc.perform(get("/pagamentos/estorno/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.cartao").value("1234567890123456"))
                .andExpect(jsonPath("$.transacao.id").value(1))
                .andExpect(jsonPath("$.transacao.descricao.status").value("CANCELADO"));
    }

    @Test
    @DisplayName("Deve retornar erro 404 quando pagamento cancelado por ID não é encontrado")
    void deveRetornarErro404QuandoPagamentoCanceladoPorIdNaoEncontrado() throws Exception {
        when(paymentService.getPaymentCanceledById(anyLong()))
                .thenThrow(new PaymentNotFoundException("Pagamento não encontrado"));

        mockMvc.perform(get("/pagamentos/estorno/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pagamento não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 404 quando pagamento com ID não está cancelado")
    void deveRetornarErro404QuandoPagamentoComIdNaoEstaCancelado() throws Exception {
        when(paymentService.getPaymentCanceledById(anyLong()))
                .thenThrow(new PaymentNotFoundException("Pagamento não está cancelado"));

        mockMvc.perform(get("/pagamentos/estorno/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pagamento não está cancelado"));
    }
}

