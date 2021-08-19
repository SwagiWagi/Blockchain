package main.me.therom.exceptions;

public class BlockLockedException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Block is locked, it was already mined.";

	public BlockLockedException()
	{
		super(message);
	}

	public BlockLockedException(Throwable root)
	{
		super(message, root);
	}
}
