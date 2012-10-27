package bluebot;



/**
 * Represents the logic for handling a {@link Robot}
 * 
 * @author Ruben Feyen
 */
public interface Driver extends Mobile {
	
	public void calibrate();
	
	public void doWhiteLineOrientation();
	
}