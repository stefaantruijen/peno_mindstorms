package bluebot.io.rabbitmq;


import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;



/**
 * Provides utilities for interacting with the RabbitMQ server
 * 
 * @author Ine & Michiel
 */
public class RabbitMQ implements RabbitConfig {
	
	public static Connection connect() throws IOException {
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST_NAME);
		factory.setPassword(PASSWORD);
		factory.setPort(PORT);
		factory.setRequestedHeartbeat(0);
		factory.setUsername(USER_NAME);
		factory.setVirtualHost(VIRTUAL_HOST);
		return factory.newConnection();
	}
	
}
