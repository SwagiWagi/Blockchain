package main.me.therom.db.managers;

import main.me.therom.core.Logger;
import main.me.therom.network.peers.DBPeer;
import main.me.therom.network.peers.Peer;
import main.me.therom.network.peers.PeerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PeersDBManager
{
	private final Logger logger;
	private final File file;

	public PeersDBManager(Logger logger, String fileLocation)
	{
		this.logger = logger;
		this.file = new File(fileLocation);

		writeFileWithHeader();
	}

	//Will not write header if file exists
	private void writeFileWithHeader()
	{
		try
		{
			if (!this.file.exists())
			{
				if (!this.file.getParentFile().mkdirs() || !this.file.createNewFile())
				{
					throw new IOException();
				}

				try (FileWriter writer = new FileWriter(this.file, false))
				{
					writer.write("Ip,Port,Type");
				}
			}
		}
		catch (IOException ex)
		{
			this.logger.log("An exception has been thrown while trying to write to peers DB file.");
			this.logger.log(ex.getMessage());
		}
	}

	private boolean fileContainsHeader() throws IOException
	{
		try
		{
			try (BufferedReader reader = new BufferedReader(new FileReader(this.file)))
			{
				return reader.readLine().equalsIgnoreCase("Ip,Port,Type");
			}
		}
		catch (IOException ex)
		{
			this.logger.log("An exception has been thrown while trying to read from peers DB file.");
			this.logger.log(ex.getMessage());

			throw ex;
		}
	}

	public File getFile()
	{
		return this.file;
	}

	public void addPeer(Peer peer) throws IOException
	{
		try
		{
			if (!dbContainsPeer(peer))
			{
				try (FileWriter writer = new FileWriter(this.file, true))
				{
					writer.write("\n" + peer.getIp() + "," + peer.getPort() + "," + peer.getType());
				}
			}
		}
		catch (IOException ex)
		{
			this.logger.log("An exception has been thrown while trying to write to peers DB file.");
			this.logger.log(ex.getMessage());

			throw ex;
		}
	}

	//No problem loading into the memory as each peer is 21 bytes without the java object
	public List<Peer> getPeers() throws IOException
	{
		try
		{
			List<Peer> peers = new ArrayList<>();

			try (BufferedReader reader = new BufferedReader(new FileReader(this.file)))
			{
				if (fileContainsHeader())
				{
					reader.readLine();
				}

				String line = reader.readLine();

				while (line != null)
				{
					String ip;
					int port;
					char type;

					ip = line.substring(0, line.indexOf(","));
					line = line.substring(ip.length() + 1);

					port = Integer.parseInt(line.substring(0, line.indexOf(",")));
					line = line.substring(String.valueOf(port).length() + 1);

					type = line.charAt(0);

					peers.add(PeerFactory.getPeer(type, ip, port));

					line = reader.readLine();
				}
			}

			return peers;
		}
		catch (IOException ex)
		{
			this.logger.log("An exception has been thrown while trying to read from peers DB file.");
			this.logger.log(ex.getMessage());

			throw ex;
		}
	}

	public boolean dbContainsPeer(DBPeer peer) throws IOException
	{
		try
		{
			return
					Files.lines(this.file.toPath())
							.filter(line -> line.contains(peer.getIp()))
							.filter(line -> line.contains(String.valueOf(peer.getPort())))
							.anyMatch(line -> line.contains(String.valueOf(peer.getType())));
		}
		catch (IOException ex)
		{
			this.logger.log("An exception has been thrown while trying to read from peers DB file.");
			this.logger.log(ex.getMessage());

			throw ex;
		}
	}

	public int getLength() throws IOException
	{
		try
		{
			int lineCounter = 0;

			try (BufferedReader reader = new BufferedReader(new FileReader(this.file)))
			{
				while (reader.readLine() != null)
				{
					lineCounter++;
				}
			}

			//Opening again to avoid having another condition inside the first loop
			try (BufferedReader reader = new BufferedReader(new FileReader(this.file)))
			{
				if (reader.readLine().equalsIgnoreCase("Ip,Port,Type"))
				{
					return lineCounter - 1;
				}

				return lineCounter;
			}
		}
		catch (IOException ex)
		{
			this.logger.log("An exception has been thrown while trying to read from peers DB file.");
			this.logger.log(ex.getMessage());

			throw ex;
		}
	}

}
