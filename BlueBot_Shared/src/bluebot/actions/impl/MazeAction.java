package bluebot.actions.impl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import algorithms.Dijkstra;
import bluebot.BarcodeExecuter;
import bluebot.Driver;
import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.graph.Border;
import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.sensors.CalibrationException;



/**
 * {@link Action} implementation for the maze exploration algorithm
 */
public class MazeAction extends Action {
	
	private Driver driver;
	private final Graph maze;
	private Direction headDirection,moveDirection;
	private Tile current;
	private int turnTimes = 0;
	private List<Tile> blackSpots;
	private BarcodeExecuter barcodeExecuter;
	private ArrayList<Tile> stillCheckForBarcode;
	private final Dijkstra pf;
	
	
	public MazeAction(){
		this.maze = new Graph();
		this.headDirection=Direction.UP;
		this.moveDirection=Direction.UP;
		this.blackSpots = null;
		this.stillCheckForBarcode = new ArrayList<Tile>();
		this.pf = new Dijkstra(this.maze);
		//this.pf = PathFinderFactory.createPathFinder(this.maze, pathFinderChoice);
	}
	/**
	 * Execute the wall following algorithm. Always keep the wall to your right. Till we're back on the start position and all
	 * Start neighbors are explored. This means 'black spots' still remain in the maze. The algorithm detects black spots and will visit the black spots to explore the remaining tiles.
	 * @throws ActionException 
	 * @throws CalibrationException 
	 */
	@Override
	public void execute(Driver driver) throws InterruptedException, ActionException, DriverException {
		this.driver = driver;
		this.driver.resetOrientation();
		long startTime = System.currentTimeMillis();
		this.initializeRootTile();
		
		// The barcode executor can only be initialized here,
		// because there is no Driver instance
		// to pass to its constructor before this point.
		this.barcodeExecuter = new BarcodeExecuter(driver, maze);
		
		do{
			if(isAborted()){
				return;
			}
			
			Tile next = this.determineNextTile();
			this.moveTo(next);
			if(!next.isExplored()){
				this.checkEfficicientlyTile(next);
			}
			
			if(next.canHaveBarcode()){
				final int barcode = scanBarcode(next);
				if (barcode > 0) {
					this.barcodeExecuter.executeBarcode(barcode, next);
				}
			}
			
			this.maze.addVerticies(next.getAbsoluteNeighbors());
			
			if(current == this.maze.getRootTile() && !hasUnvisitedNeighbors(this.maze.getRootTile())){
				this.findBlackSpots();
			}
			
		}while(this.hasUnvisitedNeighbors(this.maze.getRootTile())||this.hasUnvisitedNeighbors(current)||this.graphHasUnvisitedNeighbors());
		
		this.processBarcodes();
		this.current.setOrientationToReach(this.moveDirection);
		long stopTime = System.currentTimeMillis();
		long duration = stopTime-startTime;
		int seconds = (int) (duration / 1000) % 60 ;
		int minutes = (int) ((duration / (1000*60)) % 60);
		String finishStamp = null;
		if(this.maze.getFinishVertex() != null && this.maze.getCheckpointVertex() != null){
			this.followEfficientlyPath(pf.findShortestPath(current, this.maze.getCheckpointVertex()));
			long startFinish = System.currentTimeMillis();
			this.followEfficientlyPath(pf.findShortestPath(current, this.maze.getFinishVertex()));
			long endFinish = System.currentTimeMillis();
			long finishDuration = endFinish-startFinish;
			int finishseconds = (int) (finishDuration / 1000) % 60 ;
			int finishminutes = (int) ((finishDuration / (1000*60)) % 60);
			finishStamp = (finishminutes<10 ? "0"+finishminutes : finishminutes)+":"+(finishseconds<10 ? "0"+finishseconds : finishseconds);
		}else{
			driver.sendError("No finish tile was scanned.");
		}
		StringBuilder str = new StringBuilder();
		str.append("It took "+(minutes<10 ? "0"+minutes : minutes)+":"+(seconds<10 ? "0"+seconds : seconds)+" to explore the maze.");
		if(finishStamp != null){
			str.append("\nIt took "+finishStamp+" to reach the finish tile.");
		}
		driver.sendMessage(str.toString(), "Maze explored !");
		
		//this.followPath(pf.findShortestPath(current, maze.getVertex(0,3)));
		
	}
	/**
	 * Check for tiles that still need to be checked for barcodes. And process them if necessary.
	 * 
	 * @throws CalibrationException
	 * @throws InterruptedException
	 * @throws ActionException
	 * @throws DriverException
	 */
	private void processBarcodes() throws CalibrationException,
			InterruptedException, ActionException, DriverException {
		if(stillCheckForBarcode.size()>0){
			for(Tile t:stillCheckForBarcode){
				this.followEfficientlyPath(pf.findShortestPath(current,t));;
				final int barcode = scanBarcode(current);
				if (barcode > 0) {
					this.barcodeExecuter.executeBarcode(barcode, current);
				}
				
			}
		}
	}
	/**
	 * Follow a given path of tiles.
	 * 
	 * @param path
	 * @throws CalibrationException
	 * @throws InterruptedException
	 * @throws ActionException
	 */
	@Deprecated
	private void followPath(List<Tile> path) throws CalibrationException, InterruptedException, ActionException{
		for(Tile t : path){
			this.moveTo(t);
			this.current = t;
		}
		
	}
	
