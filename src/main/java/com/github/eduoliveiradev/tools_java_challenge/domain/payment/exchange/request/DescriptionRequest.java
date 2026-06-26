package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record DescriptionRequest(
        @Positive(message = "O campo valor deve ser maior que zero")
        @NotBlank(message = "O campo valor é obrigatório")
        String valor,
        LocalDateTime dataHora,
        @NotBlank(message = "O campo estabelecimento é obrigatório")
        String estabelecimento
) {
        public DescriptionRequest{
                if (dataHora == null) {
                        dataHora = LocalDateTime.now();
                }
        }
}
