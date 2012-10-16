/**
 * 
 */
package simulator.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import simulator.*;

/**
 * @author Dieter
 *
 */
public class TestRobot {
	
	@Test
	public void test() {
		assert(true);
	}
	
	@Test
	public void testRobot(){
		Robot r = new Robot();
		assertEquals(Robot.STANDARD_TRAVEL_SPEED, r.getTravelSpeed(),0);
		assertEquals(Robot.STANDARD_ROTATE_SPEED, r.getTravelSpeed(),0);
		assertEquals(false,r.isMoving());
	}
	
	@Test
	public void testRobot2(){
		Random rand = new Random();
		Double travelSpeedRandom = rand.nextInt(100000) * rand.nextDouble();
		Double rotateSpeedRandom = rand.nextInt(100000) * rand.nextDouble();
		Robot r2 = new Robot(travelSpeedRandom, rotateSpeedRandom);
		assertEquals(travelSpeedRandom, r2.getTravelSpeed(),0);
		assertEquals(rotateSpeedRandom, r2.getRotateSpeed(),0);

	}
	
	@Test
	public void testRotate(){
		Robot r3 = new Robot();
		Thread testThread = new Thread(r3);
		testThread.start();
		double angle = 1000*Robot.STANDARD_ROTATE_SPEED;
		r3.rotate(angle);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(Action.ROTATE, r3.getCurrentAction());
		assertEquals(angle, r3.getCurrentArgument(), 0);
		r3.kill();
	}
	
	@Test
	public void testTravel(){
		Robot r4 = new Robot();
		Thread testThread2 = new Thread(r4);
		testThread2.start();
		double distance = 1000*Robot.STANDARD_TRAVEL_SPEED;
		r4.travel(distance);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(Action.TRAVEL,r4.getCurrentAction());
		assertEquals(distance, r4.getCurrentArgument(), 0);
		r4.kill();
	}
	
	@Test
	public void testQueueing(){
		Robot r5 = new Robot();
		Thread testThread3 = new Thread(r5);

		r5.travel(1000);
		r5.rotate(60);
		r5.travel(1000);
		r5.rotate(60);
		r5.travel(1000);
		r5.rotate(60);
		
		testThread3.start();
	}
	


}
