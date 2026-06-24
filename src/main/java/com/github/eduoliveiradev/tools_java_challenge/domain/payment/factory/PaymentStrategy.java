package com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.Payment;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.request.PaymentResquest;

public interface PaymentStrategy {
   Payment processPayment(PaymentResquest paymentRequest);
}

