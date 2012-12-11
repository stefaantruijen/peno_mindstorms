package bluebot.actions.impl;


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
	
	
	
	protected void execute() throws InterruptedException {
		final float angle = (360F / corners);
		for (int i = corners; i > 0; i--) {
			if (isAborted()) {
				return;
			}
			getDriver().moveForward(length, false);
			waitForMotion();
			if (isAborted()) {
				return;
			}
			getDriver().turnRight(angle, false);
			waitForMotion();
		}
	}
	
}