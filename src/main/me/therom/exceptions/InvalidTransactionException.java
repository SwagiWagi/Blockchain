package main.me.therom.exceptions;

public class InvalidTransactionException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "The transaction is invalid.";

	public InvalidTransactionException()
	{
		super(message);
	}

	public InvalidTransactionException(Throwable root)
	{
		super(message, root);
	}
}