	/**
	 * Move to a given next tile.
	 * 
	 * @param next
	 * @throws InterruptedException
	 * @throws ActionException 
	 * @throws CalibrationException 
	 */
	private void moveTo(Tile next) throws InterruptedException, ActionException, CalibrationException {
		if(next.equals(current)){
			driver.sendError("Tiles are the same");
		}
		if(next.isEastFrom(this.current)){
			this.travelEast();
		}else if(next.isWestFrom(this.current)){
			this.travelWest();
		}else if(next.isNorthFrom(this.current)){
			this.travelNorth();
		}else if(next.isSouthFrom(this.current)){
			this.travelSouth();
		}else{
			driver.sendError("[EXCEPTION]-Something strange happend.");
		}
		this.current = next;
		
	}
	
	
	/**
	 * Move forward , every 4 tiles orientate the robot.
	 * 
	 * @throws InterruptedException
	 * @throws ActionException 
	 */
	private void moveForward() throws InterruptedException, ActionException, CalibrationException {
		this.driver.moveForward(400F, true);
		driver.modifyOrientation();
	}
	/**
	 * Let the robot travel south.
	 * 
	 * @throws InterruptedException
	 * @throws ActionException 
	 */
	private void travelSouth() throws InterruptedException, ActionException, CalibrationException {
		switch(moveDirection){
			case DOWN:
				break;
			case LEFT:
				this.driver.turnLeft(90F, true);
				break;
			case RIGHT:
				this.driver.turnRight(90F,true);
				break;
			case UP:
				this.driver.turnRight(180F,true);
				break;
			default:
				break;
			
		}
		this.moveForward();
		this.moveDirection = Direction.DOWN;
		this.headDirection = moveDirection;
		
	}
	/**
	 * Let the robot travel west.
	 * 
	 * @throws InterruptedException
	 * @throws ActionException 
	 * @throws CalibrationException 
	 */
	private void travelWest() throws InterruptedException, ActionException, CalibrationException {
		switch(moveDirection){
			case DOWN:
				this.driver.turnRight(90F, true);
				
				break;
			case LEFT:
				
				break;
			case RIGHT:
				this.driver.turnRight(180F, true);
				
				break;
			case UP:
				this.driver.turnLeft(90F,true);

				break;
			default:
				break;
		
		}
		this.moveForward();
		this.moveDirection = Direction.LEFT;
		this.headDirection = this.moveDirection;
		
	}
	/**
	 * Let the robot travel north.
	 * 
	 * @throws InterruptedException
	 * @throws ActionException 
	 * @throws CalibrationException 
	 */
	private void travelNorth() throws InterruptedException, ActionException, CalibrationException {
		switch(moveDirection){
			case DOWN:
				this.driver.turnRight(180F, true);
				
				break;
			case LEFT:
				this.driver.turnRight(90F, true);
				
				break;
			case RIGHT:
				this.driver.turnLeft(90F, true);
				
				break;
			case UP:
				
				break;
			default:
				break;
			
		}
		this.moveForward();
		this.moveDirection = Direction.UP;
		this.headDirection = this.moveDirection;
	}
	/**
	 * Let the robot travel east.
	 * 
	 * @throws InterruptedException
	 * @throws ActionException 
	 * @throws CalibrationException 
	 */
	private void travelEast() throws InterruptedException, ActionException, CalibrationException {
		switch(moveDirection){
			case DOWN:
				this.driver.turnLeft(90F, true);
				break;
			case LEFT:
				this.driver.turnRight(180F,true);
				break;
			case RIGHT:
				break;
			case UP:
				this.driver.turnRight(90F, true);		
				break;
			default:
				break;
			
		}
		
		this.moveForward();
		this.moveDirection = Direction.RIGHT;
		this.headDirection = moveDirection;
		
	}
	/**
	 * Always give right or ahead or left or back. If black spots are detected, give these priority as next tile if they are reachable.
	 * 
	 * @return
	 */
	private Tile determineNextTile() {
		if(this.blackSpots != null){
			Iterator<Tile> iter = this.blackSpots.iterator();
			while(iter.hasNext()){
				Tile t = iter.next();
				if(t.isNeighborFrom(current)){
					iter.remove();
					return t;
				}
			}
		}
		switch(moveDirection){
			case DOWN:
				if(current.getBorderWest() == Border.OPEN){
					return maze.getVertex(current.getX()-1,current.getY());
				}
				if(current.getBorderSouth() == Border.OPEN){
					return maze.getVertex(current.getX(),current.getY()-1);
				}
				if(current.getBorderEast()==Border.OPEN){
					return maze.getVertex(current.getX()+1,current.getY());
				}
				if(current.getBorderNorth() == Border.OPEN){
					return maze.getVertex(current.getX(),current.getY()+1);
					
				}
				break;
			case LEFT:
				if(current.getBorderNorth() == Border.OPEN){
					return maze.getVertex(current.getX(),current.getY()+1);
					
				}
				if(current.getBorderWest() == Border.OPEN){
					return maze.getVertex(current.getX()-1,current.getY());
				}
				if(current.getBorderSouth() == Border.OPEN){
					return maze.getVertex(current.getX(),current.getY()-1);
				}
				if(current.getBorderEast()==Border.OPEN){
					return maze.getVertex(current.getX()+1,current.getY());
				}
				
				break;
			case RIGHT:
				if(current.getBorderSouth() == Border.OPEN){
					return maze.getVertex(current.getX(),current.getY()-1);
				}
				if(current.getBorderEast()==Border.OPEN){
					return maze.getVertex(current.getX()+1,current.getY());
				}
				if(current.getBorderNorth() == Border.OPEN){
					return maze.getVertex(current.getX(),current.getY()+1);
					
				}
				if(current.getBorderWest() == Border.OPEN){
					return maze.getVertex(current.getX()-1,current.getY());
				}
				break;
			case UP:
				if(current.getBorderEast()==Border.OPEN){
					return maze.getVertex(current.getX()+1,current.getY());
				}
				if(current.getBorderNorth() == Border.OPEN){
					return maze.getVertex(current.getX(),current.getY()+1);
					
				}
				if(current.getBorderWest() == Border.OPEN){
					return maze.getVertex(current.getX()-1,current.getY());
				}
				if(current.getBorderSouth() == Border.OPEN){
					return maze.getVertex(current.getX(),current.getY()-1);
				}
				
				break;
		}
		
		return null;
		
	}
	/**
	 * Check if a wall is present in front of the robot.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	private Boolean checkForWall() throws InterruptedException{
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
	/**
	 * Initialize the robot and the root tile it is standing on.
	 * @throws InterruptedException
	 */
	private void initializeRootTile() throws InterruptedException{
		Tile root = new Tile(0,0);
		this.checkEfficicientlyTile(root);
		//Tile root = this.exploreTile(new Tile(0,0));
		this.maze.setRootTile(root);
		this.maze.addVerticies(root.getAbsoluteNeighbors());
		driver.sendTile(root);
		this.current = root;
		//this.processUnExploredTiles();
	}
	
