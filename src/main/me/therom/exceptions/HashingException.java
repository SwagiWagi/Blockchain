package main.me.therom.exceptions;

public class HashingException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Error hashing.";

	public HashingException()
	{
		super(message);
	}

	public HashingException(Throwable root)
	{
		super(message, root);
	}
}
