package bluebot.simulator;

import java.util.ArrayList;
import java.util.Hashtable;

import bluebot.game.World;
import bluebot.graph.Tile;

/**
 * Class implementing the methods all sensors need.
 * 
 * @author Dieter
 * 
 *TODO: use orientation of shared: bluebot.graph?
 *
 * Methods containing 'grid' expect or return values related to Tile.getX() and Tile.getY().
 * 	The 'grid 'is only for the placement of the tiles with respect to eachother.
 * 	So (0,0) is the place of the lower left Tile.
 * 
 * Methods NOT containing 'grid' expect or return values related to Tile.getX()*TILE_SIZE and Tile.getY()*TILE_SIZE.
 * 	The "NOT 'grid'" are the actual coordinates in the plane made by ALL tiles.
 * 	Where (0,0) is the lower left point of the plane.
 * 
 */
public class Sensors {
	/**
	 * The light sensor of this Sensors object.
	 */
	private VirtualLightSensor light;
	/**
	 * The sonar of this Sensors object.
	 */
	private VirtualSonar sonar;
	/**
	 * The infrared sensor of this Sensors object.
	 */
	private VirtualInfraredSensor irsenor;
	/**
	 * The list of tiles for this Sensors object.
	 */
	private Tile[] tilesList;
	/**
	 * Hashtable containing every tile hashed with Sensors.hash().
	 */
	protected Hashtable<Double, Tile> TileMap = new Hashtable<Double, Tile>() ;
	/**
	 * The maximum x value of the grid.
	 */
	private int maxXOfGrid;
	/**
	 * The maximum y value of the grid
	 */
	private int maxYOfGrid;
	/**
	 * The world of this sensors.
	 */
	private World world;
	/**
	 * The tile size.
	 */
	public static final int TILE_SIZE = VirtualRobot.TILE_SIZE_CM;
	
	

	/**
	 * Default constructor uses the Tile objects in tilesList for making the virtual sensor data.
	 * 
	 * @param tilesList
	 */
	public Sensors(World world) {
		this.world = world;
		this.tilesList = world.getMaze();
		findGridBorders();
		makeTileMap();
		light = new VirtualLightSensor(this);
		sonar = new VirtualSonar(this);
		irsenor = new  VirtualInfraredSensor(this);
	}
	
	public World getWorld(){
		return world;
	}
	
	/**
	 * Returns the light value (a percentage) at the given coordinate.
	 * @param X
	 * @param Y
	 * @return
	 */
	public int getLightValuePercentage(int X, int Y){
		return light.getLightValuePercentage(X, Y);
	}
	
	/**
	 * Returns the light value at the given coordinate.
	 * @param X
	 * @param Y
	 * @return An integer between 0 and 1023
	 */
	public int getLightValue(int X, int Y){
		return light.getLightValue(X, Y);
	}
	
	/**
	 * Returns the sonar value at the given coordinate and heading.
	 * @param X
	 * @param Y
	 * @param heading
	 * @return
	 */
	public int getSonarValue(int X, int Y, float heading){
		return sonar.getSonarValue(X, X, heading);
	}
	
	/**
	 * Gets the tile located at the given position.
	 * @param imgX 
	 * 		number b
	 * @param imgY
	 * @return
	 */
	public Tile getTileAt(int imgX, int imgY){
		int tileX = imgX/TILE_SIZE;
		int tileY = imgY/TILE_SIZE;
		return getTileOnGridAt(tileX,tileY);
	}
	
	/**
	 * 
	 * @param imgX
	 * @param imgY
	 * @return The Til object at the given coordinates in the grid.
	 * 			null if there is no tile at the given coordinates
	 */
	public Tile getTileOnGridAt(int imgX, int imgY){
			return TileMap.get(hash(imgX,imgY));
	}
	
	/**
	 * Maks a hash for the given coordinates.
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	protected Double hash(int X, int Y){
		return Math.pow(2,X)*Math.pow(3,Y);
	}
	
	/**
	 * Creates the hashtable of getTilesList(). Should be called at construct.
	 */
	private void makeTileMap(){
		for(Tile t : getTilesList()){
			Double value = hash(t.getX(),t.getY());
			TileMap.put(value, t);
		}
	}
	
	/**
	 * Returns the tile size.
	 * 
	 * @return TILE_SIZE
	 */
	public int getTileSize(){
		return TILE_SIZE;
	}
	
