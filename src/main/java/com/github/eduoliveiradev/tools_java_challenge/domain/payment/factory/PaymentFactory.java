package com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.Payment;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class PaymentFactory {

    private static final AtomicLong count = new AtomicLong(1);

    public Payment createPayment(PaymentResquest paymentResquest) {
        if (paymentResquest == null) {
            throw new IllegalArgumentException("Payment request não pode ser nulo");
        }

        var tipo = paymentResquest.transacao().formaPagamento().tipo();
        var generatedId = count.getAndIncrement();

        PaymentStrategy strategy = switch (tipo.toUpperCase()) {
            case "AVISTA" -> new PayFullPayment();
            case "PARCELADO LOJA" -> new InstallmentsStorePayment();
            case "PARCELADO EMISSOR" -> new InstallmentsBankPayment();
            default -> throw new IllegalArgumentException("Tipo de pagamento inválido");
        };

        return strategy.processPayment(paymentResquest, generatedId);
    }
}
