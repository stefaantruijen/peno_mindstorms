package bluebot.game;


import java.io.IOException;

import bluebot.io.rabbitmq.RabbitConfig;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import peno.htttp.Client;
import peno.htttp.Handler;



/**
 * 
 * @author Ruben Feyen
 */
public class Game {
	
	private Client client;
	
	
	public Game(final String gameId, final String playerId) throws IOException {
		this.client = createClient(gameId, playerId);
	}
	
	
	
	private static final Connection connect() throws IOException {
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitConfig.HOST_NAME);
		factory.setPassword(RabbitConfig.PASSWORD);
		factory.setPort(RabbitConfig.PORT);
		factory.setRequestedHeartbeat(0);
		factory.setUsername(RabbitConfig.USER_NAME);
		factory.setVirtualHost(RabbitConfig.VIRTUAL_HOST);
		return factory.newConnection();
	}
	
	private final Client createClient(final String gameId, final String playerId)
			throws IOException {
		return new Client(connect(), createHandler(), gameId, playerId);
	}
	
	protected Handler createHandler() {
		return new GameHandler();
	}
	
	public Client getClient() {
		return client;
	}
	
}
