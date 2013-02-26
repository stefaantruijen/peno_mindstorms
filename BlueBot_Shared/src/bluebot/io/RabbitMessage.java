package bluebot.io;



/**
 * 
 * @author Ruben Feyen
 */
public class RabbitMessage extends Message {
	
	public static final String DEFAULT_KEY = "BlueBot";
	
	private String key;
	
	
	public RabbitMessage(final String msg) {
		this(msg, DEFAULT_KEY);
	}
	public RabbitMessage(final String msg, final String key) {
		super(msg);
		this.key = key;
	}
	
	
	
	public String getKey() {
		return key;
	}
	
	@Override
	public Type getType() {
		return Type.RABBIT_MQ;
	}
	
}
