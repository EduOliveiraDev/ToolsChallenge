package com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.DescriptionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentMethodResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                new TransactionResponse(
                        payment.cartao(),
                        payment.id(),
                        new DescriptionResponse(
                                payment.valor(),
                                payment.dataHora(),
                                payment.estabelecimento(),
                                payment.nsu(),
                                payment.codigoAutorizacao(),
                                payment.status()
                        ),
                        new PaymentMethodResponse(
                                payment.tipo(),
                                payment.parcelas()
                        )
                )
        );
    }
}