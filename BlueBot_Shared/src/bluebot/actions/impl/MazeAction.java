package bluebot.actions.impl;


import java.util.ArrayList;
import java.util.List;

import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.graph.Border;
import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Tile;



/**
 * {@link Action} implementation for the maze exploration algorithm
 */
public class MazeAction extends Action {
	private Graph maze;
	private Direction headDirection;
	private Direction moveDirection;
	private Driver driver;
	private static final int DISTANCE_EDGE = 35;
	private Tile currentTile;
	private WhiteLineAction wa;
	public MazeAction(){
		this.maze = new Graph();
		wa = new WhiteLineAction();
	}
	public void execute(final Driver driver) throws InterruptedException {
		this.driver = driver;
		driver.setSpeed(50);
		this.initializeRootTile();
		while(true){
			Tile nextTile = getNextTile();
			driver.sendDebug("Moving to "+nextTile.toString());
			this.moveTo(nextTile);
			if(!this.maze.hasVertex(nextTile)){
				this.exploreTile(nextTile);
				this.maze.addVertex(nextTile);
			}
			
			if(checkDeadTile(nextTile)){
				driver.sendDebug("Dead end reached");
				return;
			}
			
		}
		
	}
	
	private boolean checkDeadTile(Tile t){
		int counter = 0;
		if(t.getBorderEast() == Border.OPEN){
			counter++;
		}
		if(t.getBorderNorth()== Border.OPEN){
			counter++;
		}
		
		if(t.getBorderSouth()==Border.OPEN){
			counter++;
		}
		if(t.getBorderWest()==Border.OPEN){
			counter++;
		}
		
		return !(counter>1);
	}
	private Tile getNextTile(){
		int x = currentTile.getX();
		int y = currentTile.getY();
		List<Tile> neighbors = new ArrayList<Tile>();
		if(this.currentTile.getBorderEast()==Border.OPEN)
			neighbors.add(new Tile(x+1,y));
		if(this.currentTile.getBorderNorth()==Border.OPEN)
			neighbors.add(new Tile(x,y+1));
		if(this.currentTile.getBorderSouth()==Border.OPEN)
			neighbors.add(new Tile(x,y-1));
		if(this.currentTile.getBorderWest()==Border.OPEN)
			neighbors.add(new Tile(x-1,y));
		
		if(neighbors.size() != 0){
			return neighbors.get(0);
		}
		
		return null;
		
	}
	private void initializeRootTile() throws InterruptedException{
		this.headDirection=Direction.UP;
		this.moveDirection=Direction.UP;
		Tile root = this.exploreTile(new Tile(0,0));
		this.maze.setRootTile(root);
		driver.sendTile(root);
		this.currentTile = root;
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
		return t;
		
	}
	
	private Boolean checkForWall() throws InterruptedException{
		Thread.sleep(1000);
		int dist = driver.readSensorUltraSonic();
		if(dist < 25){
			return true;
		}else if(dist > 30){
			return false;
		}else{
			driver.turnHeadCounterClockWise(5);
			Thread.sleep(200);
			int dist1 = driver.readSensorUltraSonic();
			driver.turnHeadClockWise(10);
			Thread.sleep(200);
			int dist2 = driver.readSensorUltraSonic();
			driver.turnHeadCounterClockWise(5);
			if(dist1 < 25 || dist2 < 25){
				return true;
			}
			return false;
		}
		

	}
	
	private void moveTo(Tile nextTile) throws InterruptedException{
		Direction nextDir = this.moveDirection;
		if(nextTile.isEastFrom(currentTile)){
			nextDir = Direction.RIGHT;
		}
		else if(nextTile.isNorthFrom(currentTile)){
			nextDir = Direction.UP;
		}
		else if(nextTile.isSouthFrom(currentTile)){
			nextDir = Direction.DOWN;
		}
		else if(nextTile.isWestFrom(currentTile)){
			nextDir = Direction.LEFT;
		}
		
		this.turnTo(nextDir);
		wa.execute(driver);
		driver.setSpeed(50);
		this.driver.moveForward(200F, true);
		this.currentTile = nextTile;
		this.headDirection = moveDirection;
	}
	
	private void turnTo(Direction other){
		driver.sendDebug("Current moving : "+moveDirection.toString());
		driver.sendDebug("Turning : "+other.toString());
		
		if(!moveDirection.equals(other)){
			if(moveDirection.turnCWise().equals(other)){
				driver.turnRight(90F, true);
				this.moveDirection=moveDirection.turnCWise();
				return;
			}
			
			else if(moveDirection.turnCCWise().equals(other)){
				driver.turnLeft(90F, true);
				this.moveDirection = moveDirection.turnCCWise();
				return;
			}
			
			else{
				driver.turnRight(180F, true);
				this.moveDirection = moveDirection.turnCWise();
				this.moveDirection = moveDirection.turnCWise();
				return;
			}
		}
		
	}
}