package bluebot.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * An undirected edge in a graph
 * 
 * @author Incalza Dario
 */
public class Edge{
  private Tile t1,t2;

  /**
   * Create a directed edge between from and to
   * 
   * @param from
   *          the starting vertex
   * @param to
   *          the ending vertex
   */
  public Edge(Tile t1, Tile t2) {
    this.t1 = t1;
    this.t2 = t2;
  }

  public int getWeight(){
	  return 1;
  }

  public List<Tile> getTiles(){
	  List<Tile> tiles = new ArrayList<Tile>();
	  tiles.add(t1);
	  tiles.add(t2);
	  
	  return tiles;
  }
  
  public Tile getT1(){
	  return this.t1;
  }
  
  public Tile getT2(){
	  return this.t2;
  }

  @Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Edge) {
			final Edge e = (Edge) obj;
			return (e.getTiles().contains(this.getTiles().get(1))&&e.getTiles().contains(this.getTiles().get(0)));
		}
		return false;
	}
	
}
