package bluebot;


import bluebot.sensors.Brightness;
import bluebot.sensors.Calibration;
import bluebot.sensors.CalibrationException;



/**
 * Skeletal implementation of the {@link Operator} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractOperator implements Operator {
	
	public boolean detectInfrared() {
		final int dir = readSensorInfrared();
		return ((dir > 2) && (dir < 8));
	}
	
	public void dispose() {
		//	ignored
	}
	
	protected boolean isSensorLightBrightness(final Brightness brightness)
			throws CalibrationException {
		return (readSensorLightBrightness() == brightness);
	}
	
	public boolean isSensorLightBrightnessBlack() throws CalibrationException {
		return isSensorLightBrightness(Brightness.BLACK);
	}
	
	public boolean isSensorLightBrightnessGray() throws CalibrationException {
		return isSensorLightBrightness(Brightness.GRAY);
	}
	
	public boolean isSensorLightBrightnessWhite() throws CalibrationException {
		return isSensorLightBrightness(Brightness.WHITE);
	}
	
	public Brightness readSensorLightBrightness() throws CalibrationException {
		final Calibration calibration = getCalibration();
		if (!calibration.isCalibrated()) {
			throw new CalibrationException("The light sensor has not been calibrated");
		}
		
		final int value = readSensorLight();
		if (value >= calibration.getLightThresholdWhite()) {
			return Brightness.WHITE;
		} else if (value > calibration.getLightThresholdBlack()) {
			return Brightness.GRAY;
		} else {
			return Brightness.BLACK;
		}
	}
	
}
