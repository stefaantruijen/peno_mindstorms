package bluebot.actions;



/**
 * Signals an error during the execution of an {@link Action}
 * 
 * @author Ruben Feyen
 */
public class ActionException extends Exception {
	private static final long serialVersionUID = 1L;
	
	
	public ActionException(final String msg) {
		super(msg);
	}
	
}