package bluebot.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * A graph vertex with optional data.
 * 
 * @author Incalza Dario
 */

public class Vertex {
  private List<Edge> incomingEdges = new ArrayList<Edge>();

  private List<Edge> outgoingEdges = new ArrayList<Edge>();

  private Tile data;

  /**
   * Calls this(null, null).
   */
  public Vertex(Tile data) {
    this.data =data;
  }

  /**
   * @return the possibly null data of the vertex
   */
  public Tile getData() {
    return this.data;
  }

  /**
   * @param data
   *          The data to set.
   */
  public void setData(Tile data) {
    this.data = data;
  }

  /**
   * Add an edge to the vertex. If edge.from is this vertex, its an outgoing
   * edge. If edge.to is this vertex, its an incoming edge. If neither from or
   * to is this vertex, the edge is not added.
   * 
   * @param e -
   *          the edge to add
   * @return true if the edge was added, false otherwise
   */
  public boolean addEdge(Edge e) {
    if (e.getFrom() == this)
      outgoingEdges.add(e);
    else if (e.getTo() == this)
      incomingEdges.add(e);
    else
      return false;
    return true;
  }

  /**
   * Add an outgoing edge ending at to.
   * 
   * @param to -
   *          the destination vertex
   * @param cost
   *          the edge cost
   */
  public void addOutgoingEdge(Vertex to, int cost) {
    Edge out = new Edge(this, to, cost);
    outgoingEdges.add(out);
  }

  /**
   * Add an incoming edge starting at from
   * 
   * @param from -
   *          the starting vertex
   * @param cost
   *          the edge cost
   */
  public void addIncomingEdge(Vertex from, int cost) {
    Edge out = new Edge(this, from, cost);
    incomingEdges.add(out);
  }

  /**
   * Check the vertex for either an incoming or outgoing edge mathcing e.
   * 
   * @param e
   *          the edge to check
   * @return true it has an edge
   */
  public boolean hasEdge(Edge e) {
    if (e.getFrom() == this)
      return incomingEdges.contains(e);
    else if (e.getTo() == this)
      return outgoingEdges.contains(e);
    else
      return false;
  }

  /**
   * Remove an edge from this vertex
   * 
   * @param e -
   *          the edge to remove
   * @return true if the edge was removed, false if the edge was not connected
   *         to this vertex
   */
  public boolean remove(Edge e) {
    if (e.getFrom() == this)
      incomingEdges.remove(e);
    else if (e.getTo() == this)
      outgoingEdges.remove(e);
    else
      return false;
    return true;
  }

  /**
   * 
   * @return the count of incoming edges
   */
  public int getIncomingEdgeCount() {
    return incomingEdges.size();
  }

  /**
   * Get the ith incoming edge
   * 
   * @param i
   *          the index into incoming edges
   * @return ith incoming edge
   */
  public Edge getIncomingEdge(int i) {
    return incomingEdges.get(i);
  }

  /**
   * Get the incoming edges
   * 
   * @return incoming edge list
   */
  public List<Edge> getIncomingEdges() {
    return this.incomingEdges;
  }

  /**
   * 
   * @return the count of incoming edges
   */
  public int getOutgoingEdgeCount() {
    return outgoingEdges.size();
  }

  /**
   * Get the ith outgoing edge
   * 
   * @param i
   *          the index into outgoing edges
   * @return ith outgoing edge
   */
  public Edge getOutgoingEdge(int i) {
    return outgoingEdges.get(i);
  }

  /**
   * Get the outgoing edges
   * 
   * @return outgoing edge list
   */
  public List<Edge> getOutgoingEdges() {
    return this.outgoingEdges;
  }

  /**
   * Search the outgoing edges looking for an edge whose's edge.to == dest.
   * 
   * @param dest
   *          the destination
   * @return the outgoing edge going to dest if one exists, null otherwise.
   */
  public Edge findEdge(Vertex dest) {
    for (Edge e : outgoingEdges) {
      if (e.getTo().getData() == dest.getData())
        return e;
    }
    return null;
  }

  /**
   * Search the outgoing edges for a match to e.
   * 
   * @param e -
   *          the edge to check
   * @return e if its a member of the outgoing edges, null otherwise.
   */
  public Edge findEdge(Edge e) {
    if (outgoingEdges.contains(e))
      return e;
    else
      return null;
  }


  /**
   * Is there an outgoing edge ending at dest.
   * 
   * @param dest -
   *          the vertex to check
   * @return true if there is an outgoing edge ending at vertex, false
   *         otherwise.
   */
  public boolean hasEdge(Vertex dest) {
    return (findEdge(dest) != null);
  }
  
  @Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Vertex) {
			final Vertex v = (Vertex) obj;
			return (v.getData().equals(this.getData()));
		}
		return false;
	}
  @Override
  public int hashCode(){
	  return this.getData().hashCode();
  }

}