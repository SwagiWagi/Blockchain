package main.me.therom.cryptography;

import main.me.therom.core.utils.ConstManager;
import main.me.therom.exceptions.HashingException;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;

public final class HashHelper
{
	private HashHelper()
	{
	}

	public static byte[] SHA256(byte[] text) throws HashingException
	{
		return SHA256(new String(text));
	}

	public static byte[] SHA256(String text) throws HashingException
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			md.update(text.getBytes(StandardCharsets.UTF_8));
			byte[] digest = md.digest();

			return String.format("%064x", new BigInteger(1, digest)).getBytes();
		}
		catch (NoSuchAlgorithmException ex)
		{
			throw new HashingException(ex);
		}
	}

	public static byte[] sign(byte[] data, PrivateKey privateKey) throws HashingException
	{
		try
		{
			Signature signature = Signature.getInstance(ConstManager.SIGNING_ALGORITHM);

			signature.initSign(privateKey);
			signature.update(data);

			return signature.sign();
		}
		catch (Exception ex)
		{
			throw new HashingException(ex);
		}
	}

	public static boolean isSignatureValid(PublicKey publicKey, byte[] signature, byte[] data)
	{
		try
		{
			Signature signatureObject = Signature.getInstance(ConstManager.SIGNING_ALGORITHM);

			signatureObject.initVerify(publicKey);
			signatureObject.update(data);

			return signatureObject.verify(signature);
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	public static byte[] encodeSignature(byte[] signature)
	{
		return Base58.encode(signature);
	}

	public static byte[] decodeSignature(byte[] encodedSignature)
	{
		return Base58.decode(encodedSignature);
	}

	public static byte[] encodePublicKey(PublicKey publicKey)
	{
		BCECPublicKey bcecPublicKey = new BCECPublicKey((ECPublicKey) publicKey, BouncyCastleProvider.CONFIGURATION);
		return Base58.encode(bcecPublicKey.getQ().getEncoded(true));
	}

	public static PublicKey decodePublicKey(byte[] encodedKey)
	{
		try {
			ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(ConstManager.CURVE_NAME);
			ECNamedCurveSpec params = new ECNamedCurveSpec(ConstManager.CURVE_NAME, spec.getCurve(), spec.getG(), spec.getN());
			ECPoint point = ECPointUtil.decodePoint(params.getCurve(), Base58.decode(encodedKey));
			ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, params);

			return KeyFactory.getInstance(ConstManager.KEYS_ALGORITHM).generatePublic(pubKeySpec);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

}
