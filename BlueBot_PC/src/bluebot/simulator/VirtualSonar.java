package bluebot.simulator;

import java.util.ArrayList;
import java.util.Hashtable;

import bluebot.graph.Border;
import bluebot.graph.Tile;
import bluebot.util.Utils;
/**
 * 
 * @author Dieter
 *TODO refactor some code into smaller methods?
 */
public class VirtualSonar {
	private static Sensors mySensors;
 
	public VirtualSonar(Sensors sensors) {
		this.mySensors = sensors;
	}

	/**
	 * Returns the sonar value (which is the distance to the first wall) 
	 * 	at the given coordinates and heading. 
	 * 
	 * Coordinates are given with respect to (0,0) 
	 *  which is the 'pixel' in the left lower corner.
	 * @param imgX
	 * 			An x coordinate of the plane in which all the tiles are placed.
	 * @param imgY
	 * 			An y coordinate of the plane in which all the tiles are placed.
	 * @param heading
	 * 			An angle representing the orientation of the sonar in the plane. [0,360)
	 * @return int
	 * 			The distance to the first encountered wall.
	 * @return (int)Integer.MAX_VALUE
	 * 			If no wall is found.
	 */
	public int getSonarValue(int imgX, int imgY, float heading) {
		ArrayList<int[]> intersections = this.calculateIntersections(imgX,imgY,heading);
		ArrayList<int[]> sortedIntersections = sortByDistanceTo(intersections, imgX, imgY);
		boolean wallFound = false;
		int[]currentIntercept = null;
		double distance = 0;
		while(!wallFound && !sortedIntersections.isEmpty()){
			currentIntercept = sortedIntersections.get(0);
			Tile currentT = mySensors.getTileAt(currentIntercept[0], currentIntercept[1]);
			Tile nb = null;
			if(heading == 0){ //Straight North
				nb = mySensors.getNorthNeigborOf(currentT);
				if(currentT.getBorderNorth() == Border.CLOSED 
					|| nb != null && nb.getBorderSouth()== Border.CLOSED
				   ){
					wallFound = true;
				}
			} else if(heading == 90){ //Straight East
				nb = mySensors.getEastNeigborOf(currentT);
				if(currentT.getBorderEast() == Border.CLOSED 
					|| nb != null && nb.getBorderWest()== Border.CLOSED
				   ){
					wallFound = true;
				}
			}else if(heading == 180){ //Straight South
				nb = mySensors.getSouthNeigborOf(currentT);
				if(currentT.getBorderSouth() == Border.CLOSED 
					|| nb != null && nb.getBorderNorth()== Border.CLOSED
				   ){
					wallFound = true;
				}
			}else if(heading == 270){ //Straight West
				nb = mySensors.getWestNeigborOf(currentT);
				if(currentT.getBorderWest() == Border.CLOSED 
					|| nb != null && nb.getBorderEast()== Border.CLOSED
				   ){
					wallFound = true;
				}
			} else { //More general case
				if(currentIntercept[1] == mySensors.getNorthBorderCoor(currentT)){ //A northern intersection
					nb = mySensors.getNorthNeigborOf(currentT);
					if(currentT.getBorderNorth() == Border.CLOSED 
						|| nb != null && nb.getBorderSouth()== Border.CLOSED
					   ){
						wallFound = true;
					}
				}else if(currentIntercept[0] == mySensors.getEastBorderCoor(currentT)){ //An eastern intersection
					nb = mySensors.getEastNeigborOf(currentT);
					if(currentT.getBorderEast() == Border.CLOSED 
						|| nb != null && nb.getBorderWest()== Border.CLOSED
					   ){
						wallFound = true;
					}
				} else if (currentIntercept[1] == mySensors.getSouthBorderCoor(currentT)){//A southern intersection
					nb = mySensors.getSouthNeigborOf(currentT);
					if(currentT.getBorderSouth() == Border.CLOSED 
						|| nb != null && nb.getBorderNorth()== Border.CLOSED
					   ){
						wallFound = true;
					}
				} else if(currentIntercept[0] == mySensors.getWestBorderCoor(currentT)){//A western intersection
					nb = mySensors.getWestNeigborOf(currentT);
					if(currentT.getBorderEast() == Border.CLOSED 
						|| nb != null && nb.getBorderEast()== Border.CLOSED
					   ){
						wallFound = true;
					}
				}
			}
			//Remove 'used' intercept.
			sortedIntersections.remove(currentIntercept);
		}
		if(!wallFound){//No wall is found at all.
			distance = (double)Integer.MAX_VALUE;
		} else {
			distance = getDistanceFromTo(imgY, imgY, currentIntercept[0], currentIntercept[1]);
		}
		return (int)Math.round(distance);
	}
	
