package bluebot.core;


import bluebot.Robot;



/**
 * 
 * @author Ruben Feyen
 */
public class PhysicalRobot implements Robot {
	
	private PilotController pc;
	
	
	
	public void moveBackward() {
		pc.backward();
	}
	
	public void moveBackward(final float distance) {
		pc.moveBackward(distance);
	}
	
	public void moveForward() {
		pc.forward();
	}
	
	public void moveForward(final float distance) {
		pc.moveForward(distance);
	}
	
	public void stop() {
		pc.stop();
	}
	
	public void turnLeft() {
		pc.left();
	}
	
	public void turnLeft(final float angle) {
		pc.turnLeft(angle);
	}

	@Override
	public void turnRight() {
		pc.right();
	}
	
	public void turnRight(final float angle) {
		pc.turnRight(angle);
	}
	
}