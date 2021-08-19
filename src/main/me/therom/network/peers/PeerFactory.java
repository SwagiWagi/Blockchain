package main.me.therom.network.peers;

public class PeerFactory
{
	private PeerFactory()
	{
	}

	public static Peer getPeer(char c, String ip, int port)
	{
		switch (c)
		{
			default:
			case 'c':
				return new ClientPeer(ip, port);
			case 'f':
				return new FullNodePeer(ip, port);
		}
	}
}
