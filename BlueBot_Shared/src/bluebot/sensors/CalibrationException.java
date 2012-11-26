package bluebot.sensors;


import bluebot.DriverException;



/**
 * Signals the need for calibration
 * 
 * @author Ruben Feyen
 */
public class CalibrationException extends DriverException {
	private static final long serialVersionUID = 1L;
	
	
	public CalibrationException(final String msg) {
		super(msg);
	}
	
}