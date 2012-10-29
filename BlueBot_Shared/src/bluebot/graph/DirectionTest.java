package bluebot.graph;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * Test for enum Direction
 * @author  Incalza Dario
 *
 */
public class DirectionTest {

	@Test
	public void testCWise() {
		assertTrue(Direction.UP.turnCWise()==Direction.RIGHT);
	}
	
	@Test
	public void testCCWise(){
		assertTrue(Direction.UP.turnCCWise()==Direction.LEFT);
	}

}
