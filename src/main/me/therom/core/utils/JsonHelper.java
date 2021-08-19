package main.me.therom.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.core.Logger;
import main.me.therom.core.utils.adapters.BlockDeserializer;

public class JsonHelper
{
	private final Logger logger;
	private final Gson gson;

	public JsonHelper(Logger logger)
	{
		this.logger = logger;

		//TODO .setPrettyPrinting() Should only be used for debugging
		gson = new GsonBuilder()
				.registerTypeAdapter(Block.class, new BlockDeserializer())
				.serializeNulls()
				.setPrettyPrinting()
				.create();
	}

	public <T> String getJsonFromObject(T object, Class<?> parentClass)
	{
		try
		{
			return this.gson.toJson(object, parentClass);
		}
		catch (Exception ex)
		{
			this.logger.log(ex);
			throw new JsonParseException(ex);
		}
	}

	public <T> String getJsonFromObject(T object)
	{
		try
		{
			return this.gson.toJson(object);
		}
		catch (Exception ex)
		{
			this.logger.log(ex);
			throw new JsonParseException(ex);
		}
	}

	public <T> T getObjectFromJson(String json, Class<T> parentClass)
	{
		try
		{
			return this.gson.fromJson(json, parentClass);
		}
		catch (Exception ex)
		{
			this.logger.log(ex);
			throw new JsonParseException(ex);
		}
	}

}
