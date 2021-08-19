package main.me.therom.network.peers;

/**
 * Represents the most low level kind of peer, the minimum that is needed to represent a peer in the DB.
 *
 * @author SwagiWagi
 */
public class DBPeer
{
	private final char type;
	private final String ip;
	private final int port;

	public DBPeer(char type, String ip, int port)
	{
		this.type = type;
		this.ip = ip;
		this.port = port;
	}

	public char getType()
	{
		return this.type;
	}

	public String getIp()
	{
		return this.ip;
	}

	public int getPort()
	{
		return this.port;
	}

	public enum TYPE
	{
		ClientPeer,
		FullNodePeer
	}
}
