package bluebot.ui.util;



/**
 * 
 * @author Ruben Feyen
 */
public class RabbitMessage {
	
	public static final String DEFAULT_KEY = "BlueBot";
	
	private String key, msg;
	
	
	public RabbitMessage(final String msg) {
		this(msg, DEFAULT_KEY);
	}
	public RabbitMessage(final String msg, final String key) {
		this.key = key;
		this.msg = msg;
	}
	
	
	
	public String getKey() {
		return key;
	}
	
	public String getMessage() {
		return msg;
	}
	
}
