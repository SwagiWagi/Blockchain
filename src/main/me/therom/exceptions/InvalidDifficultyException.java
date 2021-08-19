package main.me.therom.exceptions;

public class InvalidDifficultyException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Difficulty must be greater than 0.";

	public InvalidDifficultyException()
	{
		super(message);
	}

	public InvalidDifficultyException(Throwable root)
	{
		super(message, root);
	}
}
