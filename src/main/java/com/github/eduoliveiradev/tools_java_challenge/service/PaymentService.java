package com.github.eduoliveiradev.tools_java_challenge.service;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.response.DescriptionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.response.PaymentMethodResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.response.TransactionResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory.PaymentFactory;
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
}
