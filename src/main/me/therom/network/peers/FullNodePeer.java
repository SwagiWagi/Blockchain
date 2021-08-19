package main.me.therom.network.peers;

import java.net.Socket;

/**
 * Will be used in the future.
 *
 * @author SwagiWagi
 */
public class FullNodePeer extends Peer
{
	public FullNodePeer(String ip, int port)
	{
		super('f', ip, port);
	}

	public FullNodePeer(Socket socket)
	{
		super('f', socket);
	}

}
