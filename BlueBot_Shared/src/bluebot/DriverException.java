package bluebot;


import java.io.IOException;



/**
 * Indicates an error against the protocol
 */
public class DriverException extends IOException {
	private static final long serialVersionUID = 1L;
	
	
	public DriverException(final String msg) {
		super(msg);
	}
	
}
