package bluebot;

import java.util.ArrayList;
import java.util.List;

import bluebot.graph.Border;
import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Tile;

/**
 * Explore a maze.
 * 
 * @author  Incalza Dario
 *
 */
public class MazeExplorer implements Runnable {
	private final Driver robot;
	private final static int DISTANCE_TO_EDGE = 35; //(cm)
	private Direction currentMoveDirection;
	private Direction currentLookDirection;
	private Graph maze;
	private Tile currentTile;
	private List<Tile> tilesToDeadEnd;
	
	public MazeExplorer(Driver robot){
		this.robot = robot;
		this.maze = new Graph();
		this.tilesToDeadEnd = new ArrayList<Tile>();
		this.robot.setSpeed(50);
		this.currentMoveDirection = Direction.UP;
		this.currentLookDirection = Direction.UP;
	}
	
	@Override
	public void run() {
		boolean done = false;
		robot.sendDebug("Exploring maze..");
		currentTile = initialize();
		this.robot.sendTile(currentTile);
		while(true){
			Tile nextTile = this.getNextTile(currentTile);
			if(nextTile == null){
				//detected a deadend tile.
				tilesToDeadEnd.add(currentTile);
				robot.sendDebug("Dead end");
			}
			else{
				this.moveTo(currentTile, nextTile);
				if(!this.maze.hasVertex(nextTile)){
					this.checkTile(nextTile);
					this.maze.addVertex(nextTile);
					this.maze.addEdge(currentTile,nextTile);
					this.robot.sendTile(nextTile);
					this.robot.sendDebug(nextTile.toString());
				}
					
			}
		}
		
		
	}
	/**
	 * Move from a given current tile to a given other tile. Always just one step.
	 * 
	 * @param current
	 * @param other
	 */
	private void moveTo(Tile current,Tile other){
		if(current.isEastFrom(other)){
			this.moveEast();
		}
		
		if(current.isWestFrom(other)){
			this.moveWest();
		}
		
		if(current.isSouthFrom(other)){
			this.moveSouth();
		}
		
		if(current.isNorthFrom(other)){
			this.moveNorth();
		}
		
		this.currentTile = other;
		this.currentLookDirection = this.currentMoveDirection;
		robot.sendDebug("Moving : "+this.currentMoveDirection);
		robot.sendDebug("Looking : "+ this.currentLookDirection);
	}
	/**
	 * Move one tile east.
	 */
	private void moveEast(){
		this.robot.turnRight(90, true);
		this.currentMoveDirection = currentMoveDirection.turnCWise();
		this.robot.moveForward(400F,true);
	}
	/**
	 * Move one tile west.
	 */
	private void moveWest(){
		this.robot.turnLeft(90, true);
		this.currentMoveDirection = currentMoveDirection.turnCCWise();
		this.robot.moveForward(400F,true);
	}
	/**
	 * Move one tile north.
	 */
	private void moveNorth(){
		this.robot.moveForward(400F,true);
	}
	/**
	 * Move one tile south.
	 */
	private void moveSouth(){
		this.robot.turnRight(90, true);
		this.currentMoveDirection = currentMoveDirection.turnCWise();
		this.robot.turnRight(90, true);
		this.currentMoveDirection = currentMoveDirection.turnCWise();
		this.robot.moveForward(400F,true);
	}
	
	/**
	 * Check the borders for a given tile.
	 * 
	 * @param tile
	 */
	private void checkTile(Tile tile){
		for(int i = 0;i<=3;i++){
			int dist = robot.readSensorUltraSonic();
			boolean wall = (dist <= getDistanceToEdge());
			
			switch(currentLookDirection){
				case DOWN:
					if(wall){
						tile.setBorderSouth(Border.CLOSED);
					}else
						tile.setBorderSouth(Border.OPEN);
					this.turnCounterClockwise();
					this.turnCounterClockwise();
					this.turnCounterClockwise();
					
					break;
				case LEFT:
					if(wall){
						tile.setBorderEast(Border.CLOSED);
					}else
						tile.setBorderEast(Border.OPEN);
					this.turnClockwise();
					break;
				case RIGHT:
					if(wall){
						tile.setBorderWest(Border.CLOSED);
					}else
						tile.setBorderWest(Border.OPEN);
					this.turnClockwise();
					break;
				case UP:
					if(wall){
						tile.setBorderNorth(Border.CLOSED);
					}else
						tile.setBorderNorth(Border.OPEN);
					
					this.turnClockwise();
					break;
				default:
					break;
			}
			
			this.robot.sendDebug(currentLookDirection.toString()+ " = "+wall);
		}
	}
	/**
	 * Get a random next tile from the current tile.
	 * 
	 * @param current
	 * @return
	 */
	private Tile getNextTile(Tile current){
		ArrayList<Tile> possibilities = new ArrayList<Tile>();
		if(current.getBorderEast()==Border.OPEN){
			possibilities.add(new Tile(current.getX()+1,current.getY()));
		}
		
		if(current.getBorderWest() == Border.OPEN){
			possibilities.add(new Tile(current.getX()-1,current.getY()));
		}
		
		if(current.getBorderNorth() == Border.OPEN){
			possibilities.add(new Tile(current.getX(),current.getY()+1));
		}
		
		if(current.getBorderSouth() == Border.OPEN){
			possibilities.add(new Tile(current.getX(),current.getY()-1));
		}
		
		if(possibilities.size() > 0){
			for(Tile t : possibilities){
				if(!this.tilesToDeadEnd.contains(t)){
					return t;
				}
			}
		}
		
		
		return null;
		
	}

	/**
	 * Return the current direction the robot has while exploring the maze.
	 * @return
	 */
	public Direction getCurrentMoveDirection() {
		return currentMoveDirection;
	}
	/**
	 * Initiliaze the root tile from the maze.
	 * @return
	 * 			The root tile.
	 */
	public Tile initialize(){
		this.robot.sendDebug("Initializing root tile");
		Tile first = new Tile(0,0);
		this.checkTile(first);
		this.maze.setRootTile(first);
		
		return first;
	}



	/**
	 * Turn this robot clockwise and update currentDirection.
	 */
	private void turnClockwise(){
		robot.turnHeadClockWise(90);
		this.currentLookDirection = currentLookDirection.turnCWise();
	}
	/**
	 * Turn this robot counterclockwise and update currentDirection.
	 */
	private void turnCounterClockwise(){
		robot.turnHeadCounterClockWise(90);
		this.currentLookDirection = currentLookDirection.turnCCWise();
	}


	/**
	 * 
	 * @return
	 */
	public static int getDistanceToEdge() {
		return DISTANCE_TO_EDGE;
	}


}
