package bluebot.io.rabbitmq;

/**
 * An interface that contains the configuration data.
 * 
 * @author Ine & Michiel
 */
public interface RabbitConfig {
	public static final String USER_NAME = "guest";
	public static final String PASSWORD = "guest";
	public static final String VIRTUAL_HOST = "/";
	public static final String HOST_NAME = "leuven.cs.kotnet.kuleuven.be";
//	public static final String HOST_NAME = "localhost";
	public static final int PORT = 5672;
	
	// the default exchange that will be used for races during the official demo's
	public static final String EXCHANGE_NAME = "RaceExchange";
	public static final String LAUNCH_ROUTING_KEY = "race.launch";
	
	//monitor key for the event listener
	public static final String MONITOR_KEY = "race.*";
}