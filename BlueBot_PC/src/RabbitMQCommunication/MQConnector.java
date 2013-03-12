package RabbitMQCommunication;


import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;



/**
 * Standard class to connect to the AMQP server described in the Config class.
 * @author Ine & Michiel
 *
 */
public class MQConnector {

	/**
	 * Create a connection to a AMQP server using the configuration in the
	 * Config class.
	 * 
	 * @return An active connection
	 * @throws IOException
	 */
	public static Connection createConnection() throws IOException {
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Config.HOST_NAME);
		factory.setPassword(Config.PASSWORD);
		factory.setPort(Config.PORT);
		factory.setRequestedHeartbeat(0);
		factory.setUsername(Config.USER_NAME);
		factory.setVirtualHost(Config.VIRTUAL_HOST);
		return factory.newConnection();
	}

	/**
	 * Create a channel on the given connection. This method will also configure
	 * a message exchange and message queue based on the configuration in the
	 * Config class. The routing key in the config class is used to route
	 * message from the exchange to the queue.
	 * 
	 * @param conn
	 * @return The created channel.
	 * @throws IOException
	 */
	public static Channel createChannel(Connection conn) throws IOException {
		Channel channel = conn.createChannel();
		channel.exchangeDeclare(Config.EXCHANGE_NAME, "topic");

		return channel;
	}	
}
