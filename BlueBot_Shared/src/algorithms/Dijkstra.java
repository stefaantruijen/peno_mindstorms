package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
	  private ArrayList<Predecessor> predecessors;
	  private ArrayList<Distance> distance;
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
	  public void execute(Tile source) {
	    settledNodes = new ArrayList<Tile>();
	    unSettledNodes = new ArrayList<Tile>();
	    distance = new ArrayList<Distance>();
	    predecessors = new ArrayList<Predecessor>();
	    distance.add(new Distance(source, 0));
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
	        distance.add(new Distance(target, getShortestDistance(node)
	            + getDistance(node, target)));
	        predecessors.add(new Predecessor(target, node));
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
	    Integer d = this.getDistanceForTile(destination);
	    if (d == null) {
	      return Integer.MAX_VALUE;
	    } else {
	      return d;
	    }
	  }
	  
	  private Integer getDistanceForTile(Tile destination){
		  for(Distance d : distance){
			  if(d.getTile().equals(destination)){
				  return d.getInteg();
			  }
		  }
		  
		  return null;
	  }

	  /*
	   * This method returns the path from the source to the selected target and
	   * NULL if no path exists
	   */
	  public List<Tile> getPath(Tile target) {
	    LinkedList<Tile> path = new LinkedList<Tile>();
	    Tile step = target;
	    // Check if a path exists
	    if (this.getPredecessorForKey(step) == null) {
	      return null;
	    }
	    path.add(step);
	    while (this.getPredecessorForKey(step) != null) {
	      step = this.getPredecessorForKey(step);
	      path.add(step);
	    }
	    // Put it into the correct order
	    return this.reverse(path);
	   
	  }
	  
	  private Tile getPredecessorForKey(Tile key){
		  for(Predecessor p : this.predecessors){
			  if(p.getKey().equals(key)){
				  return p.getValue();
			  }
		  }
		  
		  return null;
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
