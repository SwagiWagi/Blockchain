package main.me.therom.blockchain.implementations;

import main.me.therom.blockchain.abstracts.Transaction;

import java.security.PublicKey;
import java.util.HashMap;

public class Payment extends Transaction
{
	private final double amount;

	public Payment(PublicKey fromKey, PublicKey toKey, double amount)
	{
		super("Payment", fromKey, toKey);
		this.amount = amount;
	}

	@Override
	public String getSpecificFields()
	{
		return String.valueOf(this.amount);
	}

	/**
	 * Should not be called from outside the Transaction class
	 */
	@Override
	protected boolean isImplementationValid()
	{
		return (this.amount > 0);
	}

	public double getAmount()
	{
		return this.amount;
	}

	@Override
	public HashMap<String, String> getExtraFieldsToSend()
	{
		HashMap<String, String> extraFields = new HashMap<>();

		extraFields.put("amount", Double.toString(this.amount));

		return extraFields;
	}

}
