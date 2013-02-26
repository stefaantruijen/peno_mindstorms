package bluebot.io;



/**
 * 
 * @author Ruben Feyen
 */
public class Message {
	
	private String msg;
	
	
	public Message(final String msg) {
		this.msg = msg;
	}
	
	
	
	public String getMessage() {
		return msg;
	}
	
	public Type getType() {
		return Type.NORMAL;
	}
	
	
	
	
	
	
	
	
	
	
	public static enum Type {
		NORMAL,
		RABBIT_MQ
	}
	
}
