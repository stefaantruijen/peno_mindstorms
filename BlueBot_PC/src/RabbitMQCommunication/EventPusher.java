package RabbitMQCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * A simple message pusher class. It will connect to the queue declared in the Config class.
 * Use sendMQMessage(String message) to send messages to the queue.
 * @author Ine & Michiel
 *
 */
public class EventPusher {
	
	private Connection conn = null;
	private Channel channel = null;
	private String teamName = null;
	private String routingKey = null;
	private boolean racing = false;
	
//	public static void main(String[] args) {
//		EventPusher ep = new EventPusher();
//		ep.run();
//	}
	
	public EventPusher() {
		run();
	}
	
	/**
	 * Sets up a connection to the server defined in the Config class.
	 */
	public void run() {
		try {
			setup();
		} catch (IOException e) {
			System.err.println("Unable to setup program and connect to AMQP server");
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
	
	/**
	 * Set up the connection to the server and ask a team name to derive the
	 * routing key. 
	 * 
	 * @throws IOException
	 */
	//TODO Assign teamname automatic / in GUI?
	public void setup() throws IOException {
		// create connection to the AMQP server and create a channel to the exchange (See Config.EXCHANGE_NAME)
		this.conn = MQConnector.createConnection();
		this.channel = MQConnector.createChannel(this.conn);
		
		// read the team name from stdin
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Please provide a team name:");
		System.out.print("> ");

		this.teamName = stdin.readLine();

		if (this.teamName == null) {
			System.exit(1);
		}

		// use the team name in the routing key
		this.teamName = this.teamName.trim();
		this.routingKey = "race." + this.teamName;

		System.out.println(String.format("Sending events for team %s with routing key '%s' to exchange %s",
				this.teamName, this.routingKey, Config.EXCHANGE_NAME));
		System.out.println("Press ENTER to exit");
	}
	
	public void setTeamName(String tn){
		tn = tn.trim();
		//TODO Check validity of teamname?
		this.teamName = tn;
	}
	
	private void sendMessage(String message) {
		// set some properties of the message
		AMQP.BasicProperties props = new AMQP.BasicProperties();
		props.setTimestamp(new Date());	// set the time of the message, otherwise the
										// receivers do not know when the message is sent
		props.setContentType("text/plain"); // the body of the message is plain text
		props.setDeliveryMode(1);			// do not make the message persistant
		System.out.println(String.format("Send message '%s' to exchange '%s' with key '%s'",
				message, Config.EXCHANGE_NAME, routingKey));
		try {
			// publish the message to the exchange with the race.$teamname routing key
			channel.basicPublish(Config.EXCHANGE_NAME, routingKey, props, message.getBytes());
		} catch (IOException e) {
			System.err.println("Unable to send message to AMQP server"); 
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a string message to an amqp server.
	 * 
	 * @param message
	 */
	public void sendMQMessage(String message){
		if(this.conn!=null && this.channel!=null && this.teamName!=null && this.routingKey!=null){
			sendMessage(message);
		}
		else{
			System.out.println("You are stupid.");
		}
	}
	
	/**
	 * This method closes the amqp connection.
	 * 
	 * @throws IOException
	 */
	public void exit() throws IOException {
		this.channel.close();
		this.conn.close();
		//System.exit(0);
	}

}
