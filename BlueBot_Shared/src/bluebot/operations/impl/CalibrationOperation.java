package bluebot.operations.impl;


import bluebot.operations.Operation;
import bluebot.sensors.Calibration;



/**
 * 
 * @author Ruben Feyen
 */
public class CalibrationOperation extends Operation<Void> {
	
	protected Void execute() throws InterruptedException {
		// Set speed to 35%
		getOperator().setSpeed(35);
		
		int max = 0;
		int min = 1023;
		//getOperator().moveForward(300F, false);
		getOperator().turnRight(360, false);
		while (!isAborted() && getOperator().isMoving()) {
			int value = getOperator().readSensorLight();
			if(value > max){
				max = value;
			}
			if(value <min){
				min = value;
			}
		}
		
		checkAborted();
		
		final int thresholdWhite = (max + 20);

		int black = 1023;
		getOperator().moveForward(400, false);
		while(!isAborted() && getOperator().isMoving()){
			int value = getOperator().readSensorLight();
			if(value<black){
				black = value;
			}
		}
		
		checkAborted();
		
		int thresholdBlack = (black + min )/2 - 1 ;
		
		final Calibration calibration = getOperator().getCalibration();
		calibration.setLightThresholdWhite(thresholdWhite);
		calibration.setLightThresholdBlack(thresholdBlack);
		return null;
	}
	
}
