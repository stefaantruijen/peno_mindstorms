package bluebot;



/**
 * Signals an error in the {@link Driver} API
 * 
 * @author Ruben Feyen
 */
public class DriverException extends Exception {
	private static final long serialVersionUID = 1L;
	
	
	public DriverException(final String msg) {
		super(msg);
	}
	
}