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
		assertTrue(Direction.LEFT.turnCWise()==Direction.UP);
		assertTrue(Direction.DOWN.turnCWise()==Direction.LEFT);
		assertTrue(Direction.RIGHT.turnCWise()==Direction.DOWN);
	}
	
	@Test
	public void testCCWise(){
		assertTrue(Direction.UP.turnCCWise()==Direction.LEFT);
		assertTrue(Direction.DOWN.turnCCWise()==Direction.RIGHT);
		assertTrue(Direction.LEFT.turnCCWise()==Direction.DOWN);
		assertTrue(Direction.RIGHT.turnCCWise()==Direction.UP);
	}

}
