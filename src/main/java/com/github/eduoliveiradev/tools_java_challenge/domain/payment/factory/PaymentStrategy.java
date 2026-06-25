package com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.Payment;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;

public interface PaymentStrategy {
   Payment processPayment(PaymentResquest paymentRequest, Long generatedId);
}

