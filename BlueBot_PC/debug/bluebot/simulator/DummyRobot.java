package bluebot.simulator;


import bluebot.Robot;



/**
 * Dummy implementation of the {@link Robot} interface
 * (for debugging purposes)
 * 
 * @author Ruben Feyen
 */
public class DummyRobot implements Robot {
	
	public void moveBackward() {
		
	}
	
	public void moveBackward(final float distance) {
		
	}
	
	public void moveForward() {
		System.out.println("Moving forward");
	}
	
	public void moveForward(final float distance) {
		
	}
	
	public void stop() {
		System.out.println("Stopped");
	}
	
	public void turnLeft() {
		
	}
	
	public void turnLeft(final float angle) {
		
	}
	
	public void turnRight() {
		
	}
	
	public void turnRight(final float angle) {
		
	}
	
}