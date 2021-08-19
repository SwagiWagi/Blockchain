package main.me.therom.blockchain.implementations;

import main.me.therom.blockchain.abstracts.*;
import main.me.therom.blockchain.builders.PaymentBuilder;
import main.me.therom.core.utils.ConstManager;
import main.me.therom.core.utils.JsonHelper;
import main.me.therom.cryptography.HashHelper;
import main.me.therom.db.loopers.BlockLooper;
import main.me.therom.db.managers.BlockchainDBManager;
import main.me.therom.exceptions.BlockLockedException;
import main.me.therom.exceptions.BlockchainInitializationException;
import main.me.therom.exceptions.HashingException;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockchainV1 extends Blockchain
{
	public BlockchainV1(JsonHelper jsonHelper, BlockchainDBManager blockchainDBManager)
	{
		super(jsonHelper, blockchainDBManager);
	}

	@Override
	public boolean isChainValid() throws HashingException, IOException
	{
		byte[] previousBlockHash = ConstManager.GENESIS_BLOCK_PREVIOUS_HASH;

		BlockLooper looper = this.blockchainDBManager.generateNewBlockLooper();

		int counter = 0;
		while (looper.hasNext())
		{
			Block block = looper.getNextElement();

			if (!block.isBlockValid() || !Arrays.equals(block.getHeader().getPreviousBlockHash(), previousBlockHash))
			{
				return false;
			}

			previousBlockHash = block.getHeader().getHash();
			counter++;
		}

		return true;
	}

	@Override
	public Block mineTransactionsIntoBlock(PublicKey minerPublicKey) throws IOException, BlockLockedException, HashingException
	{
		int blockchainLength = this.blockchainDBManager.getBlockchainLength();

		//Special fromAddress - it is already (fully) encoded - no need to touch this
		Payment coinbase =
				new PaymentBuilder(HashHelper.decodePublicKey(ConstManager.FROM_ADDRESS_COINBASE), minerPublicKey, ConstManager.BLOCK_MINING_REWARD_AMOUNT)
						.setPreviousHash(ConstManager.FIRST_TRANSACTION_IN_QUEUE_PREVIOUS_TRANSACTION_HASH)
						.buildTransaction();

		byte[] previousBlockHash = blockchainLength == 0 ? ConstManager.GENESIS_BLOCK_PREVIOUS_HASH :
				this.blockchainDBManager.getBlockByIndex(blockchainLength - 1).getHeader().getHash();

		Header header = new HeaderV1(blockchainLength, previousBlockHash, ConstManager.BLOCK_MINING_REWARD_AMOUNT, ConstManager.BLOCK_MINING_DIFFICULTY);

		List<Transaction> transactions = new ArrayList<>(getTransactionQueue());

		transactions.add(coinbase);

		clearTransactionQueue();

		Body body = new BodyV1(transactions);

		Block block = new BlockV1(header, body);

		block.mineBlock(minerPublicKey);

		return block;
	}

	@Override
	public void addTransactionToQueue(Transaction transaction)
	{
		if (transaction.getPreviousHash() == null)
		{
			byte[] previousHash = (getTransactionQueue().size() == 0) ?
					ConstManager.FIRST_TRANSACTION_IN_QUEUE_PREVIOUS_TRANSACTION_HASH :
					getTransactionQueue().get(getTransactionQueue().size() - 1).getHash();

			transaction.setPreviousHash(previousHash);
		}

		this.getTransactionQueue().add(transaction);
	}

	@Override
	public Block createGenesisBlock(PublicKey minerPublicKey) throws BlockchainInitializationException, IOException, BlockLockedException, HashingException
	{
		if (!(getLength() == 0))
		{
			throw new BlockchainInitializationException();
		}

		return mineTransactionsIntoBlock(minerPublicKey);
	}

	@Override
	public void addBlock(Block block) throws IOException
	{
		this.blockchainDBManager.addBlock(block);
	}

	@Override
	public void clearTransactionQueue()
	{
		this.getTransactionQueue().clear();
	}

	@Override
	public Block getBlockByIndex(int index) throws IOException
	{
		return this.blockchainDBManager.getBlockByIndex(index);
	}

	@Override
	public Block getBlockByHash(byte[] hash) throws IOException
	{
		return this.blockchainDBManager.getBlockByHash(hash);
	}

	@Override
	public int getLength()
	{
		return this.blockchainDBManager.getBlockchainLength();
	}

	@Override
	public double getAccountBalance(PublicKey publicKey) throws IOException
	{
		double balance = 0;

		BlockLooper looper = this.blockchainDBManager.generateNewBlockLooper();

		for (int i = 0; i < looper.getLength(); i++)
		{
			Block block = looper.getNextElement();

			for (Transaction transaction : block.getBody().getTransactions())
			{
				if (transaction instanceof Payment)
				{
					if (transaction.getFromKey().equals(publicKey))
					{
						balance -= ((Payment) transaction).getAmount();
					}
					else if (transaction.getToKey().equals(publicKey))
					{
						balance += ((Payment) transaction).getAmount();
					}
				}
			}
		}

		return balance;
	}

}