	/**
	 * Finds the borders of the grid of Tile objects from the tilesList of this Sensors object.
	 * 
	 * Should be called at construct.
	 */
	private void findGridBorders(){
		int maxX =0;
		int maxY =0;
		for(Tile t : tilesList){
			int X = t.getX();
			int Y = t.getY();
			if(X >maxX){
				maxX = X;
			}
			if(Y>maxY){
				maxY = Y;
			}
		}
		setMaxXOfGrid(maxX);
		setMaxYOfGrid(maxY);
	}
	
	/**
	 * Sets the maximum x-value of the grid
	 * 
	 * @param maxX
	 */
	private void setMaxXOfGrid(int maxX) {
		maxXOfGrid = maxX;
	}
	
	/**
	 * Sets the maximum y-value of the grid
	 * 
	 * @param maxX
	 */
	private void setMaxYOfGrid(int maxY) {
		maxYOfGrid = maxY;
	}
	
	/**
	 * Returns the maximum x-value of the grid of Tile objects.
	 * @return
	 */
	public int getMaxXOfGrid(){
		return maxXOfGrid;
	}
	
	/**
	 * Returns the maximum y-value of the grid of Tile objects.
	 * @return
	 */
	public int getMaxYOfGrid(){
		return maxYOfGrid;
	}
	
	/**
	 * Returns the maximum x-value a coordinate in the plane can have.
	 * 
	 * Assumption:
	 * The plane is 'zero based' so:
	 *  (0,0) is a viable coordinate.
	 *  (getMaxYOfGrid()*TILE_SIZE,0) is NOT!
	 *  This is the reason for the '-1'
	 *  
	 * @return
	 */
	public int getMaxX(){
		return (getMaxXOfGrid()+1)*TILE_SIZE-1;
	}
	
	/**
	 * Returns the maximum y-value a coordinate in the plane can have.
	 * 
	 * Assumption:
	 * The plane is 'zero based' so:
	 *  (0,0) is a viable coordinate. 
	 *  (0,getMaxYOfGrid()*TILE_SIZE) is NOT!
	 *  This is the reason for the '-1'
	 * @return
	 */
	public int getMaxY(){
		return (getMaxYOfGrid()+1)*TILE_SIZE-1;
	}
	
	/**
	 * Returns the VirtualSonar object of this Sensors.
	 * @return
	 */
	public VirtualSonar getSonar(){
		return sonar;
	}
	
	/**
	 * Returns the VirtualLightSensor object of this Sensors.
	 * @return
	 */
	public VirtualLightSensor getLightSensor(){
		return light;
	}
	
	/**
	 * Returns the VirtualInfraredSensor object of this Sensors.
	 * @return
	 */
	public VirtualInfraredSensor getIRSensor(){
		return irsenor;
	}
	
	/**
	 * Returns the list of Tile objects of this Sensors.
	 * @return
	 */
	public Tile[] getTilesList(){
		return tilesList;
	}
	
	/**
	 * Returns the tile north of the given tile.
	 * 
	 * @param t
	 * @return
	 */
	public Tile getNorthNeigborOf(Tile t){
		int x = t.getX();
		int y =t.getY();
		return getTileOnGridAt(x, y+1);
	}
	
	/**
	 * Returns the tile east of the given tile.
	 * 
	 * @param t
	 * @return
	 */
	public Tile getEastNeigborOf(Tile t){
		int x = t.getX();
		int y =t.getY();
		return getTileOnGridAt(x+1, y);
	}
	
	/**
	 * Returns the tile south of the given tile.
	 * 
	 * @param t
	 * @return
	 */
	public Tile getSouthNeigborOf(Tile t){
		int x = t.getX();
		int y =t.getY();
		return getTileOnGridAt(x, y-1);
	}
	
	/**
	 * Returns the tile west of the given tile.
	 * 
	 * @param t
	 * @return
	 */
	public Tile getWestNeigborOf(Tile t){
		int x = t.getX();
		int y =t.getY();
		return getTileOnGridAt(x-1, y);
	}
	

