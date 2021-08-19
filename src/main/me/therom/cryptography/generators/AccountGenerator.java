package main.me.therom.cryptography.generators;

import main.me.therom.core.utils.ConstManager;
import main.me.therom.cryptography.Account;
import main.me.therom.exceptions.WalletInitializationException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class AccountGenerator
{
	private final static AccountGenerator instance = new AccountGenerator();

	private AccountGenerator()
	{
	}

	public static AccountGenerator getInstance()
	{
		return instance;
	}

	public Account generate() throws WalletInitializationException
	{
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ConstManager.KEYS_ALGORITHM);
			ECGenParameterSpec ecSpec = new ECGenParameterSpec(ConstManager.CURVE_NAME);
			SecureRandom random = SecureRandom.getInstance(ConstManager.SECURE_RANDOM_ALGORITHM);
			keyGen.initialize(ecSpec, random);
			KeyPair pair = keyGen.generateKeyPair();

			return new Account(pair.getPrivate(), pair.getPublic());
		}
		catch (Exception ex)
		{
			throw new WalletInitializationException(ex);
		}
	}
}
