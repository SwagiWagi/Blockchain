package main.me.therom.network;

import main.me.therom.exceptions.InvalidMessageException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Peers DB structure:
 * 7 bytes: = "GETPEERS"
 * Peers length structure:
 * 13 bytes: = "GETPEERSLENGTH"
 * Blockchain DB structure:
 * 7 bytes: = "GETCHAIN"
 * Chain length structure:
 * 13 bytes: = "GETCHAINLENGTH"
 *
 * @author SwagiWagi
 */
public final class PeerConnection
{
	private final RequestType requestType;
	private final char peerType;

	public PeerConnection(InputStream stream) throws InvalidMessageException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

		String peerTypeLine = reader.readLine();

		if (peerTypeLine.length() > 1)
		{
			throw new InvalidMessageException();
		}

		peerType = peerTypeLine.charAt(0);

		String requestType = reader.readLine();

		if (requestType.equals(RequestType.GETPEERS.toString())
				|| requestType.equals(RequestType.GETPEERSLENGTH.toString())
				|| requestType.equals(RequestType.GETCHAIN.toString())
				|| requestType.equals(RequestType.GETCHAINLENGTH.toString()))
		{
			this.requestType = RequestType.valueOf(requestType);
		}
		else
		{
			throw new InvalidMessageException();
		}
	}

	public RequestType getRequestType()
	{
		return this.requestType;
	}

	public char getPeerType()
	{
		return this.peerType;
	}

	public enum RequestType
	{
		GETPEERS,
		GETPEERSLENGTH,
		GETCHAIN,
		GETCHAINLENGTH
	}

}