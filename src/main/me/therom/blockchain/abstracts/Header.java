package main.me.therom.blockchain.abstracts;

import com.google.gson.annotations.JsonAdapter;
import main.me.therom.core.utils.adapters.ByteArrayTypeAdapter;
import main.me.therom.core.utils.adapters.CryptographicKeyTypeAdapter;
import main.me.therom.exceptions.BlockLockedException;

import java.security.PublicKey;
import java.time.Instant;

public abstract class Header
{
	private final int version;

	private final int height;

	@JsonAdapter(ByteArrayTypeAdapter.class)
	private final byte[] previousBlockHash;
	private final int difficulty;
	private final double rewardAmount;
	@JsonAdapter(ByteArrayTypeAdapter.class)
	protected byte[] hash;
	@JsonAdapter(ByteArrayTypeAdapter.class)
	protected byte[] merkleRoot;
	private Instant timeStamp;
	private int nonce;

	@JsonAdapter(CryptographicKeyTypeAdapter.class)
	private PublicKey minerPublicKey;

	public Header(
			int version,
			int height,
			byte[] previousBlockHash,
			byte[] hash,
			byte[] merkleRoot,
			int difficulty,
			Instant timeStamp,
			double rewardAmount,
			int nonce,
			PublicKey minerPublicKey)
	{
		this.version = version;
		this.height = height;
		this.previousBlockHash = previousBlockHash;
		this.hash = hash;
		this.merkleRoot = merkleRoot;
		this.difficulty = difficulty;
		this.timeStamp = timeStamp;
		this.rewardAmount = rewardAmount;
		this.nonce = nonce;
		this.minerPublicKey = minerPublicKey;
	}

	public Header(int version, int height, byte[] previousBlockHash, int difficulty, double rewardAmount)
	{
		this.version = version;
		this.height = height;
		this.previousBlockHash = previousBlockHash;
		this.difficulty = difficulty;
		this.rewardAmount = rewardAmount;
		this.nonce = -1;
	}

	public void setTimeStamp()
	{
		this.timeStamp = Instant.now();
	}

	public byte[] getHash()
	{
		return this.hash;
	}

	public void setHash(byte[] hash) throws BlockLockedException
	{
		if (this.hash != null)
		{
			throw new BlockLockedException();
		}

		this.hash = hash;
	}

	public int getHeight()
	{
		return this.height;
	}

	public Instant getTimeStamp()
	{
		return this.timeStamp;
	}

	public int getDifficulty()
	{
		return this.difficulty;
	}

	public byte[] getPreviousBlockHash()
	{
		return this.previousBlockHash;
	}

	public byte[] getMerkleRoot()
	{
		return this.merkleRoot;
	}

	public void setMerkleRoot(byte[] merkleRoot) throws BlockLockedException
	{
		if (this.merkleRoot != null)
		{
			throw new BlockLockedException();
		}

		this.merkleRoot = merkleRoot;
	}

	public double getRewardAmount()
	{
		return this.rewardAmount;
	}

	public void increaseNonce()
	{
		this.nonce++;
	}

	public int getNonce()
	{
		return this.nonce;
	}

	public PublicKey getMinerPublicKey()
	{
		return this.minerPublicKey;
	}

	public void setMinerPublicKey(PublicKey minerPublicKey)
	{
		this.minerPublicKey = minerPublicKey;
	}

	public int getVersion()
	{
		return this.version;
	}

}
