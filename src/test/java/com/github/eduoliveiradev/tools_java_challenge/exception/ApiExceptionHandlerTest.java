package com.github.eduoliveiradev.tools_java_challenge.exception;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    @DisplayName("Deve retornar bad request com mensagem padrão quando não houver InvalidFormatException")
    void deveRetornarBadRequestComMensagemPadrao() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Erro de leitura");

        var response = handler.handleHttpMessageNotReadable(exception);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("É obrigatório informar todos os campos obrigátorios com valores válidos", response.getBody().get("erro"))
        );
    }

    @Test
    @DisplayName("Deve retornar bad request com campo inválido quando existir InvalidFormatException")
    void deveRetornarBadRequestComCampoInvalido() {
        InvalidFormatException cause = InvalidFormatException.from((JsonParser) null, "Campo inválido", "abc", Integer.class);
        cause.prependPath(new Object(), "tipo");
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Erro de leitura", cause);

        var response = handler.handleHttpMessageNotReadable(exception);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("Campo tipo está com valor inválido", response.getBody().get("erro"))
        );
    }

    @Test
    @DisplayName("Deve retornar bad request para erro de validação")
    void deveRetornarBadRequestParaErroDeValidacao() throws Exception {
        Method method = DummyController.class.getDeclaredMethod("handle", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "transacao", "O campo transacao é obrigatório"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        var response = handler.handleValidation(exception);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("O campo transacao é obrigatório", response.getBody().get("erro"))
        );
    }

    @Test
    @DisplayName("Deve retornar bad request para IllegalArgumentException")
    void deveRetornarBadRequestParaIllegalArgumentException() {
        var response = handler.handleNotFound(new IllegalArgumentException("Tipo de pagamento inválido"));

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("Tipo de pagamento inválido", response.getBody().getMessage())
        );
    }

    @Test
    @DisplayName("Deve retornar not found para PaymentNotFoundException")
    void deveRetornarNotFoundParaPaymentNotFoundException() {
        var response = handler.handleNotFound(new PaymentNotFoundException("Pagamento não encontrado"));

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("Pagamento não encontrado", response.getBody().getMessage())
        );
    }

    private static class DummyController {
        @SuppressWarnings("unused")
        void handle(String value) {
        }
    }
}


