package bluebot.actions.impl;


import bluebot.Driver;
import bluebot.actions.Action;



/**
 * {@link Action} implementation for the calibration algorithm
 */
public class CalibrationAction extends Action {
	
	public void execute(final Driver driver) throws InterruptedException {
		// Set speed to 25%
		driver.setSpeed(25);
		
		int max = 0;
		driver.moveForward(300F, false);
		while (!isAborted() && driver.isMoving()) {
			int value = driver.readSensorLight();
			if(value > max){
				max = value;
			}
		}
		
		if (isAborted()) {
			return;
		}
		
		final int thresholdWhite = (max + 2);
		driver.getCalibration().setLightThresholdWhite(thresholdWhite);
		
		//	The next few lines of code will send
		//	a report of the calibration to any client(s)
		final String msg = ("Threshold (white) = " + thresholdWhite);
		driver.sendMessage(msg, "Calibration");
	}
	
}