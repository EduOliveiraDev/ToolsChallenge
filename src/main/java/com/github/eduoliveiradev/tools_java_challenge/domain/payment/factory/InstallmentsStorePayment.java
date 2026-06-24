package com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.request.PaymentResquest;

import java.util.concurrent.atomic.AtomicLong;

public class InstallmentsStorePayment implements PaymentStrategy {

	private static final AtomicLong count = new AtomicLong(1);

	@Override
	public com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.Payment processPayment(PaymentResquest paymentResquest) {
		var generateId = count.getAndIncrement();
		return new com.github.eduoliveiradev.tools_java_challenge.domain.payment.dto.Payment(
				generateId,
				paymentResquest.transacao().cartao(),
				paymentResquest.transacao().id(),
				paymentResquest.transacao().descricao().valor(),
				paymentResquest.transacao().descricao().dataHora(),
				paymentResquest.transacao().descricao().estabelecimento(),
				"STORENSU",
				"STOREAUTH",
				"AUTORIZADO_LOJA",
				paymentResquest.transacao().formaPagamento().tipo(),
				paymentResquest.transacao().formaPagamento().parcelas()
		);
	}
}
