package main.me.therom.exceptions;

public class PeerException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "The peer had an exception.";

	public PeerException()
	{
		super(message);
	}

	public PeerException(Throwable root)
	{
		super(message, root);
	}
}
