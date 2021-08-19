package main.me.therom.blockchain.abstracts;

import main.me.therom.exceptions.BlockLockedException;
import main.me.therom.exceptions.HashingException;

import java.security.PublicKey;

public abstract class Block
{
	//Used for serialization
	private final int version;

	private final Header header;
	private final Body body;

	public Block(int version, Header header, Body body)
	{
		this.version = version;
		this.header = header;
		this.body = body;
	}

	public Header getHeader()
	{
		return this.header;
	}

	public Body getBody()
	{
		return this.body;
	}

	public byte[] calculateMerkleRoot() throws HashingException
	{
		return getBody().calculateMerkleRoot(getHeader().getRewardAmount());
	}

	public int getVersion()
	{
		return version;
	}

	public abstract void mineBlock(PublicKey minerPublicKey) throws BlockLockedException, HashingException;

	public abstract boolean isBlockValid() throws HashingException;

	public abstract byte[] hashBlock(PublicKey minerPublicKey, byte[] merkleRoot) throws HashingException;

}
