package main.me.therom.blockchain.implementations;

import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.blockchain.abstracts.Body;
import main.me.therom.blockchain.abstracts.Header;
import main.me.therom.blockchain.abstracts.Transaction;
import main.me.therom.core.utils.ConstManager;
import main.me.therom.cryptography.HashHelper;
import main.me.therom.exceptions.BlockLockedException;
import main.me.therom.exceptions.HashingException;

import java.security.PublicKey;
import java.util.Arrays;

public class BlockV1 extends Block
{
	public BlockV1(Header header, Body body)
	{
		super(1, header, body);
	}

	@Override
	public void mineBlock(PublicKey minerPublicKey) throws HashingException, BlockLockedException
	{
		byte[] merkleRoot = calculateMerkleRoot();

		byte[] tmpHash = hashBlock(minerPublicKey, merkleRoot);

		while (!(new String(tmpHash).substring(0, getHeader().getDifficulty()).equals(
				new String(new char[getHeader().getDifficulty()]).replace("\0", ConstManager.PROOF_OF_WORK_PREFIX))))
		{
			//Nonce starts at -1 so it resets it to 0
			getHeader().increaseNonce();

			tmpHash = hashBlock(minerPublicKey, merkleRoot);
		}

		getHeader().setMerkleRoot(merkleRoot);
		getHeader().setHash(tmpHash);
		getHeader().setMinerPublicKey(minerPublicKey);
		getHeader().setTimeStamp();
	}

	@Override
	public byte[] hashBlock(PublicKey minerPublicKey, byte[] merkleRoot) throws HashingException
	{
		return HashHelper.SHA256(
				new String(HashHelper.encodePublicKey(minerPublicKey)) +
						new String(merkleRoot) +
						new String(getHeader().getPreviousBlockHash()) +
						getHeader().getNonce());
	}

	@Override
	public boolean isBlockValid() throws HashingException
	{
		byte[] merkleRoot = calculateMerkleRoot();

		//Block has been tampered
		if (!Arrays.equals(getHeader().getHash(), hashBlock(getHeader().getMinerPublicKey(), merkleRoot)))
		{
			return false;
		}

		byte[] previousTransactionHash = ConstManager.FIRST_TRANSACTION_IN_QUEUE_PREVIOUS_TRANSACTION_HASH;

		for (Transaction transaction : getBody().getTransactions())
		{
			if (!transaction.isValid() || !Arrays.equals(transaction.getPreviousHash(), previousTransactionHash))
			{
				return false;
			}

			previousTransactionHash = transaction.getHash();
		}

		return true;
	}

}
