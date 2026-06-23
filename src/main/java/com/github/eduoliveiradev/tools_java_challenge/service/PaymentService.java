package com.github.eduoliveiradev.tools_java_challenge.service;

import com.github.eduoliveiradev.tools_java_challenge.dto.response.DescriptionResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.PaymentMethodResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.TransactionResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public PaymentResponse criar(PaymentResquest pagamentoRequest) {
        return new PaymentResponse(
                new TransactionResponse(
                        pagamentoRequest.transacao().cartao(),
                        pagamentoRequest.transacao().id(),
                        new DescriptionResponse(pagamentoRequest.transacao().descricao().valor(),
                                pagamentoRequest.transacao().descricao().dataHora(),
                                pagamentoRequest.transacao().descricao().estabelecimento(),
                                "1234567890",
                                "147258369",
                                "AUTORIZADO"
                        ),
                        new PaymentMethodResponse(
                                pagamentoRequest.transacao().formaPagamento().tipo(),
                                pagamentoRequest.transacao().formaPagamento().parcelas()
                        )
                )
        );
    }
}
