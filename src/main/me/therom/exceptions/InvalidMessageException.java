package main.me.therom.exceptions;

public class InvalidMessageException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Invalid message.";

	public InvalidMessageException()
	{
		super(message);
	}

	public InvalidMessageException(Throwable root)
	{
		super(message, root);
	}
}
