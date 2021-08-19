package main.me.therom.exceptions;

public class BlockMiningException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Error hashing/mining the block.";

	public BlockMiningException()
	{
		super(message);
	}

	public BlockMiningException(Throwable root)
	{
		super(message, root);
	}
}
