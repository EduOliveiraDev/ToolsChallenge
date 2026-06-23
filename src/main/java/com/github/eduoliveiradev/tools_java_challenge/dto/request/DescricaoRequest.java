package com.github.eduoliveiradev.tools_java_challenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record DescricaoRequest(
        @Positive
        String valor,
        @NotBlank
        LocalDateTime dataHora,
        @NotBlank
        String estabelecimento
) {
}
