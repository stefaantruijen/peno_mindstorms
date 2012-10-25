package bluebot.graph;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphTest {

	@Test
	public void testGraph() {
		Graph g = new Graph();
		Vertex v1 = new Vertex(new Tile(0,0));
		Vertex v2 = new Vertex(new Tile(0,1));
 		g.addVertex(v1);
		g.addVertex(v2);
		g.insertBiEdge(v1, v2);
		assertTrue(g.getEdges().size()==2);
	}

}
