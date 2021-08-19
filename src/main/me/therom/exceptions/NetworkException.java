package main.me.therom.exceptions;

public class NetworkException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "A network exception has occurred.";

	public NetworkException()
	{
		super(message);
	}

	public NetworkException(Throwable root)
	{
		super(message, root);
	}
}
