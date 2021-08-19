package main.me.therom.blockchain.builders;

import main.me.therom.blockchain.abstracts.Transaction;

import java.time.Instant;

/**
 * @param <T> The implementor
 * @author SwagiWagi
 * <p>
 * This whole class is because of the hash setting, thank you hash, seriously, this whole class (well and project) won't be possible without you
 */
public abstract class TransactionBuilder<T extends Transaction>
{
	protected final T transaction;

	public TransactionBuilder(T transaction)
	{
		this.transaction = transaction;
	}

	public TransactionBuilder<T> setHash(byte[] hash)
	{
		this.transaction.setHash(hash);

		return this;
	}

	public TransactionBuilder<T> setSignature(byte[] signature)
	{
		this.transaction.setSignature(signature);

		return this;
	}

	public TransactionBuilder<T> setPreviousHash(byte[] previousHash)
	{
		this.transaction.setPreviousHash(previousHash);

		return this;
	}

	public TransactionBuilder<T> setTimeStamp(Instant timeStamp)
	{
		this.transaction.setTimeStamp(timeStamp);

		return this;
	}

	public T buildTransaction()
	{
		T transaction = buildImplementation();

		if (this.transaction.getHash() == null)
		{
			transaction.setHash();
		}

		return transaction;
	}

	protected abstract T buildImplementation();
}
