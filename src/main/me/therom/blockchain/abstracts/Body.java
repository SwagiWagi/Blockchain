package main.me.therom.blockchain.abstracts;

import main.me.therom.exceptions.HashingException;

import java.util.List;

public abstract class Body
{
	private final int version;

	private final List<Transaction> transactions;

	public Body(int version, List<Transaction> transactions)
	{
		this.version = version;
		this.transactions = transactions;
	}

	public List<Transaction> getTransactions()
	{
		return this.transactions;
	}

	public int getVersion()
	{
		return this.version;
	}

	protected abstract byte[] calculateMerkleRoot(double rewardAmount) throws HashingException;

}