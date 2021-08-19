package main.me.therom.blockchain.builders;

import main.me.therom.blockchain.implementations.Payment;

import java.security.PublicKey;

public class PaymentBuilder extends TransactionBuilder<Payment>
{
	public PaymentBuilder(PublicKey from, PublicKey to, double amount)
	{
		super(new Payment(from, to, amount));
	}

	@Override
	protected Payment buildImplementation()
	{
		return this.transaction;
	}
}
