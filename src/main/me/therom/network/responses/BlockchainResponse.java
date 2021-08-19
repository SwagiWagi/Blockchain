package main.me.therom.network.responses;

import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.blockchain.abstracts.Transaction;
import main.me.therom.blockchain.builders.BlockBuilder;
import main.me.therom.blockchain.builders.PaymentBuilder;
import main.me.therom.blockchain.builders.TransactionBuilder;
import main.me.therom.blockchain.implementations.Payment;
import main.me.therom.cryptography.HashHelper;
import main.me.therom.core.utils.JsonHelper;
import main.me.therom.exceptions.InvalidMessageException;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BlockchainResponse extends Response<Block>
{
	public BlockchainResponse(JsonHelper jsonHelper, InputStream stream) throws InvalidMessageException, IOException
	{
		super(jsonHelper, stream);
	}

	/**
	 * A response will be structured as the following:
	 * X + 2 bytes: Blockchain version number + line delimiter (\n)
	 * X + 2 bytes: Number of blocks that will be transferred + line delimiter (\n)
	 * <p>
	 * (loop) Blocks:
	 * X + 2 bytes: Block version number + line delimiter (\n)
	 * (Header):
	 * X + 2 bytes: Header version number + line delimiter (\n)
	 * X + 2 bytes: Height + line delimiter (\n)
	 * X + 2 bytes: Previous block hash + line delimiter (\n)
	 * X + 2 bytes: Block hash + line delimiter (\n)
	 * X + 2 bytes: Block merkle root + line delimiter (\n)
	 * X + 2 bytes: Difficulty + line delimiter (\n)
	 * X + 2 bytes: TimeStamp + line delimiter (\n)
	 * X + 2 bytes: Reward amount + line delimiter (\n)
	 * X + 2 bytes: Nonce + line delimiter (\n)
	 * X + 2 bytes: Miner public Key + line delimiter (\n)
	 * (Body):
	 * X + 2 bytes: Version + line delimiter (\n)
	 * (Loop)
	 * transactions:
	 * X + 2 bytes: Transaction type + line delimiter (\n)
	 * X + 2 bytes: Previous transaction hash + line delimiter (\n)
	 * X + 2 bytes: Encoded transaction hash + line delimiter (\n)
	 * X + 2 bytes: Encoded signature + line delimiter (\n)
	 * X + 2 bytes: Encoded from address + line delimiter (\n)
	 * X + 2 bytes: Encoded to address + line delimiter (\n)
	 * X + 2 bytes: TimeStamp + line delimiter (\n)
	 * X + 2 bytes: Extra fields (ex, amount: 7.0) + line delimiter (\n)
	 * 6 bytes: End of transaction (ENDT) + line delimiter (\n)
	 * 6 bytes: End of block (ENDB) + line delimiter (\n)
	 *
	 * @throws InvalidMessageException
	 * @throws IOException
	 */
	@Override
	public void parseResponse() throws InvalidMessageException, IOException
	{
		setVersion(stream.readLine());

		setAmount(stream.readLine());
	}

	private String getExtraField(String extraField)
	{
		return extraField.substring(extraField.indexOf(":") + 1);
	}

	@Override
	public Block getNextElement() throws InvalidMessageException, IOException
	{
		String blockVersion = stream.readLine();
		String headerVersion = stream.readLine();
		String height = stream.readLine();
		byte[] previousBlockHash = stream.readLine().getBytes();
		byte[] blockHash = stream.readLine().getBytes();
		byte[] merkleRoot = stream.readLine().getBytes();
		String difficulty = stream.readLine();
		String timeStamp = stream.readLine();
		String rewardAmount = stream.readLine();
		String nonce = stream.readLine();
		PublicKey minerPublicKey = HashHelper.decodePublicKey(stream.readLine().getBytes());

		String bodyVersion = stream.readLine();

		List<String> transactionType = new ArrayList<>();
		List<byte[]> previousTransactionHash = new ArrayList<>();
		List<byte[]> transactionHash = new ArrayList<>();
		List<byte[]> signature = new ArrayList<>();
		List<PublicKey> fromAddress = new ArrayList<>();
		List<PublicKey> toAddress = new ArrayList<>();
		List<String> transactionTimeStamp = new ArrayList<>();
		List<String> extraFields = new ArrayList<>();

		int typeToRead = 0;

		String line = stream.readLine();

		while (!line.equals("ENDB"))
		{
			switch (typeToRead)
			{
				case 0:
					transactionType.add(line);
					break;
				case 1:
					previousTransactionHash.add(line.getBytes());
					break;
				case 2:
					transactionHash.add(line.getBytes());
					break;
				case 3:
					signature.add(line.getBytes());
					break;
				case 4:
					fromAddress.add(HashHelper.decodePublicKey(line.getBytes()));
					break;
				case 5:
					toAddress.add(HashHelper.decodePublicKey(line.getBytes()));
					break;
				case 6:
					transactionTimeStamp.add(line);
					break;
				case 7:
					while (!line.equals("ENDT"))
					{
						extraFields.add(line);

						line = stream.readLine();
					}

					line = stream.readLine();
					typeToRead = 0;
					continue;
			}

			line = stream.readLine();
			typeToRead++;
		}

		int transactionLength = transactionType.size();

		if (previousTransactionHash.size() != transactionLength
				|| transactionHash.size() != transactionLength
				|| signature.size() != transactionLength
				|| fromAddress.size() != transactionLength
				|| toAddress.size() != transactionLength
				|| transactionTimeStamp.size() != transactionLength)
		{
			throw new InvalidMessageException();
		}

		List<Transaction> transactions = new ArrayList<>();

		for (int i = 0; i < transactionLength; i++)
		{
			Transaction transaction;

			switch (transactionType.get(i))
			{
				case "Payment":
					double amount = Double.parseDouble(getExtraField(extraFields.get(i)));

					TransactionBuilder<Payment> paymentBuilder =
							new PaymentBuilder(fromAddress.get(i),
									toAddress.get(i),
									amount)
							.setHash(transactionHash.get(i))
							.setPreviousHash(previousTransactionHash.get(i))
							.setTimeStamp(Instant.parse(transactionTimeStamp.get(i)));

					if (!new String(signature.get(i)).equalsIgnoreCase("null"))
					{
						paymentBuilder = paymentBuilder.setSignature(signature.get(i));
					}

					transaction = paymentBuilder.buildTransaction();

					break;
				default:
					throw new InvalidMessageException();
			}

			transactions.add(transaction);
		}

		BlockBuilder blockBuilder = new BlockBuilder(
				Integer.parseInt(blockVersion),
				Integer.parseInt(headerVersion),
				Integer.parseInt(height),
				previousBlockHash,
				blockHash,
				merkleRoot,
				Integer.parseInt(difficulty),
				Instant.parse(timeStamp),
				Double.parseDouble(rewardAmount),
				Integer.parseInt(nonce),
				minerPublicKey,
				Integer.parseInt(bodyVersion));

		transactions.forEach(blockBuilder::addTransaction);

		return blockBuilder.build();
	}
}
