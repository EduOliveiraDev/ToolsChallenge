package com.github.eduoliveiradev.tools_java_challenge.controller;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService;}

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody @Valid PaymentResquest paymentResquest
    ) {
        return ResponseEntity.ok(paymentService.create(paymentResquest));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

}
