package bluebot.graph;

/**
 * A directed edge in a graph
 * 
 * @author Incalza Dario
 */
class Edge{
  private Vertex from;

  private Vertex to;

  /**
   * Create a directed edge between from and to
   * 
   * @param from
   *          the starting vertex
   * @param to
   *          the ending vertex
   */
  public Edge(Vertex from, Vertex to) {
    this(from, to, 0);
  }

  /**
   * Create an edge between from and to with the given cost.
   * 
   * @param from
   *          the starting vertex
   * @param to
   *          the ending vertex
   */
  public Edge(Vertex from, Vertex to, int cost) {
    this.from = from;
    this.to = to;
  }

  /**
   * Get the ending vertex
   * 
   * @return ending vertex
   */
  public Vertex getTo() {
    return to;
  }

  /**
   * Get the starting vertex
   * 
   * @return starting vertex
   */
  public Vertex getFrom() {
    return from;
  }


}
