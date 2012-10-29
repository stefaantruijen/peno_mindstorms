package bluebot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import bluebot.core.PilotController;
import bluebot.graph.Border;
import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Vertex;

import bluebot.graph.Tile;

/**
 * Explore a maze.
 * 
 * @author  Incalza Dario
 *
 */
public class MazeExplorer implements Runnable {
	private final PilotController pc;
	private final UltrasonicSensor us;
	private final static int DISTANCE_TO_EDGE = 17; //(cm)
	private Direction currentDirection = Direction.UP;
	private Graph maze;
	private Tile currentTile;
	
	public MazeExplorer(PilotController pc){
		this.pc = pc;
		this.us = new UltrasonicSensor(SensorPort.S2);
		this.maze = new Graph();
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MazeExplorer exp = new MazeExplorer(new PilotController());
		exp.run();

	}

	@Override
	public void run() {
		System.out.println("Exploring maze..");
		currentTile = initialize();
		while(true){
			Tile nextTile = this.getRandomNextTile(currentTile);
			//System.out.println("Next tile = "+nextTile);
			if(nextTile == null){
				//in a deadEnd
			}else{
				this.checkTile(nextTile);
				Vertex next = new Vertex(nextTile);
				Vertex current = new Vertex(currentTile);
				this.maze.addVertex(next);
				this.maze.insertBiEdge(current,next);
				this.moveTo(currentTile, nextTile);
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
	}
	/**
	 * Move one tile east.
	 */
	private void moveEast(){
		this.turnClockwise();
		this.pc.moveForward(400F);
	}
	/**
	 * Move one tile west.
	 */
	private void moveWest(){
		this.turnCounterClockwise();
		this.pc.moveForward(400F);
	}
	/**
	 * Move one tile north.
	 */
	private void moveNorth(){
		this.pc.moveForward(400F);
	}
	/**
	 * Move one tile south.
	 */
	private void moveSouth(){
		this.turnClockwise();
		this.turnClockwise();
		this.pc.moveForward(400F);
	}
	/**
	 * Get the ultrasonic sensor.
	 * @return
	 */
	public UltrasonicSensor getUs() {
		return us;
	}
	/**
	 * Check the borders for a given tile.
	 * 
	 * @param tile
	 */
	private void checkTile(Tile tile){
		for(int i = 0;i<=3;i++){
			System.out.println("Distance = "+getUs().getDistance());
			boolean wall = (getUs().getDistance() <= 20);
			
			switch(currentDirection){
				case DOWN:
					if(wall){
						tile.setBorderSouth(Border.CLOSED);
					}else
						tile.setBorderSouth(Border.OPEN);
					break;
				case LEFT:
					if(wall){
						tile.setBorderEast(Border.CLOSED);
					}else
						tile.setBorderEast(Border.OPEN);
					break;
				case RIGHT:
					if(wall){
						tile.setBorderWest(Border.CLOSED);
					}else
						tile.setBorderWest(Border.OPEN);
					break;
				case UP:
					if(wall){
						tile.setBorderNorth(Border.CLOSED);
					}else
						tile.setBorderNorth(Border.OPEN);
					break;
				default:
					break;
			}
			
			System.out.println(currentDirection+"="+wall);
			this.turnClockwise();
			
		}
	}
	/**
	 * Get a random next tile from the current tile.
	 * 
	 * @param current
	 * @return
	 */
	private Tile getRandomNextTile(Tile current){
		ArrayList<Tile> possibilities = new ArrayList<Tile>();
		if(current.getBorderEast()==Border.OPEN){
			possibilities.add(new Tile(current.getX()-1,current.getY()));
		}
		
		if(current.getBorderWest() == Border.OPEN){
			possibilities.add(new Tile(current.getX()+1,current.getY()));
		}
		
		if(current.getBorderNorth() == Border.OPEN){
			possibilities.add(new Tile(current.getX(),current.getY()+1));
		}
		
		if(current.getBorderSouth() == Border.OPEN){
			possibilities.add(new Tile(current.getX(),current.getY()-1));
		}
		
		System.out.println("size = "+possibilities.size());
		Iterator<Tile> iter = possibilities.iterator();
		while(iter.hasNext()){
			Tile t = iter.next();
			if(maze.hasVertex(t) && currentTile.isNeighborFrom(t)){
				this.maze.insertBiEdge(new Vertex(currentTile), new Vertex(t));
				iter.remove();
			}
		}
		
		Random r = new Random();
		if(possibilities.size() > 0){
			int x = r.nextInt(possibilities.size());
			System.out.println("x = "+x);
			return possibilities.get(x);
		}
		
		return null;
		
	}

	/**
	 * Return the current direction the robot has while exploring the maze.
	 * @return
	 */
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	/**
	 * Initiliaze the root tile from the maze.
	 * @return
	 * 			The root tile.
	 */
	public Tile initialize(){
		Tile first = new Tile(0,0);
		this.checkTile(first);
		this.maze.setRootVertex(new Vertex(first));
		System.out.println("First tile : "+first);
		return first;
	}


	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}
	/**
	 * Turn this robot clockwise and update currentDirection.
	 */
	private void turnClockwise(){
		pc.turnRight(90);
		this.currentDirection = currentDirection.turnCWise();
	}
	/**
	 * Turn this robot counterclockwise and update currentDirection.
	 */
	private void turnCounterClockwise(){
		pc.turnLeft(90);
		this.currentDirection = currentDirection.turnCCWise();
	}


	/**
	 * 
	 * @return
	 */
	public static int getDistanceToEdge() {
		return DISTANCE_TO_EDGE;
	}


}