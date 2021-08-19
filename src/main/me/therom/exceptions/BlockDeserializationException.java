package main.me.therom.exceptions;

public class BlockDeserializationException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Error deserializing the block, check the BlockDeserializer.class/related classes for missing versions.";

	public BlockDeserializationException()
	{
		super(message);
	}

	public BlockDeserializationException(Throwable root)
	{
		super(message, root);
	}
}
