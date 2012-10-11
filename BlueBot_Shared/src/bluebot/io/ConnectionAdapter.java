package bluebot.io;



/**
 * An abstract adapter class for the {@link ConnectionListener} interface
 * 
 * @author Ruben Feyen
 */
public abstract class ConnectionAdapter implements ConnectionListener {
	
	public void onMessageIncoming(final String msg) {
		//	Subclasses can override this method to provide functionality
	}
	
	public void onMessageOutgoing(final String msg) {
		//	Subclasses can override this method to provide functionality
	}
	
}