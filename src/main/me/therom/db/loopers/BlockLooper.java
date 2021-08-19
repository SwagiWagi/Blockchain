package main.me.therom.db.loopers;

import com.google.gson.JsonParseException;
import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.core.utils.JsonHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockLooper extends Looper
{
	private final transient JsonHelper jsonHelper;
	private final List<File> blocksFiles;

	public BlockLooper(JsonHelper jsonHelper, File directoryPath)
	{
		super(directoryPath);

		this.jsonHelper = jsonHelper;

		this.blocksFiles = new ArrayList<>();

		try
		{
			this.blocksFiles.addAll(Arrays.asList(directoryPath.listFiles((dir, name) -> name.startsWith("BLOCK") && name.endsWith(".json"))));
		}
		catch (NullPointerException ignored)
		{}
	}

	/**
	 * NOTE: When iterating you should use this method and not the getBlockchainLength() on BlockchainDBManager
	 *
	 * @return
	 */
	@Override
	public int getLength()
	{
		return this.blocksFiles.size();
	}

	@Override
	public boolean hasNext()
	{
		return !this.blocksFiles.isEmpty();
	}

	@Override
	public Block getNextElement() throws IOException
	{
		StringBuilder json = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(this.blocksFiles.get(0))))
		{
			String line = reader.readLine();

			while (line != null)
			{
				json.append(line);
				line = reader.readLine();
			}
		}

		this.blocksFiles.remove(0);

		try
		{
			return this.jsonHelper.getObjectFromJson(json.toString(), Block.class);
		}
		catch (JsonParseException ex)
		{
			throw new IOException(ex);
		}
	}
}
