package com.github.eduoliveiradev.tools_java_challenge.controller;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.request.PaymentResquest;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.response.PaymentResponse;
import com.github.eduoliveiradev.tools_java_challenge.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService;}

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentResquest paymentResquest
    ) {
        return ResponseEntity.ok(paymentService.create(paymentResquest));
    }

}
