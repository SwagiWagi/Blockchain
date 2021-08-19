package main.me.therom.db.managers;

import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.core.Logger;
import main.me.therom.core.utils.JsonHelper;
import main.me.therom.db.loopers.BlockLooper;
import main.me.therom.exceptions.FileExistsException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class BlockchainDBManager
{
	private final Logger logger;
	private final JsonHelper jsonHelper;
	private final File directory;

	public BlockchainDBManager(Logger logger, JsonHelper jsonHelper, String directoryPath)
	{
		this.logger = logger;
		this.jsonHelper = jsonHelper;
		this.directory = new File(directoryPath);
	}

	public Block getBlockByIndex(int index) throws IOException
	{
		try
		{
			for (File file : this.directory.listFiles())
			{
				String block = file.getName();

				if (block.substring(block.indexOf("-") + 1, block.indexOf(".")).equals(String.valueOf(index)))
				{
					StringBuilder fileLines = new StringBuilder();

					Files.readAllLines(file.toPath()).forEach(fileLines::append);

					return this.jsonHelper.getObjectFromJson(fileLines.toString(), Block.class);
				}
			}
		}
		catch (IOException ex)
		{
			this.logger.log("An exception has been thrown while trying to read block file.");
			this.logger.log(ex.getMessage());

			throw ex;
		}

		return null;
	}

	public Block getBlockByHash(byte[] hash) throws IOException
	{
		for (File file : this.directory.listFiles())
		{
			String blockName = file.getName();

			if (blockName.substring(0, blockName.indexOf("-")).substring(1).equals(new String(hash)))
			{
				StringBuilder fileLines = new StringBuilder();

				Files.readAllLines(file.toPath()).forEach(fileLines::append);

				return this.jsonHelper.getObjectFromJson(fileLines.toString(), Block.class);
			}
		}

		return null;
	}

	public void addBlock(Block block) throws IOException
	{
		try
		{
			File newBlockFile = new File(this.directory.getAbsolutePath() + "\\BLOCK" + new String(block.getHeader().getHash()) + "-" + block.getHeader().getHeight() + ".json");

			if (newBlockFile.exists())
			{
				throw new IOException(new FileExistsException());
			}

			try (FileWriter writer = new FileWriter(newBlockFile))
			{
				writer.write(this.jsonHelper.getJsonFromObject(block));
			}
		}
		catch (IOException ex)
		{
			this.logger.log("An exception has been thrown while trying to write a new block file.");
			this.logger.log(ex.getMessage());

			throw ex;
		}
	}

	public int getBlockchainLength()
	{
		try
		{
			return this.directory.listFiles((dir, name) -> name.startsWith("BLOCK") && name.endsWith(".json")).length;
		}
		catch (NullPointerException ex)
		{
			return 0;
		}
	}

	public int getBlockchainVersion() throws IOException
	{
		return getBlockByIndex(0).getHeader().getVersion();
	}

	public BlockLooper generateNewBlockLooper()
	{
		return new BlockLooper(this.jsonHelper, this.directory);
	}

	public void deleteBlockchain()
	{
		try {
			for (File file : this.directory.listFiles()) {
				file.delete();
			}
		}
		catch (Exception ex)
		{}
	}

}
