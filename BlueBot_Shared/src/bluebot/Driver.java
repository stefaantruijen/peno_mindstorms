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
	
	
	public Driver(final Robot robot, final Connection connection) {
		this(robot, new ServerTranslator(connection));
	}
	public Driver(final Robot robot, final ServerTranslator translator) {
		this.robot = robot;
		this.translator = translator;
	}
	
	
	
	public void doLineOrientation() {
		// TODO
	}
	
	public void doPolygon() {
		// TODO(?)
	}
	
	@SuppressWarnings("unused")
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