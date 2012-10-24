package bluebot;


import bluebot.io.Connection;
import bluebot.io.ServerTranslator;



/**
 * 
 * @author Ruben Feyen
 */
public class Driver {
	
	private Robot robot;
	private ServerTranslator translator;
	private double fastSpeed = 300;
	private double slowSpeed = 25;
	private int WhiteThreshold = -1;
	
	
	public Driver(final Robot robot, final Connection connection) {
		this(robot, new ServerTranslator(connection));
	}
	public Driver(final Robot robot, final ServerTranslator translator) {
		this.robot = robot;
		this.translator = translator;
	}
	
	/**
	 * Calibrates the WhiteThreshold.
	 */
	public void calibrate(){
		robot.setTravelSpeed(slowSpeed);
		int min = 100;
		robot.moveForward(300);
		while(robot.isMoving()){
			int value = robot.readSensorLight();
			if(value<min){
				min = value;
			}
		}
		WhiteThreshold = min;
	}
	
	
	/**
	 * Sets the robot perpendicular to the first white line the robot encounters.
	 */
	public void doWhiteLineOrientation() {
		// exception if not calibrated
		if(WhiteThreshold==-1){
			getTranslator().sendError("Calibration of the light sensor is required");
			return;
		}
		
		// forward until white line (fast)
		robot.setTravelSpeed(fastSpeed);
		robot.moveForward();
		while(robot.readSensorLight()<=WhiteThreshold){
		}
		robot.stop();
		
		// backward until white line (slow)
		robot.setTravelSpeed(slowSpeed);
		robot.moveBackward();
		while(robot.readSensorLight()<=WhiteThreshold){
		}
		robot.stop();
		
		// 7 cm (sensor to wheels) forward
		robot.moveForward(70);
		// right until white line
		robot.turnRight();
		while(robot.readSensorLight()<=WhiteThreshold){
		}
		robot.stop();
		
		// left until no white line
		robot.turnLeft();
		while(robot.readSensorLight()>WhiteThreshold){
		}
		robot.stop();
		
		// left until white line
		robot.turnLeft();
		long time1 = System.currentTimeMillis();
		while(robot.readSensorLight()<=WhiteThreshold){
		}
		robot.stop();
		long time2 = System.currentTimeMillis();
		long timeToRotate = (time2-time1)/2;
		
		// turn right until time (half of whole turn) is over 
		robot.turnRight();
		long time3 = System.currentTimeMillis();
		while((System.currentTimeMillis()-time3) <timeToRotate){
		}
		robot.stop();
	}
	
	public void doPolygon() {
		// TODO(?)
	}
	
	private final ServerTranslator getTranslator() {
		return translator;
	}
	
	public void moveBackward() {
		robot.moveBackward();
	}
	
	public void moveBackward(final float distance) {
		robot.moveBackward(distance);
	}
	
	public void moveForward() {
		robot.moveForward();
	}
	
	public void moveForward(final float distance) {
		robot.moveForward(distance);
	}
	
	public void stop() {
		robot.stop();
	}
	
	public void turnLeft() {
		robot.turnLeft();
	}
	
	public void turnLeft(final float angle) {
		robot.turnLeft(angle);
	}
	
	public void turnRight() {
		robot.turnRight();
	}
	
	public void turnRight(final float angle) {
		robot.turnRight(angle);
	}
	
}