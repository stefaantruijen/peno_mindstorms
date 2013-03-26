package bluebot.sensors;



/**
 * Represents the information gathered from calibrating
 * 
 * @author Ruben Feyen
 */
public class Calibration {
	
	private int lightThresholdWhite = 556;
	private int lightThresholdBlack = 522;
	
	
	
	public int getLightThresholdWhite() {
		return lightThresholdWhite;
	}
	
	public int getLightThresholdBlack() {
		return lightThresholdBlack;
	}
	
	public boolean isCalibrated() {
		return ((getLightThresholdWhite() != -1)&&(getLightThresholdBlack() != -1));
	}
	
	public void setLightThresholdWhite(final int value) {
		this.lightThresholdWhite = value;
	}
	public void setLightThresholdBlack(final int value) {
		this.lightThresholdBlack = value;
	}
	
}