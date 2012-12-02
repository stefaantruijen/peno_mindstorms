package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Tile;

/**
 * A* algorithm implementation.
 * 
 * @author Incalza Dario
 * 
 */
public class AStar implements PathFinder
{
	private final ArrayList<Tile> closedList;
    private final SortedNodeList openList;
    private final Graph map;
    private final HeuristicFunction heuristic;
    
    private final static double TURN_COST = 1.0;
    private final static double MOVE_COST = 5.0;
   
    public AStar(Graph map, HeuristicFunction heuristic) {
            this.map = map;
            this.heuristic = heuristic;

            closedList = new ArrayList<Tile>();
            openList = new SortedNodeList();
    }

    private Path calcShortestPath(Tile start, Tile goal) {
           
            start.setDistanceFromStart(0);
            closedList.clear();
            openList.clear();
            openList.add(start);
            Tile previous;
            Tile current = openList.getFirst();
            while(openList.size() != 0) {
                   	previous = current;
                    current = openList.getFirst();
                    current.setOrientationToReach(determineBestOrientation(previous,current));
                    if(current.equals(goal)) {
                            return reconstructPath(current);
                    }

                    openList.remove(current);
                    closedList.add(current);

                    for(Tile neighbor : this.map.getNeighborsFrom(current)) {
                            boolean neighborIsBetter;

                             
                            if (closedList.contains(neighbor))
                                    continue;
                            
                                    double neighborDistanceFromStart = (current.getDistanceFromStart()+this.getNeighborCost(current.getOrientationToReach(),current,neighbor));

                                    if(!openList.contains(neighbor)) {
                                            openList.add(neighbor);
                                            neighborIsBetter = true;
                                    } else if(neighborDistanceFromStart < current.getDistanceFromStart()) {
                                            neighborIsBetter = true;
                                    } else {
                                            neighborIsBetter = false;
                                    }
                                    
                                    if (neighborIsBetter) {
                                            neighbor.setPreviousNode(current);
                                            neighbor.setDistanceFromStart(neighborDistanceFromStart);
                                            neighbor.setHeuristicDistanceFromGoal(heuristic.determineHeuristicValue(neighbor, goal));
                                    }
                    }

            }
            return null;
    }

    private Path reconstructPath(Tile node) {
            Path path = new Path();
            while(!(node.getPreviousNode() == null)) {
                    path.prependWayPoint(node);
                    node = node.getPreviousNode();
            }
            return path;
    }

    private class SortedNodeList {

            private List<Tile> list = new ArrayList<Tile>();

            public Tile getFirst() {
                    return list.get(0);
            }

            public void clear() {
                    list.clear();
            }

            public void add(Tile node) {
                    list.add(node);
                    Collections.sort(list);
            }

            public void remove(Tile n) {
                    list.remove(n);
            }

            public int size() {
                    return list.size();
            }

            public boolean contains(Tile n) {
                    return list.contains(n);
            }
    }
    
    /**
    *
    * @param current
    *             De huidige node.
    * @param neighbor
    *             De volgende node.
    * @return
    *             |result == this.robot.getMOVE_COST()+determineEfficientTurnCost(current.getOrientationToReach(),bestOrient)
    */
   private double getNeighborCost(Direction currentDir,Tile current,Tile neighbor){
       
       Direction bestDir = determineBestOrientation(current,neighbor);
       neighbor.setOrientationToReach(bestDir);
       double additionalCost = determineEfficientTurnCost(current.getOrientationToReach(),bestDir);

        return MOVE_COST+additionalCost;
   }
   /**
    *
    * @param current
    *             De huidige node.
    * @param next
    *             De volgende node.
    * @return
    *             |if(current.getX() > next.getX())
    *             |    then result == Orientation.LEFT
    *             |else if (current.getX() < next.getX())
    *             |    then result == Orientation.RIGHT
    *             |else if (current.getY() < next.getY())
    *             |    then result == Orientation.DOWN
    *             |else
    *             |    result == Orientation.UP
    */
   private Direction determineBestOrientation(Tile current,Tile next){
       if(current.getX() > next.getX()){ //coordinaat is strikt rechts van next
           return Direction.LEFT;
       }else if (current.getX() < next.getX()){ //coordinaat is strikt links van next
           return Direction.RIGHT;
       }else if (current.getY() < next.getY()){ // coordinaat is strikt boven van next
           return Direction.DOWN;
       }else{
           return Direction.UP; //coordinaat is strikt onder next
       }
   }
   /**
    *
    * @param currOrient
    *             De huidige orientatie
    * @param endOrient
    *             De nodige orientatie
    * @return
    *             | if(currOrient != endOrient)
    *             |    then if(currOrient.turnClockwise() == endOrient)
    *             |            then result == robot.getTURN_COST()
    *             |         else if(currOrient.turnCounterClockwise() == endOrient)
    *             |            then result == robot.getTURN_COST()
    *             |         else
    *             |            result == 2*robot.getTURN_COST()
    *             | else
    *             |    result == 0
    *             |
    */
   private double determineEfficientTurnCost(Direction currOrient, Direction endOrient){
       if(currOrient != endOrient){
           if(currOrient.turnCWise() == endOrient){
                   return TURN_COST; //clockwise draaien
               }
           else if(currOrient.turnCCWise() == endOrient){
                   return TURN_COST; //counterclockwise draaien
               }
           else{
               return 2*TURN_COST;
           }
       }else{
           return 0;
       }
   }


	@Override
	public List<Tile> findShortestPath(Tile from, Tile to) {
		
		return this.calcShortestPath(from, to).getWayPoints();
	}

}
