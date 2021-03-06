package bluebot.core;


import java.io.IOException;

import bluebot.ConfigListener;
import bluebot.MotionListener;
import bluebot.actionsimpl.MazeActionV2;
import bluebot.game.Game;
import bluebot.game.GameCallback;
import bluebot.game.GameException;
import bluebot.game.World;
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
	
	public void addListener(MotionListener listener);
	
	public void addListener(SensorListener listener);
	
	public void dispose();
	
	public void doCalibrate();
	
	public Game doGame(String gameId, String playerId, GameCallback callback)
			throws GameException, IOException;
	
	public MazeActionV2 doMaze(int playerNumber, int objectNumber,
			MazeListener listener);
		
	public void doPolygon(int corners, float length);
	
	public void doTile();
	
	public void doWhiteLineOrientation();
	
	public void doSeesaw();
	
	public void doReadBarcode();
	
	
	/**
	 * Returns the world reference
	 * 
	 * @return a {@link World} object
	 */
	public World getWorld();
	
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
	public void moveBackward(float distance,boolean b);
	
	/**
	 * Moves forward
	 */
	public void moveForward();
	
	/**
	 * Moves forward
	 * 
	 * @param distance - the distance to move (in mm)
	 */
	public void moveForward(float distance,boolean b);
	
	public void removeListener(ConfigListener listener);
	
	public void removeListener(ConnectionListener listener);
	
	public void removeListener(MazeListener listener);
	
	public void removeListener(MessageListener listener);
	
	public void removeListener(MotionListener listener);
	
	public void removeListener(SensorListener listener);
	
	public void reset();
	
	public void setSpeed(int percentage);
	
	public int getReceivedBarcode();
	
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
	public void turnLeft(float angle,boolean b);
	
	/**
	 * Turns right
	 */
	public void turnRight();
	
	/**
	 * Turns right
	 * 
	 * @param angle - the angle to turn (in degrees)
	 */
	public void turnRight(float angle,boolean b);

	/**
	 * Start the pickUpAction
	 */
	public void doPickUp();
	
	public void modifyOrientation();
	
	public void turnHeadClockwise(int offset);
	
	public void turnHeadCounterClockwise(int offset);
	
	public void setStartLocation(int x,int y,float heading);
}