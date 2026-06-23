package com.github.eduoliveiradev.tools_java_challenge.service;

import com.github.eduoliveiradev.tools_java_challenge.dto.response.DescricaoResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.FormaPagamentoResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.PagamentoResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.request.PagamentoResquest;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.TransacaoResponse;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {
    public PagamentoResponse criar(PagamentoResquest pagamentoRequest) {
        return new PagamentoResponse(
                new TransacaoResponse(
                        pagamentoRequest.transacao().cartao(),
                        pagamentoRequest.transacao().id(),
                        new DescricaoResponse(pagamentoRequest.transacao().descricao().valor(),
                                pagamentoRequest.transacao().descricao().dataHora(),
                                pagamentoRequest.transacao().descricao().estabelecimento(),
                                "1234567890",
                                "147258369",
                                "AUTORIZADO"
                        ),
                        new FormaPagamentoResponse(
                                pagamentoRequest.transacao().formaPagamento().tipo(),
                                pagamentoRequest.transacao().formaPagamento().parcelas()
                        )
                )
        );
    }
}
