package bluebot.graph;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphTest {

	@Test
	public void testGraph() {
		Graph g = new Graph();
		Tile t1 = new Tile(0,0);
		Tile t2 = new Tile(0,1);
		Tile t3 = new Tile(1,0);
		Tile t4 = new Tile(1,1);
 		g.addVertex(t1);
		g.addVertex(t2);
		g.addVertex(t3);
		g.addVertex(t4);
		g.addEdge(t1, t2);
		assertTrue(g.getEdges().size()==4);
	}

}
