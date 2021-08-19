package main.me.therom.exceptions;

public class WalletInitializationException extends Exception
{
	/**
	 * Not using serialization nor planning to use
	 */
	private static final long serialVersionUID = 1L;

	private final static String message = "Wallet Initialization failed.";

	public WalletInitializationException()
	{
		super(message);
	}

	public WalletInitializationException(Throwable root)
	{
		super(message, root);
	}
}
