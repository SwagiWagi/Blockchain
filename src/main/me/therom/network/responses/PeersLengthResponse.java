package main.me.therom.network.responses;

import main.me.therom.core.utils.JsonHelper;
import main.me.therom.exceptions.InvalidMessageException;

import java.io.IOException;
import java.io.InputStream;

public class PeersLengthResponse extends Response<Integer>
{
	public PeersLengthResponse(JsonHelper jsonHelper, InputStream stream) throws InvalidMessageException, IOException
	{
		super(jsonHelper, stream);
	}

	/**
	 * A response will be structured as the following:
	 * X + 2 bytes: Version number + line delimiter (\n)
	 * X + 2 bytes: Number of peers + line delimiter (\n)
	 *
	 * @throws InvalidMessageException
	 * @throws IOException
	 */
	@Override
	protected void parseResponse() throws InvalidMessageException, IOException
	{
		setVersion(stream.readLine());

		setAmount(stream.readLine());
	}

	@Override
	public Integer getNextElement()
	{
		return getAmount();
	}
}
