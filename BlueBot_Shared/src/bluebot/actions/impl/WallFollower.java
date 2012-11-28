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
 * 
 * @author Dario, Dieter
 *
 */
public class WallFollower extends Action{
		private Driver driver;
		private final Graph maze;
		private Direction headDirection,moveDirection;
		private Tile current;
		private int tilesTravelledBetweenCalib = 0;
		private List<Tile> blackSpots;
		private BarcodeExecuter barcodeExecuter;
		
		public WallFollower(){
			this.maze = new Graph();
			this.headDirection=Direction.UP;
			this.moveDirection=Direction.UP;
			this.blackSpots = null;
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
			this.driver.setSpeed(80);
			this.driver.resetOrientation();
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
						//Because the ReadBarcodeAction recalibrates when he found a barcode
						this.tilesTravelledBetweenCalib = 0;
					}
				}
				
				this.maze.addEdge(current, next);
				this.maze.addVerticies(next.getAbsoluteNeighbors());
				this.current = next;
				if(current == this.maze.getRootTile() && !hasUnvisitedNeighbors(this.maze.getRootTile())){
					this.findBlackSpots();
				}
			}while(this.hasUnvisitedNeighbors(this.maze.getRootTile())||this.hasUnvisitedNeighbors(current)||this.graphHasUnvisitedNeighbors());
			Dijkstra dijkstra = new Dijkstra(maze);
			dijkstra.execute(current);
			List<Tile> path = dijkstra.getPath(maze.getVertex(3, 5));
			path.remove(0);
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
			
		}
		/**
		 * This makes the algorithm 'smarter'. It will check if we can gather information about unexplored tiles from tiles we already have explored.
		 */
		@Deprecated
		private void processUnExploredTiles(){
			for(Tile t : this.maze.getUnExploredTiles()){
				this.driver.sendTile(t);
				for(Tile t2 : this.maze.getVerticies()){
					if(t.isAbsoluteNeighborFrom(t2)){
						determineBorders(t,t2);
					}
				}
			}
		}
		/**
		 * Determine the already known border information for 2 given tiles and update the unexplored tile.
		 * 
		 * @param t
		 * @param t2
		 */
		private void determineBorders(Tile t, Tile t2) {
			if(t.isWestFrom(t2)){
				t.setBorderEast(t2.getBorderWest());
				t2.setBorderWest(t.getBorderEast());
			}
			if(t.isEastFrom(t2)){
				t.setBorderWest(t2.getBorderEast());
				t2.setBorderEast(t.getBorderWest());
			}
			
			if(t.isNorthFrom(t2)){
				t.setBorderSouth(t2.getBorderNorth());
				t2.setBorderNorth(t.getBorderSouth());
			}
			
			if(t.isSouthFrom(t2)){
				t.setBorderNorth(t2.getBorderSouth());
				t2.setBorderSouth(t.getBorderNorth());
			}
			
			driver.sendTile(t);
			driver.sendTile(t2);
		}
		/**
		 * Move forward , every 4 tiles orientate the robot.
		 * 
		 * @throws InterruptedException
		 * @throws ActionException 
		 */
		private void moveForward() throws InterruptedException, ActionException, CalibrationException {
			if(tilesTravelledBetweenCalib<3){
				this.driver.moveForward(400F, true);
				tilesTravelledBetweenCalib++;
			}else{
				this.driver.moveForward(40F, true);
				WhiteLineAction wa = new WhiteLineAction();
					driver.sendDebug("ORIENTATING");
					wa.execute(this.driver);		
				this.driver.setSpeed(80);
				this.driver.moveForward(200F, true);
				driver.sendDebug("MOVE FORWARD");
				this.tilesTravelledBetweenCalib = 0;
			}
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
		 * Explore and update a given tile.
		 * 
		 * @param t
		 * @return
		 * @throws InterruptedException
		 */
		@Deprecated
		private Tile exploreTile(Tile t) throws InterruptedException{
			for(int i = 0;i<=3;i++){
				switch(headDirection){
					case DOWN:
						if(checkForWall()){
							t.setBorderSouth(Border.CLOSED);
						}else{
							t.setBorderSouth(Border.OPEN);
						}
						break;
					case LEFT:
						if(checkForWall()){
							t.setBorderWest(Border.CLOSED);
						}else{
							t.setBorderWest(Border.OPEN);
						}
						break;
					case RIGHT:
						if(checkForWall()){
							t.setBorderEast(Border.CLOSED);
						}else{
							t.setBorderEast(Border.OPEN);	
						}
						break;
					case UP:
						if(checkForWall()){
							t.setBorderNorth(Border.CLOSED);
						}else{
							t.setBorderNorth(Border.OPEN);
						}
						break;
					default:
						break;
				
				}
				driver.turnHeadClockWise(90);
				this.headDirection = headDirection.turnCWise();
				this.driver.sendTile(t);
			}
			for(int i = 0;i<=3;i++){
				driver.turnHeadCounterClockWise(90);
				this.headDirection = headDirection.turnCCWise();
			}
			return t;
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
		}
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
		
	}
