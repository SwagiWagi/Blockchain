package main.me.therom.cryptography;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet extends Account
{
	public Wallet(PrivateKey privateKey, PublicKey publicKey)
	{
		super(privateKey, publicKey);
	}

	public Wallet(Account account)
	{
		this(account.getPrivateKey(), account.getPublicKey());
	}

}
