package main.me.therom.core.utils;

import main.me.therom.network.peers.Peer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class NetworkHelper
{
	private NetworkHelper()
	{
	}

	public static void writeLineToPeer(Peer peer, double message) throws IOException
	{
		writeLineToPeer(peer, String.valueOf(message));
	}

	public static void writeLineToPeer(Peer peer, int message) throws IOException
	{
		writeLineToPeer(peer, String.valueOf(message));
	}

	public static void writeLineToPeer(Peer peer, byte[] message) throws IOException
	{
		writeLineToPeer(peer, new String(message));
	}

	public static void writeLineToPeer(Peer peer, String message) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(peer.getSocket().getOutputStream(), StandardCharsets.UTF_8));

		writer.write(message + "\n");
		writer.flush();
	}
}
