package main.me.therom.cryptography;

import main.me.therom.blockchain.abstracts.Transaction;
import main.me.therom.exceptions.IncorrectAddressException;
import main.me.therom.exceptions.TransactionSigningException;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Account extends Keys
{
	public Account(PrivateKey privateKey, PublicKey publicKey)
	{
		super(privateKey, publicKey);
	}

	public void signTransaction(Transaction transaction) throws TransactionSigningException
	{
		try
		{
			if (!(transaction.getFromKey().equals(this.getPublicKey())))
			{
				throw new IncorrectAddressException();
			}

			transaction.setSignature(HashHelper.sign(transaction.getHash(), this.getPrivateKey()));
		}
		catch (Exception ex)
		{
			throw new TransactionSigningException(ex);
		}
	}
}
