package com.github.eduoliveiradev.tools_java_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eduoliveiradev.tools_java_challenge.dto.request.DescricaoRequest;
import com.github.eduoliveiradev.tools_java_challenge.dto.request.FormaPagamentoRequest;
import com.github.eduoliveiradev.tools_java_challenge.dto.request.PagamentoResquest;
import com.github.eduoliveiradev.tools_java_challenge.dto.request.TransacaoRequest;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.DescricaoResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.FormaPagamentoResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.PagamentoResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.TransacaoResponse;
import com.github.eduoliveiradev.tools_java_challenge.service.PagamentoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

        var dataHora = LocalDateTime.now();
        var id = UUID.randomUUID().toString();

        PagamentoResquest request =
                new PagamentoResquest(
                        new TransacaoRequest(
                                "1234567890123456",
                                id,
                                new DescricaoRequest(
                                        "99.90",
                                        dataHora,
                                        "Loja Teste"
                                ),
                                new FormaPagamentoRequest(
                                        "CREDITO",
                                        1
                                )
                        )
                );

        PagamentoResponse responseMock =
                new PagamentoResponse(
                        new TransacaoResponse(
                                "1234567890123456",
                                id,
                                new DescricaoResponse(
                                        "99.90",
                                        dataHora,
                                        "Loja Teste",
                                        "1234567890",
                                        "147258369",
                                        "APROVADO"
                                ),
                                new FormaPagamentoResponse(
                                        "CREDITO",
                                        1
                                )
                        )
                );

        when(pagamentoService.criar(any())).thenReturn(responseMock);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.cartao").value("1234567890123456"))
                .andExpect(jsonPath(".transacao.id").value(id))
                .andExpect(jsonPath("$.transacao.descricao.valor").value("99.90"))
                .andExpect(jsonPath("$.transacao.descricao.status").value("APROVADO"))
                .andExpect(jsonPath("$.transacao.formaPagamento.tipo").value("CREDITO"));
    }
}

