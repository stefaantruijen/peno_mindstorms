package bluebot.actions.impl;


import bluebot.Driver;
import bluebot.actions.Action;



/**
 * {@link Action} implementation for the polygon algorithm
 * 
 * @author Ruben Feyen
 */
public class PolygonAction extends Action {
	
	private int corners;
	private float length;
	
	
	public PolygonAction(final int corners, final float length) {
		this.corners = corners;
		this.length = length;
	}
	
	
	
	public void execute(final Driver driver) throws InterruptedException {
		final float angle = (360F / corners);
		for (int i = corners; i > 0; i--) {
			driver.moveForward(length, false);
			waitForMotion(driver);
			driver.turnRight(angle, false);
			waitForMotion(driver);
		}
	}
	
}