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
	
	/**
	 * Determines whether or not the robot is facing a wall
	 * 
	 * @param driver - the {@link Driver} of the robot
	 * 
	 * @return <code>TRUE</code> if facing a wall,
	 * 			<code>FALSE</code> otherwise
	 */
	protected boolean checkForWall() throws InterruptedException {
		Thread.sleep(200L);
		int dist = getDriver().readSensorUltraSonic();
		if(dist < 25){
			return true;
		}else if(dist > 30){
			return false;
		}else{
			getDriver().turnHeadCounterClockWise(5);
			Thread.sleep(200L);
			int dist1 = getDriver().readSensorUltraSonic();
			getDriver().turnHeadClockWise(10);
			Thread.sleep(200L);
			int dist2 = getDriver().readSensorUltraSonic();
			getDriver().turnHeadCounterClockWise(5);
			if(dist1 < 25 || dist2 < 25){
				return true;
			}
			return false;
		}
	}
	
	protected void execute() throws ActionException, InterruptedException {
		// Correct the orientation of the robot (if necessary)
		getDriver().modifyOrientation();
		
		// The fully qualified classname is used here
		// to avoid issues with bluebot.graph.Orientation
		final bluebot.util.Orientation pos = getDriver().getOrientation();
		
		// Determine X and Y in the tile coordinate-system
		final int x = (int)Math.floor(((Tile.SIZE / 2D) + pos.getX()) / Tile.SIZE);
		final int y = (int)Math.floor(((Tile.SIZE / 2D) + pos.getY()) / Tile.SIZE);
		
		// Generate a Tile object for the current tile
		final Tile tile = new Tile(x, y);
		bluebot.graph.Orientation headDirection = Orientation.forHeading(pos.getHeadingBody());
		for(int i = 0;i<=3;i++){
			switch(headDirection){
				case SOUTH:
					if(checkForWall()){
						tile.setBorderSouth(Border.CLOSED);
					}else{
						tile.setBorderSouth(Border.OPEN);
					}
					break;
				case WEST:
					if(checkForWall()){
						tile.setBorderWest(Border.CLOSED);
					}else{
						tile.setBorderWest(Border.OPEN);
					}
					break;
				case EAST:
					if(checkForWall()){
						tile.setBorderEast(Border.CLOSED);
					}else{
						tile.setBorderEast(Border.OPEN);
					}
					break;
				case NORTH:
					if(checkForWall()){
						tile.setBorderNorth(Border.CLOSED);
					}else{
						tile.setBorderNorth(Border.OPEN);
					}
					break;
				default:
					break;
			
			}
			getDriver().turnHeadClockWise(90);
			headDirection = headDirection.rotateCW();
			getDriver().sendTile(tile);
		}
		for(int i = 0;i<=3;i++){
			getDriver().turnHeadCounterClockWise(90);
			headDirection = headDirection.rotateCCW();
		}
		getDriver().sendTile(tile);
		
		
	}
	
}