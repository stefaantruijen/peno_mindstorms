package bluebot.operations.impl;


import bluebot.operations.Operation;
import bluebot.sensors.Brightness;
import bluebot.sensors.CalibrationException;



/**
 * 
 * @author Ruben Feyen
 */
public class WhiteLineOperation extends Operation<Void> {
	
	protected Void execute() throws CalibrationException, InterruptedException {
		// exception if not calibrated
		if (!getOperator().getCalibration().isCalibrated()) {
			throw new CalibrationException("Calibration of the light sensor is required");
		}
		
		// forward until white line (50%)
		getOperator().setSpeed(50);
		getOperator().moveForward();
		waitForWhiteOrWall();
		getOperator().stop();
		
		checkAborted();
		
		if (getOperator().readSensorUltrasonic() <= 14) {
			getOperator().turnLeft(90, true);
			return execute();
		}
		
		// backward until white line (12%)
		getOperator().moveForward(10, true);//Hack for simulator to also go a bit over the white line
		getOperator().setSpeed(12);
		getOperator().moveBackward();
		waitForLightSensor(Brightness.WHITE, true);
		getOperator().stop();
		
		checkAborted();
		
		// 4,2 cm (sensor to wheels) forward
		getOperator().setSpeed(100);
		getOperator().moveForward(42, true);
		
		checkAborted();
		
		// right until white line
		getOperator().setSpeed(12);
		getOperator().turnRight();
		waitForLightSensor(Brightness.WHITE, true);
		getOperator().stop();
		
		checkAborted();
		
		// left until no white line
		getOperator().turnLeft();
		waitForLightSensor(Brightness.WHITE, false);
		waitForLightSensor(Brightness.WHITE, true);
		float totalArc = Math.abs(getOperator().getAngleIncrement());
		getOperator().stop();
		
		checkAborted();
		
		// turn right until half of totalArc 
		getOperator().turnRight();
		//while(!isAborted() && (Math.abs(getOperator().getAngleIncrement()) <= ((totalArc / 2) + 1)));
		while(!isAborted() && (Math.abs(getOperator().getAngleIncrement()) <= ((totalArc / 2))));
		getOperator().stop();
		return null;
	}
	
	private final void waitForWhiteOrWall() throws CalibrationException {
		while (!isAborted()
				&& getOperator().isMoving()
				&& !getOperator().isSensorLightBrightnessWhite()
				&& (getOperator().readSensorUltrasonic() > 14));
	}
	
}
