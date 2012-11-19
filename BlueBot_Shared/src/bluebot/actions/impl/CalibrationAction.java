package bluebot.actions.impl;


import bluebot.Driver;
import bluebot.actions.Action;



/**
 * {@link Action} implementation for the calibration algorithm
 */
public class CalibrationAction extends Action {
	
	public void execute(final Driver driver) throws InterruptedException {
		// Set speed to 35%
		driver.setSpeed(35);
		int max = 0;
		int min = 1023;
		//driver.moveForward(300F, false);
		driver.turnRight(360, false);
		while (!isAborted() && driver.isMoving()) {
			int value = driver.readSensorLightValue();
			if(value > max){
				max = value;
			}
			if(value <min){
				min = value;
			}
		}
		
		if (isAborted()) {
			return;
		}
		
		final int thresholdWhite = (max + 5);

		int black = 1023;
		driver.moveForward(400, false);
		while(!isAborted() && driver.isMoving()){
			int value = driver. readSensorLightValue();
			if(value<black){
				black = value;
			}
		}
		
		int thresholdBlack = (black + min )/2;
		driver.getCalibration().setLightThresholdWhite(thresholdWhite);
		driver.getCalibration().setLightThresholdBlack(thresholdBlack);
		//	The next few lines of code will send
		//	a report of the calibration to any client(s)
		final String msg = ("Threshold (white) = " + thresholdWhite +"\n Threshold (black) = " + thresholdBlack);
		driver.sendMessage(msg, "Calibration");
	}
	
}