	/**
	 * Returns the y-coordinate of the north border of the tile in which (x,y) lies.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getNorthBorderCoor(int x, int y){
		return ((y+getTileSize())/getTileSize())*getTileSize()-1;
	}
	
	/**
	 * Returns the y-coordinate of the north border of the given tile T.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getNorthBorderCoor(Tile t){
		int x = t.getX();
		int y = t.getY();
		return getNorthBorderCoor(x,y);
	}
	
	/**
	 * Returns the x-coordinate of the east border of the tile in which (x,y) lies.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getEastBorderCoor(int x, int y){
		return ((x+getTileSize())/getTileSize())*getTileSize()-1;
	}
	
	/**
	 * Returns the x-coordinate of the east border of the given tile T.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getEastBorderCoor(Tile t){
		int x = t.getX();
		int y = t.getY();
		return getEastBorderCoor(x,y);
	}
	
	/**
	 * Returns the y-coordinate of the south border of the tile in which (x,y) lies.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getSouthBorderCoor(int x, int y){
		return (y/getTileSize())*getTileSize();
	}
	
	/**
	 * Returns the y-coordinate of the south border of the given tile T.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getSouthBorderCoor(Tile t){
		int x = t.getX();
		int y = t.getY();
		return getSouthBorderCoor(x,y);
	}
	
	/**
	 * Returns the x-coordinate of the west border of the tile in which (x,y) lies.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getWestBorderCoor(int x, int y){
		return (x/getTileSize())*getTileSize();
	}
	
	/**
	 * Returns the y-coordinate of the west border of the given tile T.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getWestBorderCoor(Tile t){
		int x = t.getX();
		int y = t.getY();
		return getWestBorderCoor(x,y);
	}
	
	/**
	 * Checks if the given x and y are valid coordinates to be used with the sensors.
	 * 
	 * @param x
	 * @param y
	 * @return 
	 */
	public boolean isValid(int x, int y) {
		boolean result = true;
		if(x<0 || x > getMaxX()){
			result = false;
		}
		if(y<0 || y > getMaxY()){
			result = false;
		}
		return result;
	}

	/**
	 * Sorts a given list of intersections by the (euclidean) distance to the given point.
	 * 
	 * @param intersections 
	 * 		must be of type: ArrayList<int[]>
	 * @param x
	 * 		The x-coordinate of the point.
	 * @param y
	 * 		The y-coordinate of the point.
	 * @return ArrayList<int[]>
	 * 		A list that contains the same elements but sorted by distance to the given point.
	 */
	protected static ArrayList<int[]> sortByDistanceTo(ArrayList<int[]> intersections, int x, int y) {
		int size = intersections.size();
		ArrayList<int[]> result = new ArrayList<int[]>();
		for(int i =0; i<size; i++){
			int[] nextMin = getMinimumWithRespectTo(intersections, x,y);
			result.add(nextMin);
			intersections.remove(nextMin);
		}
		return result;
	}

	/**
	 * Calculates the element that is closest to the given point 
	 * 
	 * @param intercepts
	 * 			must be of type: ArrayList<int[]>
	 * @param x
	 * @param y
	 * @return int[]
	 * 		The element of the list that is closest to (x,y).
	 */
	protected static int[] getMinimumWithRespectTo(ArrayList<int[]> intercepts,int x, int y) {
		int[] min = intercepts.get(0);
		for(int i =1; i<intercepts.size(); i++){
			if(compareWithRespectTo(min,intercepts.get(i), x,y) > 0){
				min = intercepts.get(i);
			}
		}
		return min;
	}

	/**
	 * Compares two positions by distance with respect to the given point (x,y).
	 * 
	 * @param c1
	 * @param c2
	 * @param x
	 * @param y 
	 * @return -1
	 * 		If c1 is closer to (x,y) then c2.
	 * @return 1
	 * 		If c2 is closer to (x,y) then c1.
	 * @return 0
	 * 		If c1 and c2 are equally close to (x,y).
	 */
	protected static int compareWithRespectTo(int[] c1, int[] c2, int x, int y){
		double d1 = getDistanceFromTo(x,y,c1[0],c1[1]);
		double d2 = getDistanceFromTo(x,y,c2[0],c2[1]);
		int result;
		if(d1 == d2){
			result = 0;
		} else if( d1 < d2 ){
			result = -1;
		} else {
			result = 1;
		}
		return result;		
	}

	/**
	 * Calculates the euclidean distance between two coordinates.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return double
	 * 		The euclidean distance between (x1,y1) and (x2,y2).
	 */
	protected static double getDistanceFromTo(int x1, int y1, int x2, int y2){
		int xDiff = x1-x2;
		int yDiff = y1-y2;
		int squaredSumDistance = xDiff*xDiff + yDiff*yDiff;
		double distance = Math.sqrt(squaredSumDistance);
		return distance;
	}
}
