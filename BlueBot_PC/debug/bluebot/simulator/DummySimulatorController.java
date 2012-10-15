package bluebot.simulator;


import bluebot.core.AbstractController;



/**
 * 
 * @author Ruben Feyen
 */
public class DummySimulatorController extends AbstractController {
	
	public void moveBackward() {
		fireMessageOutgoing("Move backward");
	}
	
	protected void moveBackward(final float distance) {
		fireMessageOutgoing("Move backward " + distance + " mm");
	}
	
	public void moveForward() {
		fireMessageOutgoing("Move forward");
	}
	
	protected void moveForward(final float distance) {
		fireMessageOutgoing("Move forward " + distance + " mm");
	}
	
	public void stop() {
		fireMessageOutgoing("Stop");
	}
	
	public void turnLeft() {
		fireMessageOutgoing("Turn left");
	}
	
	protected void turnLeft(final float angle) {
		fireMessageOutgoing("Turn left " + angle + " degrees");
	}
	
	public void turnRight() {
		fireMessageOutgoing("Turn right");
	}
	
	protected void turnRight(float angle) {
		fireMessageOutgoing("Turn right " + angle + " degrees");
	}
	
}