	/**
	 * Finds a list with all the intersections of borders of tiles 
	 * 	with the line representing the heading. 
	 * 
	 * @param imgX
	 * 			An x coordinate of the plane in which all the tiles are placed.
	 * @param imgY
	 * 			An y coordinate of the plane in which all the tiles are placed.
	 * @param heading
	 * 			An angle representing the orientation of the sonar in the plane. [0,360)
	 * @return ArrayList<int[]>
	 * 			A list with pairs (int[0] =x and int[1] = y) of coordinates representing the intersections
	 * @return null
	 * 			If no intersections are found.
	 */
	protected ArrayList<int[]> calculateIntersections(int imgX, int imgY, float heading) {
		ArrayList<int[]> resultList = new ArrayList<int[]>();
		if(heading ==0){
			int startY = mySensors.getNorthBorderCoor(imgX, imgY);
			for(int Y = startY; Y<= mySensors.getMaxX(); Y += mySensors.getTileSize()){
				int[] co = new int[]{imgX,Y};
				resultList.add(co);
			}
		} else if(heading == 180){
			int startY = mySensors.getSouthBorderCoor(imgX, imgY);
			for(int Y = startY; Y>= 0; Y -= mySensors.getTileSize()){
				int[] co = new int[]{imgX,Y};
				resultList.add(co);
			}
		} else 	if(heading ==90){
			int startX = mySensors.getEastBorderCoor(imgX, imgY);
			for(int X = startX; X<= mySensors.getMaxX(); X += mySensors.getTileSize()){
				int[] co = new int[]{X,imgY};
				resultList.add(co);
			}
		} else if(heading == 270){
			int startX = mySensors.getWestBorderCoor(imgX, imgY);
			for(int X = startX; X>= 0; X -= mySensors.getTileSize()){
				int[] co = new int[]{X,imgY};
				resultList.add(co);
			}
		} else {
			int startX = 0;
			int startY = 0;
			int stopX = 0;
			int stopY = 0;
			int checkLowX = 0;
			int checkLowY = 0;
			int checkHighX = 0;
			int checkHighY = 0;
			if(heading >0 && heading< 90){
				startX = mySensors.getNorthBorderCoor(imgX, imgY);
				stopX = mySensors.getMaxX();
				checkLowX = imgX;
				checkHighX = mySensors.getMaxX();
				startY = mySensors.getEastBorderCoor(imgX, imgY);
				stopY = mySensors.getMaxY();
				checkLowY = imgY;
				checkHighY = mySensors.getMaxY();
			} else if(heading >90 && heading< 180){
				startX = mySensors.getEastBorderCoor(imgX, imgY);
				stopX = mySensors.getMaxX();
				checkLowX = imgX;
				checkHighX = mySensors.getMaxX();
				startY = 0;
				stopY = mySensors.getSouthBorderCoor(imgX, imgY);
				checkLowY = 0;
				checkHighY = imgY;
			}else if(heading >180 && heading< 270){
				startX = 0;
				stopX = mySensors.getWestBorderCoor(imgX, imgY);
				checkLowX = 0;
				checkHighX = imgX;
				startY = 0;
				stopY = mySensors.getSouthBorderCoor(imgX, imgY);
				checkLowY = 0;
				checkHighY = imgY;
			}else if(heading >270 && heading< 360){
				startX = 0;
				stopX = mySensors.getWestBorderCoor(imgX, imgY);
				checkLowX = 0;
				checkHighX = imgX;
				startY = mySensors.getNorthBorderCoor(imgX, imgY);
				stopY = mySensors.getMaxY();
				checkLowY = imgY;
				checkHighY = mySensors.getMaxY();
			}
			//double rico = Math.round(Math.tan(Utils.degrees2radians(-heading+90)));
			double rico =Math.tan(Math.toRadians(-heading+90));
			double yIntercept = imgY - rico*imgX;
			int foundY;
			for(int X = startX; X<= stopX; X = X+mySensors.getTileSize()){
				foundY = (int) Math.round(X*rico + yIntercept);
				if(foundY >=checkLowY && foundY <=checkHighY ){
					int[] co = new int[]{X,foundY};
					resultList.add(co);
				}
			}
			int foundX;
			for(int Y = startY; Y<= stopY; Y = Y+mySensors.getTileSize()){
					foundX = (int) Math.round((Y-yIntercept)/rico);
					if(foundX >=checkLowX && foundX <=checkHighX){
						int[] co = new int[]{foundX,Y};
						resultList.add(co);
					}
				}
			}
		return resultList;
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
	
	

}
