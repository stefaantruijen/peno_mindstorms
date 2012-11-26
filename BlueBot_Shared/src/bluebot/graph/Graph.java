package bluebot.graph;	
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Representation of a undirected graph.
 * 
 * @author Incalza Dario
 *
 */
public class Graph {
	  
	  private List<Tile> verticies;
	  private List<Edge> edges;

	  /** The tile identified as the root of the graph */
	  private Tile rootVertex;
	  
	  private Tile finishVertex,checkpointVertex;
	  

	  /**
	   * Construct a new graph without any vertices or edges
	   */
	  public Graph() {
	    verticies = new ArrayList<Tile>();
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
	   * Add a tile to the graph
	   * 
	   * @param t
	   *          the Tile to add
	   * @return true if the tile was added, false if it was already in the graph.
	   */
	  public boolean addVertex(Tile t) {
	    boolean added = false;
	    if (!this.verticies.contains(t)){
	      added = verticies.add(t);
	      //check for already added tiles if the given tile is a neighbor from all the other tiles already present in the graph.
	      for(Tile tile : this.verticies){
	    	  if(t.isExplored() && t.isNeighborFrom(tile)){
	    		  this.addEdge(t, tile);
	    		  
	    	  }
	      }
	    }
	    return added;
	  }

	  /**
	   * Get the tile count.
	   * 
	   * @return the number of verticies in the graph.
	   */
	  public int size() {
	    return verticies.size();
	  }

	  /**
	   * Get the root tile
	   * 
	   * @return the root tile if one is set, null if no tile has been set as
	   *         the root.
	   */
	  public Tile getRootTile() {
	    return rootVertex;
	  }

	  /**
	   * Set a root tile. If root does no exist in the graph it is added.
	   * 
	   * @param root -
	   *          the tile to set as the root and optionally add if it does not
	   *          exist in the graph.
	   */
	  public void setRootTile(Tile t) {
	    this.rootVertex = t;
	    if (!this.verticies.contains(t))
	      this.addVertex(t);
	  }

	  /**
	   * Get the given tile.
	   * 
	   * @param n
	   *          the index [0, size()-1] of the tile to access
	   * @return the nth tile
	   */
	  public Tile getVertex(int n) {
	    return verticies.get(n);
	  }

	  /**
	   * Get the graph verticies
	   * 
	   * @return the graph verticies
	   */
	  public List<Tile> getVerticies() {
	    return new ArrayList<Tile>(this.verticies);
	  }

	  

	/**
	   * Insert a directed, weighted Edge into the graph.
	   * 
	   * @param from -
	   *          the Edge starting tile
	   * @param to -
	   
	   * @return true if the Edge was added, false if from already has this Edge
	   * @throws IllegalArgumentException
	   *           if from/to are not verticies in the graph
	   */
	  public boolean addEdge(Tile t1, Tile t2) throws IllegalArgumentException {
		 
	    if (!this.verticies.contains(t1))
	      throw new IllegalArgumentException(t1+ " is not in graph");
	    if (!this.verticies.contains(t2))
	      throw new IllegalArgumentException(t2+ " is not in graph");
	    
	    Edge e = new Edge(t1,t2);
	    if(!this.edges.contains(e))
	    	return edges.add(e);
	    return false;
	    
	  }


	  /**
	   * Get the graph edges
	   * 
	   * @return the graph edges
	   */
	  public List<Edge> getEdges() {
		  this.validateAllEdges();
	    return this.edges;
	  }

	  /**
	   * Remove a tile from the graph
	   * 
	   * @param v
	   *          the tile to remove
	   * @return true if the tile was removed
	   */
	  public boolean removeTile(Tile t) {
	    if (!this.verticies.contains(t))
	      return false;

	    verticies.remove(t);
	    if (t == rootVertex)
	      rootVertex = null;

	    // Remove the edges associated with v
	    Iterator<Edge> iter = this.edges.iterator();
	    while(iter.hasNext()){
	    	Edge e = iter.next();
	    	if(e.getTiles().contains(t)){
	    		edges.remove(e);
	    	}
	    }
	    return true;
	  }


	 
	  public String toString() {
	    StringBuffer tmp = new StringBuffer("Graph[");
	    for (Tile t : this.verticies)
	      tmp.append(t);
	    tmp.append(']');
	    return tmp.toString();
	  }
	  
	  public boolean hasVertex(Tile t){
		  return this.verticies.contains(t);
	  }
	  
	  public Tile getVertex(int x,int y){
		  for(Tile t : this.verticies){
			  if(t.getX() == x && t.getY()==y){
				  return t;
			  }
		  }
		  
		  return null;
	  }
	  
	  public void addVerticies(List<Tile> verticies){
		  for(Tile t : verticies){
			  this.addVertex(t);
		  }
	  }
	  
	  private void validateAllEdges(){
		  for(Tile t:this.getVerticies()){
			  
				  for(Tile n : this.getNeighborsFrom(t)){
					  if(t.isNeighborFrom(n)){
						  this.addEdge(t, n);
					  }
					  
				  }
			  
		  }
	  }
	  
	  public List<Tile> getUnExploredTiles(){
		  List<Tile> tiles = new ArrayList<Tile>();
		  for(Tile t : this.getVerticies()){
			  if(!t.isExplored()){
				  tiles.add(t);
			  }
		  }
		  
		  return tiles;
	  }
	  
	  public List<Tile> getNeighborsFrom(Tile t){
		  int x = t.getX();
		  int y = t.getY();
		  
		  List<Tile> neighbors = new ArrayList<Tile>();
			if(t.getBorderEast()==Border.OPEN){
				neighbors.add(this.getVertex(x+1,y));
			}
			if(t.getBorderNorth()==Border.OPEN){
				neighbors.add(this.getVertex(x,y+1));
			}
			
			if(t.getBorderSouth()==Border.OPEN){
				neighbors.add(this.getVertex(x,y-1));
			}
			
			if(t.getBorderWest()==Border.OPEN){
				neighbors.add(this.getVertex(x-1,y));
			}
			
			return neighbors;
	  }

	public Tile getFinishVertex() {
		return finishVertex;
	}

	public void setFinishVertex(Tile finishVertex) {
		this.finishVertex = finishVertex;
	}
	
	public Tile getCheckpointVertex(){
		return this.checkpointVertex;
	}
	
	public void setCheckpointVertex(Tile t){
		this.checkpointVertex = t;
	}
	  

}
	

