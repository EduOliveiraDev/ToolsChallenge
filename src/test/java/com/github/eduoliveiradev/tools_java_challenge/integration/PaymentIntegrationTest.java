package com.github.eduoliveiradev.tools_java_challenge.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.DescriptionRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentMethodRequest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.TransactionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentResquest requestValido(String cartao, String valor, String estabelecimento, String tipo, Integer parcelas) {
        return new PaymentResquest(
                new TransactionRequest(
                        cartao,
                        new DescriptionRequest(
                                valor,
                                LocalDateTime.of(2026, 6, 25, 10, 30),
                                estabelecimento
                        ),
                        new PaymentMethodRequest(tipo, parcelas)
                )
        );
    }

    private Long criarPagamentoERetornarId(PaymentResquest request) throws Exception {
        MvcResult result = mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.path("transacao").path("id").asLong();
    }

    @Test
    @DisplayName("Deve criar pagamento com sucesso")
    void deveCriarPagamentoComSucesso() throws Exception {
        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                requestValido("1234567890123456", "99.90", "Loja Teste", "AVISTA", 1)
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.id").exists())
                .andExpect(jsonPath("$.transacao.cartao").value("1234567890123456"))
                .andExpect(jsonPath("$.transacao.descricao.valor").value("99.90"))
                .andExpect(jsonPath("$.transacao.descricao.status").value("AUTORIZADO"))
                .andExpect(jsonPath("$.transacao.formaPagamento.tipo").value("AVISTA"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao criar pagamento com tipo inválido")
    void deveRetornarErro400AoCriarPagamentoComTipoInvalido() throws Exception {
        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                requestValido("1234567890123456", "99.90", "Loja Teste", "TIPO INVALIDO", 1)
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tipo de pagamento inválido"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao criar pagamento com campo obrigatório ausente")
    void deveRetornarErro400AoCriarPagamentoComCampoObrigatorioAusente() throws Exception {
        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("O campo transacao é obrigatório"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao receber JSON mal formado")
    void deveRetornarErro400AoReceberJsonMalformado() throws Exception {
        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transacao\": { \"cartao\": 123 }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("É obrigatório informar todos os campos obrigátorios com valores válidos"));
    }

    @Test
    @DisplayName("Deve listar pagamentos com sucesso")
    void deveListarPagamentosComSucesso() throws Exception {
        criarPagamentoERetornarId(requestValido("1234567890123456", "99.90", "Loja Teste", "AVISTA", 1));
        criarPagamentoERetornarId(requestValido("9876543210987654", "150.00", "Loja Teste 2", "AVISTA", 1));

        mockMvc.perform(get("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].transacao").exists())
                .andExpect(jsonPath("$[1].transacao").exists());
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao listar pagamentos quando não houver registros")
    void deveRetornarErro404AoListarPagamentosQuandoNaoHouverRegistros() throws Exception {
        mockMvc.perform(get("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Nenhum pagamento encontrado"));
    }

    @Test
    @DisplayName("Deve buscar pagamento por id com sucesso")
    void deveBuscarPagamentoPorIdComSucesso() throws Exception {
        Long id = criarPagamentoERetornarId(requestValido("1234567890123456", "99.90", "Loja Teste", "AVISTA", 1));

        mockMvc.perform(get("/pagamentos/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.id").value(id))
                .andExpect(jsonPath("$.transacao.cartao").value("1234567890123456"));
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao buscar pagamento por id inexistente")
    void deveRetornarErro404AoBuscarPagamentoPorIdInexistente() throws Exception {
        mockMvc.perform(get("/pagamentos/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pagamento não encontrado"));
    }

    @Test
    @DisplayName("Deve cancelar pagamento com sucesso")
    void deveCancelarPagamentoComSucesso() throws Exception {
        Long id = criarPagamentoERetornarId(requestValido("1234567890123456", "99.90", "Loja Teste", "AVISTA", 1));

        mockMvc.perform(patch("/pagamentos/estorno/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.id").value(id))
                .andExpect(jsonPath("$.transacao.descricao.status").value("CANCELADO"));

        mockMvc.perform(get("/pagamentos/estorno/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.id").value(id))
                .andExpect(jsonPath("$.transacao.descricao.status").value("CANCELADO"));
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao cancelar pagamento inexistente")
    void deveRetornarErro404AoCancelarPagamentoInexistente() throws Exception {
        mockMvc.perform(patch("/pagamentos/estorno/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pagamento não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao buscar pagamento cancelado inexistente")
    void deveRetornarErro404AoBuscarPagamentoCanceladoInexistente() throws Exception {
        mockMvc.perform(get("/pagamentos/estorno/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pagamento não encontrado"));
    }
}
