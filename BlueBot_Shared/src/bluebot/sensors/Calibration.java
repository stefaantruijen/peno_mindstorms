package bluebot.sensors;



/**
 * Represents the information gathered from calibrating
 * 
 * @author Ruben Feyen
 */
public class Calibration {
	
	private int lightThresholdWhite = -1;
	
	
	
	public int getLightThresholdWhite() {
		return lightThresholdWhite;
	}
	
	public boolean isCalibrated() {
		return (getLightThresholdWhite() != -1);
	}
	
	public void setLightThresholdWhite(final int value) {
		this.lightThresholdWhite = value;
	}
	
}