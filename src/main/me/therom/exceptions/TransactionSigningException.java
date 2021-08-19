package main.me.therom.exceptions;

public class TransactionSigningException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Failed to sign transaction.";

	public TransactionSigningException()
	{
		super(message);
	}

	public TransactionSigningException(Throwable root)
	{
		super(message, root);
	}
}
