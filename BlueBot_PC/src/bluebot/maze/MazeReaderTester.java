/**
 * 
 */
package bluebot.maze;

import static org.junit.Assert.*;

import org.junit.Test;

import bluebot.graph.Graph;

/**
 * @author Incalza Dario
 *
 */
public class MazeReaderTester {

	@Test
	public void testReader() {
		MazeReader reader = new MazeReader();
		Graph g = reader.parseMaze("voorbeeldMaze_Steven.txt");
		System.out.println(g);
	}

}
