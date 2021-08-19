package main.me.therom.exceptions;

public class InvalidBlockException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Block is invalid, might you be on the wrong chain?";

	public InvalidBlockException()
	{
		super(message);
	}

	public InvalidBlockException(Throwable root)
	{
		super(message, root);
	}
}
