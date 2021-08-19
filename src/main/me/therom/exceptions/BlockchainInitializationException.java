package main.me.therom.exceptions;

public class BlockchainInitializationException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Blockchain Initialization failed.";

	public BlockchainInitializationException()
	{
		super(message);
	}

	public BlockchainInitializationException(Throwable root)
	{
		super(message, root);
	}
}
