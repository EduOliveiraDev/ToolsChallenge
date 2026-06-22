package com.github.eduoliveiradev.tools_java_challenge.service;

import com.github.eduoliveiradev.tools_java_challenge.dto.PagamentoResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.PagamentoResquest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PagamentoService {
    public PagamentoResponse criar(PagamentoResquest pagamentoRequest) {
        return new PagamentoResponse(
                pagamentoRequest.cartao(),
                UUID.randomUUID(),
                pagamentoRequest.valor(),
                LocalDateTime.now(),
                pagamentoRequest.estabelecimento(),
                "1234567890",
                "147258369",
                "AUTORIZADO",
                pagamentoRequest.tipo(),
                pagamentoRequest.parcelas()
        );
    }
}
