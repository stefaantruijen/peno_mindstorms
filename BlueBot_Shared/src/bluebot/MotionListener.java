package bluebot;


import java.util.EventListener;



/**
 * Event listener interface for motion updates
 * 
 * @author Ruben Feyen
 */
public interface MotionListener extends EventListener {
	
	/**
	 * This method is called whenever a motion update is received
	 * 
	 * @param x - the position on the X axis
	 * @param y - the position on the Y axis
	 * @param heading - the heading (in degrees), zero equals north
	 */
	public void onMotion(float x, float y, float heading);
	
}