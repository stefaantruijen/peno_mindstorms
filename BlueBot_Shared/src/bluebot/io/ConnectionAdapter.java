package bluebot.io;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class ConnectionAdapter implements ConnectionListener {
	
	public void onMessageIncoming(final String msg) {
		// ignored
	}
	
	public void onMessageOutgoing(final String msg) {
		// ignored
	}
	
}