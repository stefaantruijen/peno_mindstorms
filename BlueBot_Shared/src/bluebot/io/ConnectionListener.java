package bluebot.io;


import java.util.EventListener;



/**
 * Event listener interface for the {@link Connection} class
 * 
 * @author Ruben Feyen
 */
public interface ConnectionListener extends EventListener {
	
	/**
	 * This method is called whenever an incoming message is detected
	 * 
	 * @param msg - the content of the message
	 */
	public void onMessageIncoming(String msg);
	
	/**
	 * This method is called whenever an outgoing message is detected
	 * 
	 * @param msg - the content of the message
	 */
	public void onMessageOutgoing(String msg);
	
}