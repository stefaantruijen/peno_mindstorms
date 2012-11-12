package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.graph.Edge;
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
public class Dijkstra {
	
	  private final List<Edge> edges;
	  private Set<Tile> settledNodes;
	  private Set<Tile> unSettledNodes;
	  private Map<Tile, Tile> predecessors;
	  private Map<Tile, Integer> distance;
	  private Graph graph;

	  public Dijkstra(Graph graph) {
	    this.edges = new ArrayList<Edge>(graph.getEdges());
	    this.graph = graph;
	  }

	  public void execute(Tile source) {
		  System.out.println("Calculating paths");
	    settledNodes = new HashSet<Tile>();
	    unSettledNodes = new HashSet<Tile>();
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
	    System.out.println("Paths found");
	  }

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

	  private int getDistance(Tile t1, Tile t2) {
		  System.out.println(edges.size());
	    for (Edge edge : edges) {
	      if (edge.getTiles().contains(t1)&&edge.getTiles().contains(t2)) {
	        return edge.getWeight();
	      }
	    }
	    throw new RuntimeException("Should not happen");
	  }

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

	  private Tile getMinimum(Set<Tile> vertexes) {
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
	  public LinkedList<Tile> getPath(Tile target) {
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
	    Collections.reverse(path);
	    return path;
	  }

}
