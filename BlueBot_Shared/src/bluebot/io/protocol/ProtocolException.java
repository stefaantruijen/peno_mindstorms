package bluebot.io.protocol;


import java.io.IOException;



/**
 * Indicates an error against the protocol
 * 
 * @author Ruben Feyen
 */
public class ProtocolException extends IOException {
	private static final long serialVersionUID = 1L;
	
	
	public ProtocolException(final String msg) {
		super(msg);
	}
	
}
