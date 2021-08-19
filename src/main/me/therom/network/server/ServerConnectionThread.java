package main.me.therom.network.server;

import main.me.therom.core.Logger;
import main.me.therom.core.PeersIOManager;
import main.me.therom.network.PeerConnection;
import main.me.therom.network.peers.Peer;

import java.io.IOException;

public class ServerConnectionThread implements Runnable
{
	private final transient Logger logger;
	private final transient PeersIOManager peersIOManager;
	private final Peer peer;
	private final PeerConnection.RequestType requestType;

	public ServerConnectionThread(Logger logger, PeersIOManager peersIOManager, Peer peer, PeerConnection.RequestType requestType)
	{
		this.logger = logger;
		this.peersIOManager = peersIOManager;
		this.peer = peer;
		this.requestType = requestType;
	}

	@Override
	public void run()
	{
		try
		{
			switch (this.requestType)
			{
				case GETPEERS:
					this.peersIOManager.writePeersToPeer(this.peer);
					break;
				case GETPEERSLENGTH:
					this.peersIOManager.writePeersLengthToPeer(this.peer);
					break;
				case GETCHAIN:
					this.peersIOManager.writeBlockchainToPeer(this.peer);
					break;
				case GETCHAINLENGTH:
					this.peersIOManager.writeBlockchainLengthToPeer(this.peer);
					break;
			}
		}
		catch (IOException ex)
		{
			try
			{
				this.logger.log("An exception has been thrown while writing to peer.");
				this.logger.log(ex.getMessage());
				this.logger.log("Trying to close socket...");
				this.peer.getSocket().close();
				this.logger.log("Successfully closed socket.");
			}
			catch (IOException e)
			{
				this.logger.log("An exception has been thrown while trying to close socket.");
				this.logger.log(e.getMessage());
			}
		}
	}
}
