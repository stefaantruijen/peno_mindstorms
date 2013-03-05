package RabbitMQCommunication;


import java.io.IOException;
import java.util.Date;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;



/**
 * 
 * @author Ruben Feyen
 */
public class RabbitConnection {
	
	public static final String DEFAULT_ROUTING_KEY = "race.blauw";
	
	private Channel channel;
	private Connection connection;
	
	
	public RabbitConnection() throws IOException {
		connect();
	}
	
	
	
	private final void connect() throws IOException {
		connection = MQConnector.createConnection();
		channel = MQConnector.createChannel(connection);
	}
	
	private static final BasicProperties createProperties() {
		final BasicProperties props = new BasicProperties();
		// The body of the message is plain text
		props.setContentType("text/plain");
		// Do not make the message persistant
		props.setDeliveryMode(1);
		// Set the time of the message, otherwise the
		// receivers do not know when the message was sent
		props.setTimestamp(new Date());
		return props;
	}
	
	public void disconnect() {
		if (channel != null) {
			try {
				channel.close();
			} catch (final IOException e) {
				// ignored
			} finally {
				channel = null;
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (final IOException e) {
				// ignored
			} finally {
				connection = null;
			}
		}
	}
	
	public void registerListener(final String key, final Listener listener)
			throws IOException {
		if (channel == null) {
			throw new IOException("The connection has been closed");
		}
		
		final String queue = channel.queueDeclare().getQueue();
		channel.queueBind(queue, Config.EXCHANGE_NAME, key);
		channel.basicConsume(queue, false, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(final String consumerTag,
					final Envelope envelope, 
					final AMQP.BasicProperties properties,
					final byte[] msg) throws IOException {
				listener.onMessage(properties.getTimestamp(),
						envelope.getRoutingKey(), new String(msg));
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		});
	}
	
	public void sendMessage(final String msg) throws IOException {
		sendMessage(msg, DEFAULT_ROUTING_KEY);
	}
	
	public void sendMessage(final String msg, final String key) throws IOException {
		if (channel == null) {
			throw new IOException("The connection has been closed");
		}
		
		channel.basicPublish(Config.EXCHANGE_NAME,
				key,
				createProperties(),
				msg.getBytes());
	}
	
	
	
	
	
	
	
	
	
	
	public static interface Listener {
		
		public void onMessage(Date time, String key, String msg);
		
	}
	
}
