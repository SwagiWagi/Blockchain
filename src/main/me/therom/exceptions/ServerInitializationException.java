package main.me.therom.exceptions;

public class ServerInitializationException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Server Initialization failed.";

	public ServerInitializationException()
	{
		super(message);
	}

	public ServerInitializationException(Throwable root)
	{
		super(message, root);
	}
}
