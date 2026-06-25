package com.github.eduoliveiradev.tools_java_challenge.service;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.DescriptionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentMethodResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.TransactionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory.PaymentFactory;
import com.github.eduoliveiradev.tools_java_challenge.exception.PaymentNotFoundException;
import com.github.eduoliveiradev.tools_java_challenge.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentFactory paymentFactory;

    public PaymentService(PaymentRepository paymentRepository, PaymentFactory paymentFactory) {
        this.paymentRepository = paymentRepository;
        this.paymentFactory = paymentFactory;
    }

    public PaymentResponse create(PaymentResquest paymentResquest) {
        var payment = paymentFactory.createPayment(paymentResquest);
        paymentRepository.save(payment);
        System.out.println("Payment created: " + paymentRepository.findAll());
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

    public PaymentResponse getAllPayments() {
        var payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException("Nenhum pagamento encontrado");
        }

        return new PaymentResponse(
                new TransactionResponse(
                        payments.get(0).cartao(),
                        payments.get(0).id(),
                        new DescriptionResponse(
                                payments.get(0).valor(),
                                payments.get(0).dataHora(),
                                payments.get(0).estabelecimento(),
                                payments.get(0).nsu(),
                                payments.get(0).codigoAutorizacao(),
                                payments.get(0).status()
                        ),
                        new PaymentMethodResponse(
                                payments.get(0).tipo(),
                                payments.get(0).parcelas()
                        )
                )
        );
    }
}
