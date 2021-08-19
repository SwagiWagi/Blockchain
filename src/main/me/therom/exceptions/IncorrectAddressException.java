package main.me.therom.exceptions;

public class IncorrectAddressException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "The addresses does not match.";

	public IncorrectAddressException()
	{
		super();
	}

	public IncorrectAddressException(Throwable root)
	{
		super(message, root);
	}
}
