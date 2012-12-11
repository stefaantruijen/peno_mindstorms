package bluebot.actions.impl;


import bluebot.actions.Action;



/**
 * {@link Action} implementation for the calibration algorithm
 */
public class CalibrationAction extends Action {
	
	protected void execute() throws InterruptedException {
		int speed = getDriver().getSpeed();
		// Set speed to 35%
		getDriver().setSpeed(35);
		int max = 0;
		int min = 1023;
		//getDriver().moveForward(300F, false);
		getDriver().turnRight(360, false);
		while (!isAborted() && getDriver().isMoving()) {
			int value = getDriver().readSensorLightValue();
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
		
		final int thresholdWhite = (max + 20);

		int black = 1023;
		getDriver().moveForward(400, false);
		while(!isAborted() && getDriver().isMoving()){
			int value = getDriver().readSensorLightValue();
			if(value<black){
				black = value;
			}
		}
		
		int thresholdBlack = (black + min )/2 - 1 ;
		getDriver().getCalibration().setLightThresholdWhite(thresholdWhite);
		getDriver().getCalibration().setLightThresholdBlack(thresholdBlack);
		getDriver().setSpeed(speed);
		//	The next few lines of code will send
		//	a report of the calibration to any client(s)
		final String msg = ("Threshold (white) = " + thresholdWhite +"\n Threshold (black) = " + thresholdBlack);
		getDriver().sendMessage(msg, "Calibration");
	}
	
}