/**
 * 
 */
package simulator.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import simulator.*;

/**
 * @author Dieter
 *
 */
public class TestRobot {
	
	@Test
	public void test() {
		testRobot();
	}
	
	public void testRobot(){
		Robot r = new Robot();
		assertEquals(Robot.STANDARD_TRAVEL_SPEED, r.getTravelSpeed(),0);
		assertEquals(Robot.STANDARD_ROTATE_SPEED, r.getTravelSpeed(),0);

	}
	
	public void testRobot2(){
		
	}

}
