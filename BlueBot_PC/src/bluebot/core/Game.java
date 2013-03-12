package bluebot.core;


import java.io.IOException;

import com.rabbitmq.client.Connection;

import RabbitMQCommunication.MQConnector;
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
		return MQConnector.createConnection();
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
