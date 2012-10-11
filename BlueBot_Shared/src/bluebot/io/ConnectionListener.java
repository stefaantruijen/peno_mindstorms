package bluebot.io;


import java.util.EventListener;



/**
 * 
 * @author Ruben Feyen
 */
public interface ConnectionListener extends EventListener {
	
	public void onMessageIncoming(String msg);
	
	public void onMessageOutgoing(String msg);
	
}