package main.me.therom.network.peers;

import main.me.therom.core.utils.ConstManager;

import java.io.IOException;
import java.net.Socket;

/**
 * Making it future proof for further functionality such as full nodes
 * This class will not contain a getPeers()/getBlockchain() methods to avoid OutOfMemoryError Exception
 * This class will perform the IO operations over the network + local disk
 *
 * @author SwagiWagi
 */
public abstract class Peer extends DBPeer
{
	private transient Socket socket;

	public Peer(char type, String ip, int port)
	{
		super(type, ip, port);
	}

	public Peer(char type, Socket socket)
	{
		super(type, socket.getInetAddress().getHostAddress(), ConstManager.SERVER_PORT);

		this.socket = socket;
	}

	public Socket getSocket() throws IOException
	{
		if (this.socket == null)
		{
			this.socket = new Socket(getIp(), getPort());
		}

		return this.socket;
	}

}
