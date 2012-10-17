/**
 * 
 */
package simulator;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import simulator.*;

/**
 * @author Dieter
 *
 */
public class TestRobot {
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
		
		//Testing it too fast will return false since the testThread has to be scheduled
		long start = System.currentTimeMillis();
		while(true){
			if(Action.TRAVEL.equals(r3.getCurrentAction())){
				assert(true);
				assertEquals(angle, r3.getCurrentArgument(), 0);
				break;
			}else if (System.currentTimeMillis()-start > 150){
				assert(false);
				break;
			}
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
		
		//Testing it too fast will return false since the testThread has to be scheduled
		long start = System.currentTimeMillis();
		while(true){
			if(Action.TRAVEL.equals(r4.getCurrentAction())){
				assert(true);
				assertEquals(distance, r4.getCurrentArgument(), 0);
				break;
			}else if (System.currentTimeMillis()-start > 150){
				assert(false);
				break;
			}
		}
		
		r4.kill();
	}
	
	@Test
	public void testActionQueueing(){
		//Setup
		Robot r5 = new Robot();
		Random rand = new Random();
		
		double travelValue1 = rand.nextInt(100) * rand.nextDouble();
		double travelValue2 = rand.nextInt(100) * rand.nextDouble(); 
		double travelValue3 = rand.nextInt(100) * rand.nextDouble();
		double travelValue4 = rand.nextInt(100) * rand.nextDouble();
		double travelValue5 = rand.nextInt(100) * rand.nextDouble();

		double rotateValue1 = rand.nextInt(90) * rand.nextDouble();
		double rotateValue2 = rand.nextInt(90) * rand.nextDouble();
		double rotateValue3 = rand.nextInt(90) * rand.nextDouble();
		double rotateValue4 = rand.nextInt(90) * rand.nextDouble();
		double rotateValue5 = rand.nextInt(90) * rand.nextDouble();

		r5.travel(travelValue1);
		r5.travel(travelValue2);
		r5.rotate(rotateValue1);
		r5.travel(travelValue3);
		r5.rotate(rotateValue2);
		r5.rotate(rotateValue3);
		r5.rotate(rotateValue4);
		r5.travel(travelValue4);
		r5.rotate(rotateValue5);
		r5.travel(travelValue5);
		
		LinkedBlockingQueue<ActionPacket> currentQueue = r5.queue;
		
		//Testing
		ActionPacket tmp = currentQueue.poll();
		assertEquals(Action.TRAVEL, tmp.getAction());
		assertEquals(travelValue1, tmp.getArgument(),0);
		
		tmp = currentQueue.poll();
		assertEquals(Action.TRAVEL, tmp.getAction());
		assertEquals(travelValue2, tmp.getArgument(),0);

		tmp = currentQueue.poll();
		assertEquals(Action.ROTATE, tmp.getAction());
		assertEquals(rotateValue1, tmp.getArgument(),0);

		tmp = currentQueue.poll();
		assertEquals(Action.TRAVEL, tmp.getAction());
		assertEquals(travelValue3, tmp.getArgument(),0);
		
		tmp = currentQueue.poll();
		assertEquals(Action.ROTATE, tmp.getAction());
		assertEquals(rotateValue2, tmp.getArgument(),0);
		tmp = currentQueue.poll();
		
		assertEquals(Action.ROTATE, tmp.getAction());
		assertEquals(rotateValue3, tmp.getArgument(),0);
		tmp = currentQueue.poll();
		
		assertEquals(Action.ROTATE, tmp.getAction());
		assertEquals(rotateValue4, tmp.getArgument(),0);
		
		tmp = currentQueue.poll();
		assertEquals(Action.TRAVEL, tmp.getAction());
		assertEquals(travelValue4, tmp.getArgument(),0);

		tmp = currentQueue.poll();
		assertEquals(Action.ROTATE, tmp.getAction());
		assertEquals(rotateValue5, tmp.getArgument(),0);
		
		tmp = currentQueue.poll();
		assertEquals(Action.TRAVEL, tmp.getAction());
		assertEquals(travelValue5, tmp.getArgument(),0);

	}
	
	@Test
	public void testStop(){
		//Setup
		Robot r6 = new Robot();
		Thread testThread3 = new Thread(r6);
		testThread3.start();
		
		Random rand = new Random();
		double travelValue1 = rand.nextInt((int)Robot.STANDARD_TRAVEL_SPEED) * rand.nextDouble();
		double travelValue2 = rand.nextInt((int)Robot.STANDARD_TRAVEL_SPEED) * rand.nextDouble(); 
		double travelValue3 = rand.nextInt((int)Robot.STANDARD_TRAVEL_SPEED) * rand.nextDouble();
		double travelValue4 = rand.nextInt((int)Robot.STANDARD_TRAVEL_SPEED) * rand.nextDouble();
		double travelValue5 = rand.nextInt((int)Robot.STANDARD_TRAVEL_SPEED) * rand.nextDouble();
		double rotateValue1 = rand.nextInt((int)Robot.STANDARD_ROTATE_SPEED) * rand.nextDouble();
		double rotateValue2 = rand.nextInt((int)Robot.STANDARD_ROTATE_SPEED) * rand.nextDouble();
		double rotateValue3 = rand.nextInt((int)Robot.STANDARD_ROTATE_SPEED) * rand.nextDouble();
		double rotateValue4 = rand.nextInt((int)Robot.STANDARD_ROTATE_SPEED) * rand.nextDouble();
		double rotateValue5 = rand.nextInt((int)Robot.STANDARD_ROTATE_SPEED) * rand.nextDouble();

		//Random movement being executed. 
		r6.travel(travelValue2);
		r6.rotate(rotateValue1);
		r6.travel(travelValue3);
		r6.rotate(rotateValue4);
		r6.travel(travelValue4);
		r6.rotate(rotateValue5);
		r6.travel(travelValue5);
		r6.travel(travelValue1);
		r6.rotate(rotateValue2);
		r6.rotate(rotateValue3);
		
		LinkedBlockingQueue<ActionPacket> currentQueue = r6.queue;
		
		//Testing
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		r6.stop();
		assertEquals(true, r6.getStopFlag());
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true, r6.queue.isEmpty());
		assertEquals(false, r6.isMoving());
		assertEquals(false, r6.getStopFlag()); //after clearing queue and stopping movement stopFlag returns to false
	}
	


}
