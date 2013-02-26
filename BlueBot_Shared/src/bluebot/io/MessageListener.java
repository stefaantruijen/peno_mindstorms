package bluebot.io;


import java.util.EventListener;



/**
 * 
 * @author Ruben Feyen
 */
public interface MessageListener extends EventListener {
	
	/**
	 * This method is called whenever a message is received
	 * 
	 * @param msg - the message that was received
	 */
	public void onMessageIncoming(Message msg);
	
	/**
	 * This method is called whenever a message is sent
	 * 
	 * @param msg - the message that was sent
	 */
	public void onMessageOutgoing(Message msg);
	
}
