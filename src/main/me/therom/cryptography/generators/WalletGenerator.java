package main.me.therom.cryptography.generators;

import main.me.therom.cryptography.Account;
import main.me.therom.cryptography.Wallet;
import main.me.therom.exceptions.WalletInitializationException;

public class WalletGenerator
{
	private final static WalletGenerator instance = new WalletGenerator();

	private WalletGenerator()
	{
	}

	public static WalletGenerator getInstance()
	{
		return instance;
	}

	public Wallet generate() throws WalletInitializationException
	{
		Account account = AccountGenerator.getInstance().generate();

		return new Wallet(account);
	}

}
