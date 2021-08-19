package main.me.therom.core.utils;

import java.util.Map;

/**
 * You can call this class a config class
 *
 * @author SwagiWagi
 */
public class ConstManager
{
	public static final double BLOCK_MINING_REWARD_AMOUNT = 100;
	public static final int BLOCK_MINING_DIFFICULTY = 2;
	public static final String KEYS_ALGORITHM = "EC";
	public static final String SIGNING_ALGORITHM = "SHA256withECDSA";
	public static final String CURVE_NAME = "secp256k1";
	public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
	public static final String PROOF_OF_WORK_PREFIX = "0";
	public static final byte[] FROM_ADDRESS_COINBASE = "287LckL8T7jEusoeV7dwu94hut96e5dxXSn9x1gHTkVrw".getBytes();
	public static final byte[] FIRST_TRANSACTION_IN_QUEUE_PREVIOUS_TRANSACTION_HASH = { '0' };
	public static final byte[] GENESIS_BLOCK_PREVIOUS_HASH = { '0', '0' };
	public static final Map<String, Integer> GENESIS_PEERS = Map.of("1", 1);
	public static final String GET_PEERS_MESSAGE = "GETPEERS";
	public static final String GET_PEERS_LENGTH_MESSAGE = "GETPEERSLENGTH";
	public static final String GET_CHAIN_MESSAGE = "GETCHAIN";
	public static final String GET_CHAIN_LENGTH_MESSAGE = "GETCHAINLENGTH";
	public static final int SERVER_PORT = 16482;

	private ConstManager()
	{
	}
}
