package com.github.eduoliveiradev.tools_java_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.DescriptionRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentMethodRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.TransactionRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.DescriptionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentMethodResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.TransactionResponse;
import com.github.eduoliveiradev.tools_java_challenge.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveProcessarPagamentoComSucesso() throws Exception {

        var dataHora = LocalDateTime.now();

        PaymentResquest request =
                new PaymentResquest(
                        new TransactionRequest(
                                "1234567890123456",
                                new DescriptionRequest(
                                        "99.90",
                                        dataHora,
                                        "Loja Teste"
                                ),
                                new PaymentMethodRequest(
                                        "CREDITO",
                                        1
                                )
                        )
                );

        PaymentResponse responseMock =
                new PaymentResponse(
                        new TransactionResponse(
                                "1234567890123456",
                                1L,
                                new DescriptionResponse(
                                        "99.90",
                                        dataHora,
                                        "Loja Teste",
                                        "1234567890",
                                        "147258369",
                                        "AUTORIZADO"
                                ),
                                new PaymentMethodResponse(
                                        "AVISTA",
                                        1
                                )
                        )
                );

        when(paymentService.create(any())).thenReturn(responseMock);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.cartao").value("1234567890123456"))
                .andExpect(jsonPath(".transacao.id").value(1))
                .andExpect(jsonPath("$.transacao.descricao.valor").value("99.90"))
                .andExpect(jsonPath("$.transacao.descricao.status").value("AUTORIZADO"))
                .andExpect(jsonPath("$.transacao.formaPagamento.tipo").value("AVISTA"));
    }
}

