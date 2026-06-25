package com.github.eduoliveiradev.tools_java_challenge.domain.payment.factory;

import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.Payment;
import com.github.eduoliveiradev.tools_java_challenge.domain.payment.exchange.request.PaymentResquest;

public class InstallmentsStorePayment implements PaymentStrategy {

	@Override
	public Payment processPayment(PaymentResquest paymentResquest, Long generatedId) {
		return new Payment(
				paymentResquest.transacao().cartao(),
				generatedId,
				paymentResquest.transacao().descricao().valor(),
				paymentResquest.transacao().descricao().dataHora(),
				paymentResquest.transacao().descricao().estabelecimento(),
				"1234567890",
				"147258369",
				"AUTORIZADO",
				paymentResquest.transacao().formaPagamento().tipo(),
				paymentResquest.transacao().formaPagamento().parcelas()
		);
	}
}
