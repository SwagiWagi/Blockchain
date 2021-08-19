package main.me.therom.cryptography;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * The getters are public for future implementors use
 */
public class Keys
{
	private final PrivateKey privateKey;

	private final PublicKey publicKey;

	public Keys(PrivateKey privateKey, PublicKey publicKey)
	{
		this.privateKey = privateKey;

		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey()
	{
		return this.privateKey;
	}

	public PublicKey getPublicKey()
	{
		return this.publicKey;
	}

}
