package main.me.therom.network.peers;

import java.net.Socket;

/**
 * For use by the local server
 * The client peer is a remote machine
 *
 * @author SwagiWagi
 */
public class ClientPeer extends Peer
{
	public ClientPeer(String ip, int port)
	{
		super('c', ip, port);
	}

	public ClientPeer(Socket socket)
	{
		super('c', socket);
	}
}