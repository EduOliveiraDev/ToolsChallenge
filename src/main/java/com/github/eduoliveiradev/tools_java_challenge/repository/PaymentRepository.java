package com.github.eduoliveiradev.tools_java_challenge.repository;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.Payment;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PaymentRepository {

    private final Map<Long, Payment> dataBase = new HashMap<>();

    public Payment save(Payment payment) {
        dataBase.put(payment.getId(), payment);
        return payment;
    }

    public List<Payment> findAll() {
        return new ArrayList<>(dataBase.values());
    }

    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(dataBase.get(id));
    }
}
