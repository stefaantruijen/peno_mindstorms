package bluebot;


import bluebot.io.Connection;



/**
 * Default implementation of the {@link Driver} interface
 */
public class DefaultDriver extends AbstractDriver {
	
	private double fastSpeed = 300;
	private double slowSpeed = 25;
	private int WhiteThreshold = -1;
	
	
	public DefaultDriver(final Robot robot, final Connection connection) {
		super(robot, connection);
	}
	
	
	
	/**
	 * Calibrates the WhiteThreshold.
	 */
	public void calibrate(){
		setTravelSpeed(slowSpeed);
		int min = 100;
		moveForward(300);
		while(isMoving()){
			int value = readSensorLight();
			if(value<min){
				min = value;
			}
		}
		WhiteThreshold = min - 2;
	}
	
	
	/**
	 * Sets the robot perpendicular to the first white line the robot encounters.
	 */
	public void doWhiteLineOrientation() {
		// exception if not calibrated
		if(WhiteThreshold==-1){
			sendError("Calibration of the light sensor is required");
			return;
		}
		
		// forward until white line (fast)
		setTravelSpeed(fastSpeed);
		moveForward();
		waitForWhite(true);
		stop();
		
		// backward until white line (slow)
		setTravelSpeed(slowSpeed);
		moveBackward();
		waitForWhite(true);
		stop();
		
		// 7 cm (sensor to wheels) forward
		moveForward(70);
		// right until white line
		turnRight();
		waitForWhite(true);
		stop();
		
		// left until no white line
		turnLeft();
		waitForWhite(false);
		stop();
		
		// left until white line
		turnLeft();
		long time1 = System.currentTimeMillis();
		waitForWhite(true);
		stop();
		long time2 = System.currentTimeMillis();
		long timeToRotate = (time2-time1)/2;
		
		// turn right until time (half of whole turn) is over 
		turnRight();
		long time3 = System.currentTimeMillis();
		while((System.currentTimeMillis()-time3) <timeToRotate);
		stop();
	}
	
	public void setSpeedHigh() {
		// TODO
		setTravelSpeed(fastSpeed);
	}
	
	public void setSpeedLow() {
		// TODO
		setTravelSpeed(slowSpeed);
	}
	
	public void setSpeedMedium() {
		// TODO
		setTravelSpeed((slowSpeed + fastSpeed) / 2D);
	}
	
	private final void waitForWhite(final boolean flag) {
		final int threshold = WhiteThreshold;
		if (flag) {
			while (readSensorLight() <= threshold);
		} else {
			while (readSensorLight() > threshold);
		}
	}
	
}