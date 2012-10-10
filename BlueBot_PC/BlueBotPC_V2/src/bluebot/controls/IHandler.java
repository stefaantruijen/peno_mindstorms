package bluebot.controls;
/**
 * This interface implements all the methods the NXT or SimulatorNXT should perform.
 * 
 * @author  Incalza Dario
 *
 */
public interface IHandler {
	/**
	 * Move the robot forward for a given distance in mm.
	 * 
	 * IMPORTANT : distance can only be positive.
	 * 
	 * @param distance
	 * 
	 */
	public void move(int distance);
	/**
	 * Turn the robot to the left for a given amount of degrees.
	 */
	public void turnLeft(int degrees);
	/**
	 * Turn the robot to the right for a given amount of degrees.
	 */
	public void turnRight(int degrees);
	/**
	 * Stop the current movement.
	 */
	public void stop();

}
