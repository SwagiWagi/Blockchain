package test.me.therom;

import main.me.therom.blockchain.abstracts.Block;
import main.me.therom.blockchain.abstracts.Blockchain;
import main.me.therom.blockchain.abstracts.Transaction;
import main.me.therom.blockchain.builders.PaymentBuilder;
import main.me.therom.blockchain.implementations.BlockchainV1;
import main.me.therom.core.Logger;
import main.me.therom.core.PeersIOManager;
import main.me.therom.core.utils.JsonHelper;
import main.me.therom.cryptography.Wallet;
import main.me.therom.cryptography.generators.WalletGenerator;
import main.me.therom.db.managers.BlockchainDBManager;
import main.me.therom.db.managers.PeersDBManager;
import main.me.therom.exceptions.*;
import main.me.therom.network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class Tests
{
	private Logger logger;
	private JsonHelper jsonHelper;

	@BeforeEach
	public void init()
	{
		this.logger = new Logger()
		{
			@Override
			public void log(String log)
			{
				System.out.println(log);
			}

			@Override
			public void log(Exception ex)
			{
				System.out.println(ex.toString());
			}
		};

		this.jsonHelper = new JsonHelper(this.logger);
	}

	@Test
	public void startServer() throws IOException, WalletInitializationException, BlockchainInitializationException, BlockLockedException, HashingException, ServerInitializationException
	{
		PeersDBManager peersDBManager = new PeersDBManager(this.logger, "E:\\WorkSpaces\\Java\\Blockchain\\else\\Peers\\Peers.csv");

		BlockchainDBManager blockchainDBManager = new BlockchainDBManager(this.logger, this.jsonHelper, "E:\\WorkSpaces\\Java\\Blockchain\\else\\Blocks");

		Wallet wallet = WalletGenerator.getInstance().generate();

		blockchainDBManager.deleteBlockchain();

		Blockchain blockchain = new BlockchainV1(jsonHelper, blockchainDBManager);

		Block genesisBlock = blockchain.createGenesisBlock(wallet.getPublicKey());

		blockchain.addBlock(genesisBlock);

		PeersIOManager peersIOManager = new PeersIOManager(this.logger, jsonHelper, blockchainDBManager, peersDBManager);

		Server server = new Server(this.logger, peersIOManager);

		server.startServer();
	}

	//TODO Fix sometimes blockchain valid sometimes not - we are here
	//TODO Fix spending without money
	//TODO Transaction queue implementation over peers and all of this
	@Test
	public void deleteBlockchainCreateGenesisBlockAndValidate() throws WalletInitializationException, IOException, HashingException, BlockLockedException, BlockchainInitializationException, TransactionSigningException
	{
		JsonHelper jsonHelper = new JsonHelper(this.logger);

		BlockchainDBManager blockchainDBManager = new BlockchainDBManager(this.logger, this.jsonHelper, "E:\\WorkSpaces\\Java\\Blockchain\\else\\Blocks");

		Wallet wallet = WalletGenerator.getInstance().generate();

		blockchainDBManager.deleteBlockchain();

		Blockchain blockchain = new BlockchainV1(this.jsonHelper, blockchainDBManager);

		Block genesisBlock = blockchain.createGenesisBlock(wallet.getPublicKey());

		blockchain.addBlock(genesisBlock);

		Wallet wallet2 = WalletGenerator.getInstance().generate();

		Transaction transaction = new PaymentBuilder(wallet2.getPublicKey(), wallet.getPublicKey(), 1000.0).buildTransaction();

		wallet2.signTransaction(transaction);

		blockchain.addTransactionToQueue(transaction);

		blockchain.addBlock(blockchain.mineTransactionsIntoBlock(wallet.getPublicKey()));

		assertTrue(blockchain.isChainValid());
	}

}
