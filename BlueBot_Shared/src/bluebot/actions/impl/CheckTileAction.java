package bluebot.actions.impl;


import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.graph.Orientation;
import bluebot.graph.Tile;



/**
 * {@link Action} implementation for the "Check tile" algorithm
 * 
 * @author Dario Incalza
 * @author Ruben Feyen
 */
public class CheckTileAction extends Action {
	
	/*
	 * This flag determines whether the "Maze" button in the GUI
	 * should start the MazeAction, or this CheckTileAction.
	 * 
	 * TRUE  => CheckTileAction
	 * FALSE => MazeAction
	 * 
	 * This is a rather ugly way to implement the feature,
	 * but it avoids polluting the protocol with temporary packets.
	 */
	public static final boolean USE_MANUAL_CHECK_TILE = true;
	
	
	
	/**
	 * Determines whether or not the robot is facing a wall
	 * 
	 * @param driver - the {@link Driver} of the robot
	 * 
	 * @return <code>TRUE</code> if facing a wall,
	 * 			<code>FALSE</code> otherwise
	 */
	protected boolean detectWall(final Driver driver) {
		// TODO
//		return (Math.random() < 0.5);
		return (driver.readSensorUltraSonic() < 25);
	}
	
	public void execute(final Driver driver)
			throws ActionException, InterruptedException {
		// The fully qualified classname is used here
		// to avoid issues with bluebot.graph.Orientation
		final bluebot.util.Orientation pos = driver.getOrientation();
		
		// Determine X and Y in the tile coordinate-system
		final int x = (int)Math.floor(((Tile.SIZE / 2D) + pos.getX()) / Tile.SIZE);
		final int y = (int)Math.floor(((Tile.SIZE / 2D) + pos.getY()) / Tile.SIZE);
		
		// Generate a Tile object for the current tile
		final Tile tile = new Tile(x, y);
		
		// Determine the initial orientation of the US sensor
		// It always resets to match the orientation of the robot
		Orientation head = Orientation.forHeading(pos.getHeading());
		
		// Scan in front of the robot
		tile.setBorder(head, !detectWall(driver));
		
		// Turn the sensor clockwise, twice
		// The first turn scans the right side of the robot
		// The second turn scans behind the robot
		for (int i = 0; i < 2; i++) {
			driver.turnHeadClockWise(90);
			head = head.rotateCW();
			tile.setBorder(head, !detectWall(driver));
		}
		
		// Turn the sensor around
		// to scan the left side of the robot
		driver.turnHeadCounterClockWise(270);
		head = head.rotateCW();
		tile.setBorder(head, !detectWall(driver));
		
		// Turn the sensor back to match the orientation of the robot
		driver.turnHeadClockWise(90);
		
		// Send the tile information to the client
		driver.sendTile(tile);
	}
	
}