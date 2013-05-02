package bluebot;


import java.util.ArrayList;

import bluebot.sensors.Brightness;
import bluebot.sensors.Calibration;
import bluebot.sensors.CalibrationException;



/**
 * Skeletal implementation of the {@link Operator} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractOperator implements Operator {
	
	private OperatorListener[] listeners = new OperatorListener[0];
	
	
	
	public void addListener(final OperatorListener listener) {
		if (listener == null) {
			return;
		}
		
		final int n = listeners.length;
		if (n == 0) {
			listeners = new OperatorListener[] { listener };
			return;
		}
		
		for (final OperatorListener registered : listeners) {
			if (listener == registered) {
				return;
			}
		}
		
		final OperatorListener[] array = new OperatorListener[n + 1];
		System.arraycopy(this.listeners, 0, array, 0, n);
		array[n] = listener;
		listeners = array;
	}
	
	public boolean detectInfrared() {
		final int dir = readSensorInfrared();
		return ((dir > 2) && (dir < 8));
	}
	
	public void dispose() {
		//	ignored
	}
	
	protected void fireSpeedChanged(final int percentage) {
		for (final OperatorListener listener : getListeners()) {
			listener.onSpeedChanged(percentage);
		}
	}
	
	private final OperatorListener[] getListeners() {
		return listeners;
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
	
	public void removeListener(final OperatorListener listener) {
		if (listener == null) {
			return;
		}
		
		final int n = listeners.length;
		if (n == 0) {
			return;
		}
		
		final ArrayList<OperatorListener> list = new ArrayList<OperatorListener>();
		boolean removed = false;
		for (final OperatorListener registered : listeners) {
			if (!removed && (listener == registered)) {
				removed = true;
			} else {
				list.add(registered);
			}
		}
		if (removed) {
			final OperatorListener[] array = new OperatorListener[n - 1];
			list.toArray(array);
			listeners = array;
		}
	}
	
}
