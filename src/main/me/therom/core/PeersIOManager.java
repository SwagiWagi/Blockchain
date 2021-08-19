package main.me.therom.core;

import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.blockchain.abstracts.Transaction;
import main.me.therom.core.utils.ConstManager;
import main.me.therom.core.utils.JsonHelper;
import main.me.therom.cryptography.HashHelper;
import main.me.therom.db.loopers.BlockLooper;
import main.me.therom.db.managers.BlockchainDBManager;
import main.me.therom.db.managers.PeersDBManager;
import main.me.therom.exceptions.InvalidMessageException;
import main.me.therom.network.peers.FullNodePeer;
import main.me.therom.network.peers.Peer;
import main.me.therom.network.responses.BlockchainLengthResponse;
import main.me.therom.network.responses.BlockchainResponse;
import main.me.therom.network.responses.PeersLengthResponse;
import main.me.therom.network.responses.PeersResponse;

import java.io.IOException;
import java.util.Map.Entry;

import static main.me.therom.core.utils.NetworkHelper.writeLineToPeer;

public class PeersIOManager
{
	private final Logger logger;
	private final JsonHelper jsonHelper;
	private final BlockchainDBManager blockchainDBManager;
	private final PeersDBManager peersDBManager;

	public PeersIOManager(Logger logger, JsonHelper jsonHelper, BlockchainDBManager blockchainDBManager, PeersDBManager peersDBManager)
	{
		this.jsonHelper = jsonHelper;
		this.logger = logger;
		this.blockchainDBManager = blockchainDBManager;
		this.peersDBManager = peersDBManager;
	}

	/**
	 * Writes the longest blockchain to the disk (overwrites)
	 *
	 * @throws IOException
	 * @throws InvalidMessageException
	 */
	public void writeLongestBlockchainFromGenesisPeers() throws IOException, InvalidMessageException
	{
		FullNodePeer currentPeer;

		Peer longestBlockchainPeer = null;

		for (Entry<String, Integer> genesisPeer : ConstManager.GENESIS_PEERS.entrySet())
		{
			currentPeer = new FullNodePeer(genesisPeer.getKey(), genesisPeer.getValue());
			if (longestBlockchainPeer == null ||
					getBlockchainLength(currentPeer) > getBlockchainLength(longestBlockchainPeer))
			{
				longestBlockchainPeer = currentPeer;
			}
		}

		getBlockchainAndReplaceCurrent(longestBlockchainPeer);
	}

	public void getBlockchainAndReplaceCurrent(Peer peer) throws IOException, InvalidMessageException
	{
		writeLineToPeer(peer, ConstManager.GET_CHAIN_MESSAGE);

		try (BlockchainResponse response = new BlockchainResponse(jsonHelper, peer.getSocket().getInputStream()))
		{
			this.blockchainDBManager.deleteBlockchain();

			for (int i = 0; i < response.getAmount(); i++)
			{
				this.blockchainDBManager.addBlock(response.getNextElement());
			}
		}
	}

	public int getBlockchainLength(Peer peer) throws IOException, InvalidMessageException
	{
		writeLineToPeer(peer, ConstManager.GET_CHAIN_LENGTH_MESSAGE);

		try (BlockchainLengthResponse response = new BlockchainLengthResponse(this.jsonHelper, peer.getSocket().getInputStream()))
		{
			return response.getNextElement();
		}
	}

	public void getPeersAndAddToDB(Peer peer) throws IOException, InvalidMessageException
	{
		writeLineToPeer(peer, ConstManager.GET_PEERS_MESSAGE);

		try (PeersResponse response = new PeersResponse(this.jsonHelper, peer.getSocket().getInputStream()))
		{
			for (int i = 0; i < response.getAmount(); i++)
			{
				addPeer(response.getNextElement());
			}
		}
	}

	public int getPeersLength(Peer peer) throws IOException, InvalidMessageException
	{
		writeLineToPeer(peer, ConstManager.GET_PEERS_LENGTH_MESSAGE);

		try (PeersLengthResponse response = new PeersLengthResponse(this.jsonHelper, peer.getSocket().getInputStream()))
		{
			return response.getNextElement();
		}
	}

