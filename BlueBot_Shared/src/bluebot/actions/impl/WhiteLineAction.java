package bluebot.actions.impl;


import bluebot.Driver;
import bluebot.actions.Action;



/**
 * {@link Action} implementation for the "Align on white line" algorithm
 */
public class WhiteLineAction extends Action {
	
	private static final int SPEED_FAST = 90;
	private static final int SPEED_SLOW = 30;
	
	private int threshold;
	
	
	
	public void execute(final Driver driver) throws InterruptedException {
		// exception if not calibrated
		if (!driver.getCalibration().isCalibrated()) {
			driver.sendError("Calibration of the light sensor is required");
			return;
		}
		
		threshold = driver.getCalibration().getLightThresholdWhite();
		
		// forward until white line (50%)
		driver.setSpeed(50);
		driver.moveForward();
		waitForWhiteOrWall(driver);
		driver.stop();
		
		if (isAborted()) {
			return;
		}
		
		if((driver.readSensorUltraSonic() > 14)){
			// backward until white line (12%)
			driver.setSpeed(12);
			driver.moveBackward();
			waitForWhite(driver, true);
			driver.stop();
			
			if (isAborted()) {
				return;
			}
			
			// 7 cm (sensor to wheels) forward
			driver.setSpeed(100);
			driver.moveForward(70, true);
			
			if (isAborted()) {
				return;
			}
			
			// right until white line
			driver.setSpeed(12);
			driver.turnRight();
			waitForWhite(driver, true);
			driver.stop();
			
			if (isAborted()) {
				return;
			}
			
			// left until no white line
			driver.turnLeft();
			waitForWhite(driver, false);
//			float arc = driver.getAngleIncrement();
//			driver.stop();
//			
//			if (isAborted()) {
//				return;
//			}
//			
//			// left until white line
//			driver.turnLeft();
			waitForWhite(driver, true);
			float totalArc = Math.abs(driver.getAngleIncrement());
			driver.stop();
			
			if (isAborted()) {
				return;
			}
			
	//		float totalArc = Math.abs(arc) + Math.abs(arc1);
			if(totalArc<=90){
				driver.turnLeft();
				this.waitForWhite(driver, false);
				this.waitForWhite(driver, true);
				totalArc = totalArc + Math.abs(driver.getAngleIncrement());
				driver.stop();
			}
			
			// turn right until half of totalArc 
			driver.turnRight();
			while(!isAborted() && (Math.abs(driver.getAngleIncrement()) <= ((totalArc / 2) + 2)));
			driver.stop();
		} else{
			driver.turnLeft(90, true);
			this.execute(driver);
		}
	}
	
	private final void waitForWhite(final Driver driver, final boolean flag) {
		final int threshold = this.threshold;
		if (flag) {
			while (!isAborted()
					&& driver.isMoving()
					&& (driver.readSensorLight() <= threshold));
		} else {
			while (!isAborted()
					&& driver.isMoving()
					&& (driver.readSensorLight() > threshold));
		}
	}
	
	private final void waitForWhiteOrWall(final Driver driver){
		final int threshold = this.threshold;
		while (!isAborted()
				&& driver.isMoving()
				&& (driver.readSensorLight() <= threshold)
				&& (driver.readSensorUltraSonic() > 14));
	}
	
}