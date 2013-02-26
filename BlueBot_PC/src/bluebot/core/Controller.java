package bluebot.core;


import bluebot.ConfigListener;
import bluebot.io.ConnectionListener;
import bluebot.io.MessageListener;
import bluebot.maze.MazeListener;
import bluebot.sensors.SensorListener;
import bluebot.util.EventDispatcher;



/**
 * Represents the API of the client-side back-end
 * 
 * @author Ruben Feyen
 */
public interface Controller extends EventDispatcher<ControllerListener> {
	
	public void addListener(ConfigListener listener);
	
	public void addListener(ConnectionListener listener);
	
	public void addListener(MazeListener listener);
	
	public void addListener(MessageListener listener);
	
	public void addListener(SensorListener listener);
	
	public void dispose();
	
	public void doCalibrate();
	
	public void doMaze(int pathfinder);
	
	public void doPolygon(int corners, float length);
	
	public void doTile();
	
	public void doWhiteLineOrientation();
	
	public void init();
	
	/**
	 * Moves backward
	 */
	public void moveBackward();
	
	/**
	 * Moves backward
	 * 
	 * @param distance - the distance to move (in mm)
	 */
	public void moveBackward(float distance);
	
	/**
	 * Moves forward
	 */
	public void moveForward();
	
	/**
	 * Moves forward
	 * 
	 * @param distance - the distance to move (in mm)
	 */
	public void moveForward(float distance);
	
	public void removeListener(ConfigListener listener);
	
	public void removeListener(ConnectionListener listener);
	
	public void removeListener(MazeListener listener);
	
	public void removeListener(MessageListener listener);
	
	public void removeListener(SensorListener listener);
	
	public void reset();
	
	public void setSpeed(int percentage);
	
	/**
	 * Stops moving/turning
	 */
	public void stop();
	
	/**
	 * Turns left
	 */
	public void turnLeft();
	
	/**
	 * Turns left
	 * 
	 * @param angle - the angle to turn (in degrees)
	 */
	public void turnLeft(float angle);
	
	/**
	 * Turns right
	 */
	public void turnRight();
	
	/**
	 * Turns right
	 * 
	 * @param angle - the angle to turn (in degrees)
	 */
	public void turnRight(float angle);
	
}