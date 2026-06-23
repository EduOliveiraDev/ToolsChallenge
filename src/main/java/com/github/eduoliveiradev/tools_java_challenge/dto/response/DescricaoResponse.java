package com.github.eduoliveiradev.tools_java_challenge.dto.response;

import java.time.LocalDateTime;

public record DescricaoResponse(
        String valor,
        LocalDateTime dataHora,
        String estabelecimento,
        String nsu,
        String codigoAutorizacao,
        String status
) {
}
