package bluebot.graph;	
import java.util.ArrayList;
import java.util.List;

	
public class Graph {
	  /** Vector<Vertex> of graph verticies */
	  private List<Vertex> verticies;

	  /** Vector<Edge> of edges in the graph */
	  private List<Edge> edges;

	  /** The vertex identified as the root of the graph */
	  private Vertex rootVertex;

	  /**
	   * Construct a new graph without any vertices or edges
	   */
	  public Graph() {
	    verticies = new ArrayList<Vertex>();
	    edges = new ArrayList<Edge>();
	  }

	  /**
	   * Are there any verticies in the graph
	   * 
	   * @return true if there are no verticies in the graph
	   */
	  public boolean isEmpty() {
	    return verticies.size() == 0;
	  }

	  /**
	   * Add a vertex to the graph
	   * 
	   * @param v
	   *          the Vertex to add
	   * @return true if the vertex was added, false if it was already in the graph.
	   */
	  public boolean addVertex(Vertex v) {
	    boolean added = false;
	    if (!this.hasVertex(v.getData())){
	      added = verticies.add(v);
	    }
	    return added;
	  }

	  /**
	   * Get the vertex count.
	   * 
	   * @return the number of verticies in the graph.
	   */
	  public int size() {
	    return verticies.size();
	  }

	  /**
	   * Get the root vertex
	   * 
	   * @return the root vertex if one is set, null if no vertex has been set as
	   *         the root.
	   */
	  public Vertex getRootVertex() {
	    return rootVertex;
	  }

	  /**
	   * Set a root vertex. If root does no exist in the graph it is added.
	   * 
	   * @param root -
	   *          the vertex to set as the root and optionally add if it does not
	   *          exist in the graph.
	   */
	  public void setRootVertex(Vertex v) {
	    this.rootVertex = v;
	    if (this.hasVertex(v.getData()) == false)
	      this.addVertex(v);
	  }

	  /**
	   * Get the given Vertex.
	   * 
	   * @param n
	   *          the index [0, size()-1] of the Vertex to access
	   * @return the nth Vertex
	   */
	  public Vertex getVertex(int n) {
	    return verticies.get(n);
	  }

	  /**
	   * Get the graph verticies
	   * 
	   * @return the graph verticies
	   */
	  public List<Vertex> getVerticies() {
	    return this.verticies;
	  }

	  

	/**
	   * Insert a directed, weighted Edge into the graph.
	   * 
	   * @param from -
	   *          the Edge starting vertex
	   * @param to -
	   
	   * @return true if the Edge was added, false if from already has this Edge
	   * @throws IllegalArgumentException
	   *           if from/to are not verticies in the graph
	   */
	  public boolean addEdge(Vertex from, Vertex to) throws IllegalArgumentException {
		 
	    if (this.hasVertex(from.getData()) == false)
	      throw new IllegalArgumentException("from is not in graph");
	    if (this.hasVertex(to.getData()) == false)
	      throw new IllegalArgumentException("to is not in graph");

	    Edge e = new Edge(from, to);
	    if (from.findEdge(to) != null)
	      return false;
	    else {
	      from.addEdge(e);
	      to.addEdge(e);
	      edges.add(e);
	      return true;
	    }
	  }

	  /**
	   * Insert a bidirectional Edge in the graph
	   * 
	   * @param from -
	   *          the Edge starting vertex
	   * @param to -
	   *          the Edge ending vertex
	   * @param cost -
	   *          the Edge weight/cost
	   * @return true if edges between both nodes were added, false otherwise
	   * @throws IllegalArgumentException
	   *           if from/to are not verticies in the graph
	   */
	  public boolean insertBiEdge(Vertex from, Vertex to)
	      throws IllegalArgumentException {
	    return addEdge(from, to) && addEdge(to, from);
	  }

	  /**
	   * Get the graph edges
	   * 
	   * @return the graph edges
	   */
	  public List<Edge> getEdges() {
	    return this.edges;
	  }

	  /**
	   * Remove a vertex from the graph
	   * 
	   * @param v
	   *          the Vertex to remove
	   * @return true if the Vertex was removed
	   */
	  public boolean removeVertex(Vertex v) {
	    if (!this.hasVertex(v.getData()))
	      return false;

	    verticies.remove(v);
	    if (v == rootVertex)
	      rootVertex = null;

	    // Remove the edges associated with v
	    for (int n = 0; n < v.getOutgoingEdgeCount(); n++) {
	      Edge e = v.getOutgoingEdge(n);
	      v.remove(e);
	      Vertex to = e.getTo();
	      to.remove(e);
	      edges.remove(e);
	    }
	    for (int n = 0; n < v.getIncomingEdgeCount(); n++) {
	      Edge e = v.getIncomingEdge(n);
	      v.remove(e);
	      Vertex predecessor = e.getFrom();
	      predecessor.remove(e);
	    }
	    return true;
	  }

	  /**
	   * Remove an Edge from the graph
	   * 
	   * @param from -
	   *          the Edge starting vertex
	   * @param to -
	   *          the Edge ending vertex
	   * @return true if the Edge exists, false otherwise
	   */
	  public boolean removeEdge(Vertex from, Vertex to) {
	    Edge e = from.findEdge(to);
	    if (e == null)
	      return false;
	    else {
	      from.remove(e);
	      to.remove(e);
	      edges.remove(e);
	      return true;
	    }
	  }

	 
	  public String toString() {
	    StringBuffer tmp = new StringBuffer("Graph[");
	    for (Vertex v : verticies)
	      tmp.append(v.getData());
	    tmp.append(']');
	    return tmp.toString();
	  }
	  
	  
	  public boolean hasVertex(Tile data){
		  for(Vertex v : this.verticies){
			  if(v.getData().equals(data)){
				  return true;
			  }
		  }
		  
		  return false;
	  }

	}
	

