package com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.Payment;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.request.PaymentResquest;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {

    public Payment createPayment(PaymentResquest paymentResquest) {
        if (paymentResquest == null) {
            throw new IllegalArgumentException("Payment request não pode ser nulo");
        }

        var tipo = paymentResquest.transacao().formaPagamento().tipo();
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de pagamento não pode ser nulo");
        }

        PaymentStrategy strategy = switch (tipo.toUpperCase()) {
            case "AVISTA" -> new PayFullPayment();
            case "PARCELADO LOJA" -> new InstallmentsStorePayment();
            case "PARCELADO EMISSOR" -> new InstallmentsBankPayment();
            default -> throw new IllegalArgumentException("Tipo de pagamento inválido");
        };

        return strategy.processPayment(paymentResquest);
    }
}
