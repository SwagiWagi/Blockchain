package main.me.therom.network.server;

import main.me.therom.core.Logger;
import main.me.therom.core.PeersIOManager;
import main.me.therom.core.utils.ConstManager;
import main.me.therom.exceptions.InvalidMessageException;
import main.me.therom.exceptions.ServerInitializationException;
import main.me.therom.network.PeerConnection;
import main.me.therom.network.peers.ClientPeer;
import main.me.therom.network.peers.FullNodePeer;
import main.me.therom.network.peers.Peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private final transient Logger logger;
	private static ServerSocket SERVER;
	private final transient PeersIOManager peersIOManager;

	public Server(Logger logger, PeersIOManager peersIOManager)
	{
		this.logger = logger;
		this.peersIOManager = peersIOManager;
	}

	public void startServer() throws ServerInitializationException
	{
		if (SERVER != null)
		{
			return;
		}

		try
		{
			SERVER = new ServerSocket(ConstManager.SERVER_PORT);
		}
		catch (IOException ex)
		{
			throw new ServerInitializationException(ex);
		}

		waitForConnections();
	}

	public void waitForConnections()
	{
		while (true)
		{
			try
			{
				//Async call to avoid hanging - however, I'm still not sure how much calls can this server can handle
				receiveConnection(SERVER.accept());
			}
			catch (IOException ex)
			{
				this.logger.log("An exception has been thrown while accepting a peer.");
				this.logger.log(ex.getMessage());
			}
		}
	}

	//Well, kind of async isn't it?
	private void receiveConnection(Socket connection)
	{
		new Thread(() ->
		{
			try
			{
				PeerConnection request = new PeerConnection(connection.getInputStream());

				Peer peer;

				switch (request.getPeerType())
				{
					default:
						peer = new ClientPeer(connection);
						break;
					case 'f':
						peer = new FullNodePeer(connection);
						break;
				}

				this.peersIOManager.addPeer(peer);

				new Thread(new ServerConnectionThread(this.logger, this.peersIOManager, peer, request.getRequestType())).start();
			}
			catch (IOException ex)
			{
				this.logger.log("An exception has been thrown while accepting a peer.");
				this.logger.log(ex.getMessage());
			}
			catch (InvalidMessageException ex)
			{
				this.logger.log("Received an invalid message from peer.");
				this.logger.log(ex.getMessage());
			}
		}).start();
	}

}
