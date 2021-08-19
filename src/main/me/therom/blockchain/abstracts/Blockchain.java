package main.me.therom.blockchain.abstracts;

import main.me.therom.core.utils.JsonHelper;
import main.me.therom.db.managers.BlockchainDBManager;
import main.me.therom.exceptions.BlockLockedException;
import main.me.therom.exceptions.BlockchainInitializationException;
import main.me.therom.exceptions.HashingException;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public abstract class Blockchain
{
	protected final JsonHelper jsonHelper;
	protected final BlockchainDBManager blockchainDBManager;
	//Would not be on a file since it's disposable, and should be fairly small
	private final List<Transaction> transactionQueue;

	public Blockchain(JsonHelper jsonHelper, BlockchainDBManager blockchainDBManager)
	{
		this.jsonHelper = jsonHelper;
		this.blockchainDBManager = blockchainDBManager;
		this.transactionQueue = new ArrayList<>();
	}

	public List<Transaction> getTransactionQueue()
	{
		return this.transactionQueue;
	}

	public abstract Block createGenesisBlock(PublicKey minerPublicKey) throws BlockchainInitializationException, IOException, BlockLockedException, HashingException;

	public abstract void addBlock(Block block) throws IOException;

	public abstract void addTransactionToQueue(Transaction transaction);

	public abstract int getLength();

	public abstract Block mineTransactionsIntoBlock(PublicKey minerPublicKey) throws IOException, BlockLockedException, HashingException;

	public abstract boolean isChainValid() throws HashingException, IOException;

	public abstract void clearTransactionQueue();

	public abstract Block getBlockByIndex(int index) throws IOException;

	public abstract Block getBlockByHash(byte[] hash) throws IOException;

	public abstract double getAccountBalance(PublicKey publicKey) throws IOException;
}
