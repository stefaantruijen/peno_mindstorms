package bluebot.actions.impl;


import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.graph.Border;
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
	protected boolean checkForWall(final Driver driver) throws InterruptedException {
		Thread.sleep(200L);
		int dist = driver.readSensorUltraSonic();
		if(dist < 25){
			return true;
		}else if(dist > 30){
			return false;
		}else{
			driver.turnHeadCounterClockWise(5);
			Thread.sleep(200L);
			int dist1 = driver.readSensorUltraSonic();
			driver.turnHeadClockWise(10);
			Thread.sleep(200L);
			int dist2 = driver.readSensorUltraSonic();
			driver.turnHeadCounterClockWise(5);
			if(dist1 < 25 || dist2 < 25){
				return true;
			}
			return false;
		}
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
		bluebot.graph.Orientation headDirection = Orientation.forHeading(pos.getHeading());
		for(int i = 0;i<=3;i++){
			switch(headDirection){
				case SOUTH:
					if(checkForWall(driver)){
						tile.setBorderSouth(Border.CLOSED);
					}
					break;
				case WEST:
					if(checkForWall(driver)){
						tile.setBorderWest(Border.CLOSED);
					}
					break;
				case EAST:
					if(checkForWall(driver)){
						tile.setBorderEast(Border.CLOSED);
					}
					break;
				case NORTH:
					if(checkForWall(driver)){
						tile.setBorderNorth(Border.CLOSED);
					}
					break;
				default:
					break;
			
			}
			driver.turnHeadClockWise(90);
			headDirection = headDirection.rotateCW();
			driver.sendTile(tile);
		}
		for(int i = 0;i<=3;i++){
			driver.turnHeadCounterClockWise(90);
			headDirection = headDirection.rotateCCW();
		}
		tile.setExplored();
		driver.sendTile(tile);
		
		
	}
	
}