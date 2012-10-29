package bluebot;


import bluebot.io.Connection;



/**
 * Default implementation of the {@link Driver} interface
 */
public class DefaultDriver extends AbstractDriver {
	
	private double fastSpeed = 150;
	private double slowSpeed = 25;
	private int WhiteThreshold = -1;
	
	
	public DefaultDriver(final Robot robot, final Connection connection) {
		super(robot, connection);
	}
	
	
	
	/**
	 * Calibrates the WhiteThreshold.
	 */
	public void calibrate(){
		setSpeedLow();
		int max = 0;
		moveForward(300, false);
		while(isMoving()){
			int value = readSensorLight();
			if(value>max){
				max = value;
			}
		}
		WhiteThreshold = max + 2;
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
		setSpeedHigh();
		moveForward();
		waitForWhite(true);
		stop();
		
		// backward until white line (slow)
		setSpeedLow();
		moveBackward();
		waitForWhite(true);
		stop();
		
		// 7 cm (sensor to wheels) forward
		moveForward(70, true);
		// right until white line
		turnRight();
		waitForWhite(true);
		stop();
		
		// left until no white line
		turnLeft();
		waitForWhite(false);
		float arc = getAngleIncrement();
		stop();
		
		// left until white line
		turnLeft();
		waitForWhite(true);
		float arc1 = getAngleIncrement();
		stop();
		
		float totalArc = Math.abs(arc) + Math.abs(arc1);
		
		if(totalArc<=90){
			totalArc = totalArc + 90;
		}
		
		// turn right until half of totalArc 
		turnRight();
		
		while(Math.abs(getAngleIncrement()) <= totalArc/2);
		stop();
	}
	
	protected double getSpeedHigh() {
		return fastSpeed;
	}
	
	protected double getSpeedLow() {
		return slowSpeed;
	}
	
	protected double getSpeedMedium() {
		return ((getSpeedLow() + getSpeedHigh()) / 2D);
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