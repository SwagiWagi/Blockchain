package main.me.therom.exceptions;

public class ServerException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Something went wrong.";

	public ServerException()
	{
		super(message);
	}

	public ServerException(Throwable root)
	{
		super(message, root);
	}
}
