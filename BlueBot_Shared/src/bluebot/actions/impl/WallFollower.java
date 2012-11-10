package bluebot.actions.impl;

import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.graph.Border;
import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Tile;

public class WallFollower extends Action{
		private Driver driver;
		private final Graph maze;
		private Direction headDirection,moveDirection;
		private Tile current;
		
		public WallFollower(){
			this.maze = new Graph();
			this.headDirection=Direction.UP;
			this.moveDirection=Direction.UP;
		}
		
		@Override
		public void execute(Driver driver) throws InterruptedException {
			this.driver = driver;
			this.initializeRootTile();
			
			do{
				Tile next = this.determineNextTile();
				this.moveTo(next);
				if(!next.isExplored()){
					this.exploreTile(next);
				}
				this.maze.addEdge(current, next);
				this.maze.addVerticies(next.getNeighbors());
				this.current = next;
				driver.sendDebug("CURRENT TILE : "+current.toString());
			}while(current != maze.getRootTile());
			
			
		}
		
		private void moveTo(Tile next) {
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
			}
		}
		
		private void moveForward() {
			this.driver.moveForward(400F, true);
			driver.sendDebug("MOVE FORWARD");
		}
		
		private void travelSouth() {
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
		
		private void travelWest() {
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
		private void travelNorth() {
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
		
		private void travelEast() {
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
		 * Always give right or ahead or left or back.
		 * 
		 * @return
		 */
		private Tile determineNextTile() {
			
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
		private Boolean checkForWall(){
			int dist = driver.readSensorUltraSonic();
			if(dist < 25){
				return true;
			}else if(dist > 30){
				return false;
			}else{
				driver.turnHeadCounterClockWise(5);
				int dist1 = driver.readSensorUltraSonic();
				driver.turnHeadClockWise(10);
				int dist2 = driver.readSensorUltraSonic();
				driver.turnHeadCounterClockWise(5);
				if(dist1 < 25 || dist2 < 25){
					return true;
				}
				return false;
			}
			
		}
		
		private void initializeRootTile() throws InterruptedException{
			
			Tile root = this.exploreTile(new Tile(0,0));
			this.maze.setRootTile(root);
			this.maze.addVerticies(root.getNeighbors());
			driver.sendTile(root);
			this.current = root;
		}
		
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
				driver.sendDebug(headDirection.toString()+" = "+checkForWall().toString());
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
	}
