package main.me.therom.blockchain.implementations;

import main.me.therom.blockchain.abstracts.Body;
import main.me.therom.blockchain.abstracts.Transaction;
import main.me.therom.cryptography.HashHelper;
import main.me.therom.exceptions.HashingException;

import java.util.ArrayList;
import java.util.List;

public class BodyV1 extends Body
{
	public BodyV1(List<Transaction> transactions)
	{
		super(1, transactions);
	}

	@Override
	public byte[] calculateMerkleRoot(double rewardAmount) throws HashingException
	{
		List<byte[]> transactionsHashes = new ArrayList<>();

		for (int i = 0; i < getTransactions().size(); i++)
		{
			transactionsHashes.add(getTransactions().get(i).getHash());
		}

		return calculateMerkleRootRecursive(transactionsHashes);
	}

	private byte[] calculateMerkleRootRecursive(List<byte[]> oldHashes) throws HashingException
	{
		List<byte[]> newHashes = new ArrayList<>();

		for (int i = 0; i < oldHashes.size(); i += 2)
		{
			byte[] firstTransaction = oldHashes.get(i);

			byte[] secondTransaction = (oldHashes.size() - i >= 2) ? oldHashes.get(i + 1) : null;

			if (secondTransaction == null)
			{
				newHashes.add(HashHelper.SHA256(firstTransaction));
			}
			else
			{
				newHashes.add(HashHelper.SHA256(
						new String(firstTransaction) + new String(secondTransaction)));
			}
		}

		if (newHashes.size() == 1)
		{
			return newHashes.get(0);
		}

		return calculateMerkleRootRecursive(newHashes);
	}

}
