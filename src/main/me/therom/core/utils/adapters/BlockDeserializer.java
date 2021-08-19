package main.me.therom.core.utils.adapters;

import com.google.gson.*;
import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.blockchain.abstracts.Body;
import main.me.therom.blockchain.abstracts.Header;
import main.me.therom.blockchain.abstracts.Transaction;
import main.me.therom.blockchain.implementations.BlockV1;
import main.me.therom.blockchain.implementations.BodyV1;
import main.me.therom.blockchain.implementations.HeaderV1;
import main.me.therom.blockchain.implementations.Payment;
import main.me.therom.exceptions.BlockDeserializationException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class purpose is to make the blockchain in memory completely abstract
 * Meaning it should be cross-version.
 *
 * @author SwagiWagi
 */
public class BlockDeserializer implements JsonDeserializer<Block>
{

	/**
	 * When creating a new version (V1, V2) you should add a case in the switch for deserialization
	 */
	@Override
	public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException
	{
		JsonObject blockObject = json.getAsJsonObject();

		Header header;
		Body body;
		Block block;

		JsonObject headerObject = blockObject.get("header").getAsJsonObject();

		switch (headerObject.get("version").getAsInt())
		{
			case 1:
				header = context.deserialize(headerObject, HeaderV1.class);
				break;

			default:
				throw new JsonParseException(new BlockDeserializationException());
		}

		JsonObject bodyObject = blockObject.get("body").getAsJsonObject();

		switch (bodyObject.get("version").getAsInt())
		{
			case 1:
				List<Transaction> transactions = new ArrayList<>();

				bodyObject.get("transactions").getAsJsonArray().forEach(e -> transactions.add(getTransactionFromJson(context, e)));

				body = new BodyV1(transactions);
				break;

			default:
				throw new JsonParseException(new BlockDeserializationException());
		}

		switch (blockObject.get("version").getAsInt())
		{
			case 1:
				block = new BlockV1(header, body);
				break;

			default:
				throw new JsonParseException(new BlockDeserializationException());
		}

		return block;
	}

	/**
	 * When creating a new Transaction type (such as Payment.class) you should add a case in the switch for deserialization
	 *
	 * @param jsonElement the Transaction
	 * @return A transaction object
	 */
	private Transaction getTransactionFromJson(JsonDeserializationContext context, JsonElement jsonElement)
	{
		switch (jsonElement.getAsJsonObject().get("type").getAsString())
		{
			case "Payment":
				return context.deserialize(jsonElement, Payment.class);

			default:
				throw new JsonParseException(new BlockDeserializationException());
		}
	}

}
