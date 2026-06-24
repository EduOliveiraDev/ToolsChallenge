package com.github.eduoliveiradev.tools_java_challenge.service;

import com.github.eduoliveiradev.tools_java_challenge.dto.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.DescriptionResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.PaymentMethodResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.dto.response.TransactionResponse;
import com.github.eduoliveiradev.tools_java_challenge.model.Payment;
import com.github.eduoliveiradev.tools_java_challenge.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) { this.paymentRepository = paymentRepository;}

    private static final AtomicLong count = new AtomicLong(1);

    public PaymentResponse create(PaymentResquest paymentResquest) {

        var generateId = count.getAndIncrement();

        var payment = new Payment(
                generateId,
                paymentResquest.transacao().cartao(),
                paymentResquest.transacao().id(),
                paymentResquest.transacao().descricao().valor(),
                paymentResquest.transacao().descricao().dataHora(),
                paymentResquest.transacao().descricao().estabelecimento(),
                "1234567890",
                "147258369",
                "AUTORIZADO",
                paymentResquest.transacao().formaPagamento().tipo(),
                paymentResquest.transacao().formaPagamento().parcelas()
        );

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
