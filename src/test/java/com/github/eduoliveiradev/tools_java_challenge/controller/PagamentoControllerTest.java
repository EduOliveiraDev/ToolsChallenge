package com.github.eduoliveiradev.tools_java_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eduoliveiradev.tools_java_challenge.dto.PagamentoResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.PagamentoResquest;
import com.github.eduoliveiradev.tools_java_challenge.service.PagamentoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagamentoController.class)
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagamentoService pagamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveProcessarPagamentoComSucesso() throws Exception {

        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        PagamentoResquest request = new PagamentoResquest(
                "1234567890123456",
                new BigDecimal("99.90"),
                "Loja Teste",
                "CREDITO",
                1
        );

        PagamentoResponse responseMock = new PagamentoResponse(
                "1234567890123456",
                id,
                new BigDecimal("99.90"),
                LocalDateTime.now(),
                "Loja Teste",
                "1234567890",
                "147258369",
                "APROVADO",
                "CREDITO",
                1
        );

        when(pagamentoService.criar(any())).thenReturn(responseMock);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartao").value("1234567890123456"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("APROVADO"));
    }
}

