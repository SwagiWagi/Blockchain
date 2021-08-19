package main.me.therom.blockchain.abstracts;

import com.google.gson.annotations.JsonAdapter;
import main.me.therom.core.utils.ConstManager;
import main.me.therom.core.utils.adapters.ByteArrayTypeAdapter;
import main.me.therom.core.utils.adapters.CryptographicKeyTypeAdapter;
import main.me.therom.core.utils.adapters.SignatureTypeAdapter;
import main.me.therom.cryptography.HashHelper;
import main.me.therom.exceptions.HashingException;

import java.security.PublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Transaction
{
	//Used for serialization
	private final String type;

	@JsonAdapter(CryptographicKeyTypeAdapter.class)
	private final PublicKey fromKey;
	@JsonAdapter(CryptographicKeyTypeAdapter.class)
	private final PublicKey toKey;

	@JsonAdapter(ByteArrayTypeAdapter.class)
	private byte[] previousTransactionHash;
	@JsonAdapter(ByteArrayTypeAdapter.class)
	private byte[] hash;

	@JsonAdapter(SignatureTypeAdapter.class)
	private byte[] signature;
	private Instant timeStamp;

	public Transaction(String type, PublicKey fromKey, PublicKey toKey)
	{
		this.type = type;
		this.fromKey = fromKey;
		this.toKey = toKey;
		this.timeStamp = Instant.now();

		setHash();
	}

	public boolean isValid()
	{
		//Coinbase transaction
		if (Arrays.equals(HashHelper.encodePublicKey(this.fromKey), ConstManager.FROM_ADDRESS_COINBASE) &&
				this.signature == null &&
				Arrays.equals(this.previousTransactionHash, ConstManager.FIRST_TRANSACTION_IN_QUEUE_PREVIOUS_TRANSACTION_HASH) &&
				this.toKey != null &&
				this.hash != null &&
				isImplementationValid())
		{
			return true;
		}

		return HashHelper.isSignatureValid(this.getFromKey(), getSignature(), this.getHash());
	}

	public byte[] getHash()
	{
		setHash();

		return this.hash;
	}

	/**
	 * In case of building a known transaction
	 * @param hash
	 */
	public void setHash(byte[] hash)
	{
		this.hash = hash;
	}

	/**
	 * Will not take a hash parameter since the hashing is constant,
	 * the only variable in here is the specific fields that it gets from the implementor
	 */
	public void setHash()
	{
		try
		{
			//What the actual fuck
			this.hash = HashHelper.SHA256(
					(this.previousTransactionHash == null ? "" : new String(this.previousTransactionHash)) +
							(this.signature == null ? "" : new String(this.signature)) +
							new String(HashHelper.encodePublicKey(this.fromKey)) +
							new String(HashHelper.encodePublicKey(this.toKey)) +
							this.timeStamp +
							getSpecificFields());
		}
		catch (HashingException ex)
		{
			//Transaction will be invalid
			this.hash = null;
		}
	}

	public PublicKey getFromKey()
	{
		return this.fromKey;
	}

	public PublicKey getToKey()
	{
		return this.toKey;
	}

	public byte[] getPreviousHash()
	{
		return this.previousTransactionHash;
	}

	public void setPreviousHash(byte[] previousHash)
	{
		if (this.previousTransactionHash == null)
		{
			this.previousTransactionHash = previousHash;
		}
	}

	public byte[] getSignature()
	{
		return this.signature;
	}

	public void setSignature(byte[] signature)
	{
		this.signature = signature;
	}

	public Instant getTimeStamp()
	{
		return this.timeStamp;
	}

	public void setTimeStamp(Instant timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public String getType()
	{
		return this.type;
	}

	/**
	 * NOTE: DON'T JUST RETURN A WHOLE OBJECT, ONLY IT'S VALUE - IT HAS TO BE CONSTANT, ie the toString() method, objects can have runtime information
	 * @returns anything unique to this specific transaction, most of the time will just be the fields, make sure it stays the same across versions.
	 */
	public abstract String getSpecificFields();

	/**
	 * Should not be called to verify if the transaction is valid.
	 * Call isTransactionValid() instead, this is only the implementor specific details
	 *
	 * @returns is the implementors state valid
	 */
	protected abstract boolean isImplementationValid();

	/**
	 * The format: field:value
	 *
	 * @return
	 */
	public abstract HashMap<String, String> getExtraFieldsToSend();
}
