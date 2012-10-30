package bluebot.nxt;


import bluebot.util.Orientation;



/**
 * 
 * @author Ruben Feyen
 */
public interface Axle {
	
	public float getHeading();
	
	public Orientation getOrientation();
	
	public float getX();
	
	public float getY();
	
	public void reset();
	
}