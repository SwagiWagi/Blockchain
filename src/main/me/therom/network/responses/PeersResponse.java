package main.me.therom.network.responses;

import main.me.therom.core.utils.JsonHelper;
import main.me.therom.exceptions.InvalidMessageException;
import main.me.therom.network.peers.Peer;
import main.me.therom.network.peers.PeerFactory;

import java.io.IOException;
import java.io.InputStream;

public class PeersResponse extends Response<Peer>
{
	public PeersResponse(JsonHelper jsonHelper, InputStream stream) throws InvalidMessageException, IOException
	{
		super(jsonHelper, stream);
	}

	/**
	 * A response will be structured as the following:
	 * X + 2 bytes: Version number + line delimiter (\n)
	 * X + 2 bytes: Number of peers that will be Transferred + line delimiter (\n)
	 * <p>
	 * (loop) Peers:
	 * X + 5 bytes: IP + line delimiter (\n)
	 * X + 2 bytes: Port + line delimiter (\n)
	 * X + 2 bytes: Type of peer ("c" (client)/"f" (full node)) + line delimiter (\n)
	 * 4 bytes: 2 ending dots (marks the end of the peers) + line delimiter (\n)
	 *
	 * @throws InvalidMessageException
	 * @throws IOException
	 */
	@Override
	protected void parseResponse() throws InvalidMessageException, IOException
	{
		setVersion(stream.readLine());

		setAmount(stream.readLine());
	}

	@Override
	public Peer getNextElement() throws InvalidMessageException, IOException
	{
		String ip = stream.readLine();

		String port = stream.readLine();

		char typeOfPeer;

		try
		{
			typeOfPeer = (char) Integer.parseInt(stream.readLine());
		}
		catch (Exception ex)
		{
			throw new InvalidMessageException(ex);
		}

		try
		{
			return PeerFactory.getPeer(typeOfPeer, ip, Integer.parseInt(port));
		}
		catch (Exception ex)
		{
			throw new InvalidMessageException(ex);
		}
	}

}