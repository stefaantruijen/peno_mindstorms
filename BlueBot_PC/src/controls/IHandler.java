package controls;
/**
 * This interface implements all the methods the NXT or SimulatorNXT should perform.
 * 
 * @author  Incalza Dario
 *
 */
public interface IHandler {
	/**
	 * Move the robot forward. 
	 * TODO: Determine how much forward.
	 */
	public void move();
	/**
	 * Turn the robot 90 degrees to the left.
	 */
	public void turnLeft();
	/**
	 * Turn the robot 90 degrees to the right.
	 */
	public void turnRight();
	/**
	 * Stop the current movement by the robot.
	 */
	public void stop();

}
