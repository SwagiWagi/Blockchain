package main.me.therom.blockchain.builders;

import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.blockchain.abstracts.Body;
import main.me.therom.blockchain.abstracts.Header;
import main.me.therom.blockchain.abstracts.Transaction;
import main.me.therom.blockchain.implementations.BlockV1;
import main.me.therom.blockchain.implementations.BodyV1;
import main.me.therom.blockchain.implementations.HeaderV1;

import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BlockBuilder
{
	private final int blockVersion;

	private final int headerVersion;

	private final int height;
	private final byte[] previousBlockHash;
	private final byte[] hash;
	private final byte[] merkleRoot;
	private final int difficulty;
	private final Instant timeStamp;
	private final double rewardAmount;
	private final int nonce;
	private final PublicKey minerPublicKey;

	private final int bodyVersion;

	private final List<Transaction> transactions;

	public BlockBuilder(
			int blockVersion,
			int headerVersion,
			int height,
			byte[] previousBlockHash,
			byte[] hash,
			byte[] merkleRoot,
			int difficulty,
			Instant timeStamp,
			double rewardAmount,
			int nonce,
			PublicKey minerPublicKey,
			int bodyVersion
	)
	{
		this.blockVersion = blockVersion;
		this.headerVersion = headerVersion;
		this.height = height;
		this.previousBlockHash = previousBlockHash;
		this.hash = hash;
		this.merkleRoot = merkleRoot;
		this.difficulty = difficulty;
		this.timeStamp = timeStamp;
		this.rewardAmount = rewardAmount;
		this.nonce = nonce;
		this.minerPublicKey = minerPublicKey;
		this.bodyVersion = bodyVersion;
		this.transactions = new ArrayList<>();
	}

	public void addTransaction(Transaction transaction)
	{
		this.transactions.add(transaction);
	}

	//I could make it abstract but then it would be very hard to make it cross-version compatible
	public Block build()
	{
		Header header;
		Body body;
		Block block;

		switch (headerVersion)
		{
			default:
				header = new HeaderV1(
						height,
						previousBlockHash,
						hash,
						merkleRoot,
						difficulty,
						timeStamp,
						rewardAmount,
						nonce,
						minerPublicKey
				);
				break;
		}

		switch (bodyVersion)
		{
			default:
				body = new BodyV1(
						transactions
				);
				break;
		}

		switch (blockVersion)
		{
			default:
				block = new BlockV1(
						header, body
				);
				break;
		}

		return block;
	}
}