	/**
	 * Will get the peers DB from the genesis peers
	 *
	 * @throws InvalidMessageException
	 */
	public void getPeersFromGenesisPeersAndAddToDB() throws InvalidMessageException
	{
		FullNodePeer host;

		for (Entry<String, Integer> genesisPeer : ConstManager.GENESIS_PEERS.entrySet())
		{
			host = new FullNodePeer(genesisPeer.getKey(), genesisPeer.getValue());

			try
			{
				getPeersAndAddToDB(host);
			}
			catch (IOException ex)
			{
				this.logger.log("Exception while getting peers from genesis peers and adding them to the DB.");
				this.logger.log(ex.getMessage());
			}
		}
	}

	public void addPeer(Peer peer) throws IOException
	{
		this.peersDBManager.addPeer(peer);
	}

	public void writePeersToPeer(Peer peer) throws IOException
	{
		writeLineToPeer(peer, this.blockchainDBManager.getBlockchainVersion());
		writeLineToPeer(peer, this.peersDBManager.getLength());

		for (Peer currentPeer : this.peersDBManager.getPeers())
		{
			System.out.println(currentPeer.getIp());

			writeLineToPeer(peer, currentPeer.getIp());
			writeLineToPeer(peer, currentPeer.getPort());
			writeLineToPeer(peer, currentPeer.getType());
		}
	}

	public void writePeersLengthToPeer(Peer peer) throws IOException
	{
		writeLineToPeer(peer, this.blockchainDBManager.getBlockchainVersion());
		writeLineToPeer(peer, this.peersDBManager.getLength());
	}

	public void writeBlockchainToPeer(Peer peer) throws IOException
	{
		writeLineToPeer(peer, this.blockchainDBManager.getBlockchainVersion());
		writeLineToPeer(peer, this.blockchainDBManager.getBlockchainLength());

		BlockLooper blockLooper = this.blockchainDBManager.generateNewBlockLooper();

		for (int i = 0; i < blockLooper.getLength(); i++)
		{
			Block block = blockLooper.getNextElement();

			writeLineToPeer(peer, block.getVersion());
			writeLineToPeer(peer, block.getHeader().getVersion());
			writeLineToPeer(peer, block.getHeader().getHeight());
			writeLineToPeer(peer, block.getHeader().getPreviousBlockHash());
			writeLineToPeer(peer, block.getHeader().getHash());
			writeLineToPeer(peer, block.getHeader().getMerkleRoot());
			writeLineToPeer(peer, block.getHeader().getDifficulty());
			writeLineToPeer(peer, block.getHeader().getTimeStamp().toString());
			writeLineToPeer(peer, block.getHeader().getRewardAmount());
			writeLineToPeer(peer, block.getHeader().getNonce());
			writeLineToPeer(peer, HashHelper.encodePublicKey(block.getHeader().getMinerPublicKey()));

			writeLineToPeer(peer, block.getBody().getVersion());

			for (int k = 0; k < block.getBody().getTransactions().size(); k++)
			{
				Transaction transaction = block.getBody().getTransactions().get(k);

				writeLineToPeer(peer, transaction.getType());
				writeLineToPeer(peer, transaction.getPreviousHash());
				writeLineToPeer(peer, transaction.getHash());
				writeLineToPeer(peer, transaction.getSignature());
				writeLineToPeer(peer, transaction.getFromKey().getEncoded());
				writeLineToPeer(peer, transaction.getToKey().getEncoded());
				writeLineToPeer(peer, transaction.getTimeStamp().toString());

				for (Entry<String, String> entry : transaction.getExtraFieldsToSend().entrySet())
				{
					writeLineToPeer(peer, entry.getKey() + ":" + entry.getValue());
				}

				writeLineToPeer(peer, "ENDT");
			}

			writeLineToPeer(peer, "ENDB");
		}
	}

	public void writeBlockchainLengthToPeer(Peer peer) throws IOException
	{
		writeLineToPeer(peer, this.blockchainDBManager.getBlockchainVersion());
		writeLineToPeer(peer, this.blockchainDBManager.getBlockchainLength());
	}
}
