package bluebot.actions.impl;


import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.sensors.Brightness;
import bluebot.sensors.CalibrationException;



/**
 * {@link Action} implementation for the "Align on white line" algorithm
 */
public class WhiteLineAction extends Action {

	protected void execute() throws ActionException, DriverException, InterruptedException {
//		System.out.println("started");
		int speed = getDriver().getSpeed();
		// exception if not calibrated
		if (!getDriver().getCalibration().isCalibrated()) {
			throw new ActionException("Calibration of the light sensor is required");
		}
		
		// forward until white line (50%)
		getDriver().setSpeed(50);
		getDriver().moveForward();
		waitForWhiteOrWall();
		getDriver().stop();
		
		if (isAborted()) {
			return;
		}
		
		if((getDriver().readSensorUltraSonic() > 14)){
			// backward until white line (12%)
			getDriver().setSpeed(12);
			getDriver().moveBackward();
			waitForLightSensor(Brightness.WHITE, true);
			getDriver().stop();
			
			if (isAborted()) {
				return;
			}
			
			// 4,2 cm (sensor to wheels) forward
			getDriver().setSpeed(100);
			getDriver().moveForward(42, true);
			
			if (isAborted()) {
				return;
			}
			
			// right until white line
			getDriver().setSpeed(12);
			getDriver().turnRight();
			waitForLightSensor(Brightness.WHITE, true);
			getDriver().stop();
			
			if (isAborted()) {
				return;
			}
			
			// left until no white line
			getDriver().turnLeft();
			waitForLightSensor(Brightness.WHITE, false);
			waitForLightSensor(Brightness.WHITE, true);
			float totalArc = Math.abs(getDriver().getAngleIncrement());
			getDriver().stop();
			
			if (isAborted()) {
				return;
			}
			
			// turn right until half of totalArc 
			getDriver().turnRight();
			//while(!isAborted() && (Math.abs(getDriver().getAngleIncrement()) <= ((totalArc / 2) + 1)));
			while(!isAborted() && (Math.abs(getDriver().getAngleIncrement()) <= ((totalArc / 2))));
			getDriver().stop();
			
		} else{
			getDriver().turnLeft(90, true);
			this.execute();
		}
		getDriver().setSpeed(speed);
	}
	
	private final void waitForWhiteOrWall() throws CalibrationException{
		while (!isAborted()
				&& getDriver().isMoving()
				&& !getDriver().readsWhite()
				&& (getDriver().readSensorUltraSonic() > 14));
	}
	
}