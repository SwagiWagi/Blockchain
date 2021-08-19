package main.me.therom.network.responses;

import main.me.therom.core.utils.JsonHelper;
import main.me.therom.exceptions.InvalidMessageException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The idea of this class is to minimize the amount of bytes going through the network, yea I could have just sent the whole directory/file,
 * but it could be 1TB of files which may be reduced by about %50 with this method.
 * <p>
 * Every response will send each property with a line delimiter marking the end of the property, ex: X + 2 bytes: Version number + line delimiter (\n)
 *
 * @param <T> The element
 * @author SwagiWagi
 */
public abstract class Response<T> implements AutoCloseable
{
	protected final transient JsonHelper jsonHelper;

	protected final transient BufferedReader stream;
	private int version;
	private int amount;

	public Response(JsonHelper jsonHelper, InputStream stream) throws InvalidMessageException, IOException
	{
		this.jsonHelper = jsonHelper;
		this.stream = new BufferedReader(new InputStreamReader(stream));
		parseResponse();
	}

	public int getVersion()
	{
		return this.version;
	}

	protected void setVersion(String version) throws InvalidMessageException
	{
		try
		{
			this.version = Integer.parseInt(version);
		}
		catch (NumberFormatException ex)
		{
			throw new InvalidMessageException(ex);
		}
	}

	public int getAmount()
	{
		return this.amount;
	}

	protected void setAmount(String amount) throws InvalidMessageException
	{
		try
		{
			this.amount = Integer.parseInt(amount);
		}
		catch (NumberFormatException ex)
		{
			throw new InvalidMessageException(ex);
		}
	}

	@Override
	public void close() throws IOException
	{
		this.stream.close();
	}

	/**
	 * Basic parsing including version and amount only.
	 * Try to keep this protected and not public to avoid confusion
	 *
	 * @throws IOException
	 * @throws InvalidMessageException
	 */
	protected abstract void parseResponse() throws InvalidMessageException, IOException;

	public abstract T getNextElement() throws InvalidMessageException, IOException;
}