	/**
	 * Check if a given tile has unvisited neighbors.
	 * @param t
	 * @return
	 */
	private boolean hasUnvisitedNeighbors(Tile t){
		for(Tile n : t.getNeighbors()){
			if(!maze.getVertex(n.getX(), n.getY()).isExplored()){
				return true;
			}
		}
		
		return false;
		
	}
	/**
	 * Check if the graph still has unvisited neighbors left.
	 * @return
	 */
	private boolean graphHasUnvisitedNeighbors(){
		for(Tile t : this.maze.getVerticies()){
			if(t.isExplored()){
				if(hasUnvisitedNeighbors(t)){
					return true;
				}
			}
		}
		
		return false;
	}
	/**
	 * Find black spots in the currently used maze.
	 * 
	 */
	private void findBlackSpots(){
		this.blackSpots = new ArrayList<Tile>();
		for(Tile t : this.maze.getVerticies()){
			if(!t.isExplored()){
				blackSpots.add(t);
			}
		}
		
	}
	/**
	 * Check efficiently a given tile.
	 * 
	 * @param t
	 * @throws InterruptedException
	 */
	private void checkEfficicientlyTile(Tile t) throws InterruptedException{
		for(Direction d : this.getBordersToBeChecked(t)){
			checkBorder(d,t);
		}
	}
	/**
	 * Check the border from a given tile in a given direction.
	 * 
	 * @param d
	 * @param t
	 * @throws InterruptedException
	 */
	private void checkBorder(Direction d, Tile t) throws InterruptedException {
		Tile neighbor = this.getNeighborForGivenDirection(d,t);
		Border flag = Border.OPEN;
		if(wallInDirection(d)){
			flag = Border.CLOSED;
		}
		switch(d){
		case DOWN:
			t.setBorderSouth(flag);
			neighbor.setBorderNorth(flag);
			break;
		case LEFT:
			t.setBorderWest(flag);
			neighbor.setBorderEast(flag);
			break;
		case RIGHT:
			t.setBorderEast(flag);
			neighbor.setBorderWest(flag);
			break;
		case UP:
			t.setBorderNorth(flag);
			neighbor.setBorderSouth(flag);
			break;
		default:
			throw new IllegalStateException("Woops, the world has collapsed.");
		
		}
		driver.sendTile(t);
		driver.sendTile(neighbor);
		if(neighbor.getBarCode()!=-1 && neighbor.canHaveBarcode()){
			this.stillCheckForBarcode.add(neighbor);
		}
	}
	/**
	 * Ask the neighboring tile in a given direction and given tile.
	 * 
	 * @param d
	 * @param t
	 * @return
	 */
	private Tile getNeighborForGivenDirection(Direction d,Tile t) {
		this.maze.addVerticies(t.getAbsoluteNeighbors());
		for(Tile n : this.maze.getAbsoluteNeighborsFrom(t)){
			switch(d){
				case DOWN:
					if(n.isSouthFrom(t)){
						return n;
					}
					break;
				case LEFT:
					if(n.isWestFrom(t)){
						return n;
					}
					break;
				case RIGHT:
					if(n.isEastFrom(t)){
						return n;
					}
					break;
				case UP:
					if(n.isNorthFrom(t)){
						return n;
					}
					break;
				default:
					break;
			
			}
		}
		
		throw new IllegalStateException("Oh dear, shouldn't happen.");
	}
	/**
	 * Check for a wall in a given direction.
	 * 
	 * @param d
	 * @return true if a wall is detected, false otherwise.
	 * @throws InterruptedException
	 */
	private boolean wallInDirection(Direction d) throws InterruptedException{
		boolean wall;
		switch(this.headDirection){
		case DOWN:
			if(d == Direction.DOWN){
				wall = checkForWall();
				
			}else if(d == Direction.LEFT){
				driver.turnHeadClockWise(90);
				wall = checkForWall();
				driver.turnHeadCounterClockWise(90);
			}else if(d == Direction.RIGHT){
				driver.turnHeadCounterClockWise(90);
				wall = checkForWall();
				driver.turnHeadClockWise(90);
			}else{
				driver.turnHeadClockWise(180);
				wall = checkForWall();
				driver.turnHeadCounterClockWise(180);
			}
			return wall;
		case LEFT:
			if(d == Direction.DOWN){
				driver.turnHeadCounterClockWise(90);
				wall = checkForWall();
				driver.turnHeadClockWise(90);
			}else if(d == Direction.LEFT){
				wall = checkForWall();
			}else if(d == Direction.RIGHT){
				driver.turnHeadClockWise(180);
				wall = checkForWall();
				driver.turnHeadCounterClockWise(180);
			}else{
				driver.turnHeadClockWise(90);
				wall = checkForWall();
				driver.turnHeadCounterClockWise(90);
			}
			return wall;
		case RIGHT:
			if(d == Direction.DOWN){
				
				driver.turnHeadClockWise(90);
				wall = checkForWall();
				driver.turnHeadCounterClockWise(90);
				
			}else if(d == Direction.LEFT){
				driver.turnHeadClockWise(180);
				wall = checkForWall();
				driver.turnHeadCounterClockWise(180);
			}else if(d == Direction.RIGHT){
				wall = checkForWall();
			}else{
				driver.turnHeadCounterClockWise(90);
				wall = checkForWall();
				driver.turnHeadClockWise(90);
			}
			return wall;
		case UP:
			if(d == Direction.DOWN){
				driver.turnHeadClockWise(180);
				wall = checkForWall();
				driver.turnHeadCounterClockWise(180);
			}else if(d == Direction.LEFT){
				driver.turnHeadCounterClockWise(90);
				wall = checkForWall();
				driver.turnHeadClockWise(90);
			}else if(d == Direction.RIGHT){
				driver.turnHeadClockWise(90);
				wall =checkForWall();
				driver.turnHeadCounterClockWise(90);
			}else{
				wall = checkForWall();
			}
			return wall;
		
		}
		throw new IllegalStateException("Should not happen here.");
	}
	/**
	 * Determine which borders still need to be checked for a given tile.
	 * 
	 * @param t
	 * @return list of directions the tile still needs to be checked.
	 */
	private List<Direction> getBordersToBeChecked(Tile t){
		List<Direction> directions = new ArrayList<Direction>();
		if(t.getBorderEast()==Border.UNKNOWN){
			directions.add(Direction.RIGHT);
		}
		if(t.getBorderNorth()==Border.UNKNOWN){
			directions.add(Direction.UP);
		}
		if(t.getBorderSouth() == Border.UNKNOWN){
			directions.add(Direction.DOWN);
		}
		if(t.getBorderWest() == Border.UNKNOWN){
			directions.add(Direction.LEFT);
		}
		
		return directions;
	}
	/**
	 * Scan a given tile for a barcode.
	 * 
	 * @param tile
	 * @return
	 * @throws ActionException
	 * @throws DriverException
	 * @throws InterruptedException
	 */
	private final int scanBarcode(final Tile tile)
			throws ActionException, DriverException, InterruptedException {
		int barcode = tile.getBarCode();
		if (barcode == 0) {
			// The tile has been checked before,
			// and it has no barcode
			return -1;
		}
		if (barcode > 0) {
			// The tile has been checked before,
			// and it has a valid barcode
			return barcode;
		}
		
		final ReadBarcodeAction reader = new ReadBarcodeAction(tile);
		
		final int speed = driver.getSpeed();
		reader.execute(driver);
		driver.setSpeed(speed);
		
		barcode = reader.getBarcode();
		if (barcode <= 0) {
			// Remember to not check this tile again
			tile.setBarCode(0);
			return -1;
		}
		
		tile.setBarCode(barcode);
		driver.sendTile(tile);
		return barcode;
	}
	
	private void followEfficientlyPath(List<Tile> path) throws CalibrationException, InterruptedException, ActionException{
		this.driver.setSpeed(100);
		ArrayList<Tile> straightLine = new ArrayList<Tile>();
		for(Tile t : path){
			if(this.isOnStraighLine(t)){
				straightLine.add(t);
			}else{
				if(straightLine.size()>0){
					int distanceForward = straightLine.size()*400;
					this.driver.moveForward(distanceForward,true);
				}
				this.moveTo(t);
				straightLine.clear();
			}
			this.current = t;
		}
		
	}
	
	private boolean isOnStraighLine(Tile t){
		return t.equals(getNeighborForGivenDirection(this.moveDirection, this.current));
	}
	
}