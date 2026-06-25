package com.github.eduoliveiradev.tools_java_challenge.service;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.enums.PaymentStatus;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.PaymentMapper;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory.PaymentFactory;
import com.github.eduoliveiradev.tools_java_challenge.exception.PaymentNotFoundException;
import com.github.eduoliveiradev.tools_java_challenge.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentFactory paymentFactory;
    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentFactory paymentFactory, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentFactory = paymentFactory;
        this.paymentMapper = paymentMapper;
    }

    public PaymentResponse create(PaymentResquest paymentResquest) {
        var payment = paymentFactory.createPayment(paymentResquest);
        paymentRepository.save(payment);
        return paymentMapper.toResponse(payment);
    }

    public List<PaymentResponse> getAllPayments() {
        var payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException("Nenhum pagamento encontrado");
        }
        return payments.stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(Long id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pagamento não encontrado"));
        return paymentMapper.toResponse(payment);
    }

    public PaymentResponse cancelPayment(Long id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pagamento não encontrado"));
        var updatedPayment = payment.changeStatus(PaymentStatus.CANCELADO);
        paymentRepository.save(updatedPayment);
        return paymentMapper.toResponse(updatedPayment);
    }

    public PaymentResponse getPaymentCanceledById(Long id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pagamento não encontrado"));
        if (!payment.status().equals(PaymentStatus.CANCELADO)) {
            throw new PaymentNotFoundException("Pagamento não está cancelado");
        }
        return paymentMapper.toResponse(payment);
    }
}
