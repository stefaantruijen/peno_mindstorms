package bluebot.actions.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import algorithms.Dijkstra;
import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.graph.Border;
import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Tile;

public class WallFollower extends Action{
		private Driver driver;
		private final Graph maze;
		private Direction headDirection,moveDirection;
		private Tile current;
		private int tilesTravelledBetweenCalib = 0;
		private List<Tile> blackSpots;
		
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
		 */
		@Override
		public void execute(Driver driver) throws InterruptedException, ActionException {
			this.driver = driver;
			this.driver.setSpeed(80);
			this.driver.resetOrientation();
			this.initializeRootTile();
			do{
				if(isAborted()){
					return;
				}
				Tile next = this.determineNextTile();
				this.moveTo(next);
				if(!next.isExplored()){
					this.exploreTile(next);
				}
				this.maze.addEdge(current, next);
				this.maze.addVerticies(next.getNeighbors());
				this.current = next;
				driver.sendDebug("CURRENT TILE : "+current.toString());
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
		 */
		private void moveTo(Tile next) throws InterruptedException, ActionException {
			if(next.equals(current)){
				driver.sendDebug("Tiles are the same");
			}
			if(next.isEastFrom(this.current)){
				driver.sendDebug("TRAVELLING EAST");
				this.travelEast();
			}else if(next.isWestFrom(this.current)){
				driver.sendDebug("TRAVELLING WEST");
				this.travelWest();
			}else if(next.isNorthFrom(this.current)){
				driver.sendDebug("TRAVELLING NORTH");
				this.travelNorth();
			}else if(next.isSouthFrom(this.current)){
				driver.sendDebug("TRAVELLING SOUTH");
				this.travelSouth();
			}else{
				driver.sendError("Something strange happend.");
			}
			
		}
		/**
		 * Move forward , every 4 tiles orientate the robot.
		 * 
		 * @throws InterruptedException
		 * @throws ActionException 
		 */
		private void moveForward() throws InterruptedException, ActionException {
			//if(tilesTravelledBetweenCalib<3){
				this.driver.moveForward(400F, true);
				driver.sendDebug("MOVE FORWARD");
			/**	tilesTravelledBetweenCalib++;
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
			
			driver.modifyOrientation();**/
		}
		/**
		 * Let the robot travel south.
		 * 
		 * @throws InterruptedException
		 * @throws ActionException 
		 */
		private void travelSouth() throws InterruptedException, ActionException {
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
		 */
		private void travelWest() throws InterruptedException, ActionException {
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
		 */
		private void travelNorth() throws InterruptedException, ActionException {
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
		 */
		private void travelEast() throws InterruptedException, ActionException {
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
			
			Tile root = this.exploreTile(new Tile(0,0));
			this.maze.setRootTile(root);
			this.maze.addVerticies(root.getNeighbors());
			driver.sendTile(root);
			this.current = root;
		}
		/**
		 * Explore and update a given tile.
		 * 
		 * @param t
		 * @return
		 * @throws InterruptedException
		 */
		private Tile exploreTile(Tile t) throws InterruptedException{
			for(int i = 0;i<=3;i++){
				switch(headDirection){
					case DOWN:
						if(checkForWall()){
							t.setBorderSouth(Border.CLOSED);
						}
						break;
					case LEFT:
						if(checkForWall()){
							t.setBorderWest(Border.CLOSED);
						}
						break;
					case RIGHT:
						if(checkForWall()){
							t.setBorderEast(Border.CLOSED);
						}
						break;
					case UP:
						if(checkForWall()){
							t.setBorderNorth(Border.CLOSED);
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
			t.setExplored();
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
	}
