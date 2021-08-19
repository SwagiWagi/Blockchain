package main.me.therom.blockchain.implementations;

import main.me.therom.blockchain.abstracts.Header;

import java.security.PublicKey;
import java.time.Instant;

public class HeaderV1 extends Header
{
	public HeaderV1(
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
		super(
				1,
				height,
				previousBlockHash,
				hash,
				merkleRoot,
				difficulty,
				timeStamp,
				rewardAmount,
				nonce,
				minerPublicKey);
	}

	public HeaderV1(int height, byte[] previousBlockHash, double rewardAmount, int difficulty)
	{
		super(1, height, previousBlockHash, difficulty, rewardAmount);
	}

}
