package main.me.therom.exceptions;

public class FileExistsException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "A file with the same hash already exists.";

	public FileExistsException()
	{
		super(message);
	}

	public FileExistsException(Throwable root)
	{
		super(message, root);
	}
}
