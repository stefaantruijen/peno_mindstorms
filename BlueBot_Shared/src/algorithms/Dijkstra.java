package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bluebot.graph.Edge;
import bluebot.graph.Graph;
import bluebot.graph.Tile;
/**
 * Dijkstra's Shortest Path Algorithm in Java
 * 
 * Lars Vogel
 *
 * Version 0.3
 *
 * Copyright © 2009 
 *
 *	02.11.2009
 * 
 * @author Lars Vogel,Incalza Dario
 *
 */
public class Dijkstra{
	
	  private final List<Edge> edges;
	  private List<Tile> settledNodes;
	  private List<Tile> unSettledNodes;
	  private Map<Tile, Tile> predecessors;
	  private Map<Tile, Integer> distance;
	  private Graph graph;

	  public Dijkstra(Graph graph) {
	    this.edges = new ArrayList<Edge>(graph.getEdges());
	    this.graph = graph;
	  }
	  /**
	   * Execute the Dijkstra Algorithm for a given source tile.
	   * 
	   * @param source
	   */
	  private void execute(Tile source) {
	    settledNodes = new ArrayList<Tile>();
	    unSettledNodes = new ArrayList<Tile>();
	    distance = new HashMap<Tile, Integer>();
	    predecessors = new HashMap<Tile, Tile>();
	    distance.put(source, 0);
	    unSettledNodes.add(source);
	    while (unSettledNodes.size() > 0) {
	      Tile node = getMinimum(unSettledNodes);
	      settledNodes.add(node);
	      unSettledNodes.remove(node);
	      findMinimalDistances(node);
	    }
	  }
	  /**
	   * Find the minimal distance for a given node.
	   * 
	   * @param node
	   */
	  private void findMinimalDistances(Tile node) {
	    List<Tile> adjacentNodes = getNeighbors(node);
	    for (Tile target : adjacentNodes) {
	      if (getShortestDistance(target) > getShortestDistance(node)
	          + getDistance(node, target)) {
	        distance.put(target, getShortestDistance(node)
	            + getDistance(node, target));
	        predecessors.put(target, node);
	        unSettledNodes.add(target);
	      }
	    }

	  }
	  /**
	   * Get the distance between 2 given tiles.
	   * 
	   * @param t1
	   * @param t2
	   * @return
	   */
	  private int getDistance(Tile t1, Tile t2) {
		  /**
	    for (Edge edge : edges) {
	      if (edge.getTiles().contains(t1)&&edge.getTiles().contains(t2)) {
	        return edge.getWeight();
	      }
	    }
	    throw new RuntimeException("Should not happen");**/
		  return 1;
	  }
	  /**
	   * Get all neighbors for a given tile.
	   * 
	   * @param node
	   * @return
	   */
	  private List<Tile> getNeighbors(Tile node) {
	    List<Tile> neighbors = new ArrayList<Tile>();
	    for(Tile t : node.getNeighbors()){
	    	Tile n = graph.getVertex(t.getX(), t.getY());
	    	if(n!=null && !isSettled(n)){
	    		neighbors.add(n);
	    	}
	    }
	    return neighbors;
	  }
	  
	  private Tile getMinimum(List<Tile> vertexes) {
	    Tile minimum = null;
	    for (Tile vertex : vertexes) {
	      if (minimum == null) {
	        minimum = vertex;
	      } else {
	        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
	          minimum = vertex;
	        }
	      }
	    }
	    return minimum;
	  }

	  private boolean isSettled(Tile vertex) {
	    return settledNodes.contains(vertex);
	  }

	  private int getShortestDistance(Tile destination) {
	    Integer d = distance.get(destination);
	    if (d == null) {
	      return Integer.MAX_VALUE;
	    } else {
	      return d;
	    }
	  }

	  /*
	   * This method returns the path from the source to the selected target and
	   * NULL if no path exists
	   */
	  private List<Tile> getPath(Tile target) {
	    LinkedList<Tile> path = new LinkedList<Tile>();
	    Tile step = target;
	    // Check if a path exists
	    if (predecessors.get(step) == null) {
	      return null;
	    }
	    path.add(step);
	    while (predecessors.get(step) != null) {
	      step = predecessors.get(step);
	      path.add(step);
	    }
	    // Put it into the correct order
	    return this.reverse(path);
	   
	  }
	  
	  private List<Tile> reverse(List<Tile> path){
		  List<Tile> tmp = new LinkedList<Tile>();
		  for(int i = path.size()-1;i>=0;i--){
			  tmp.add(path.get(i));
		  }
		  tmp.remove(0);
		  return tmp;
	  }
	
	public List<Tile> findShortestPath(Tile from, Tile to) {
		this.execute(from);
		return this.getPath(to);
	}

}
