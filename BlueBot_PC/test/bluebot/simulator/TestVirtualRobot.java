package bluebot.simulator;

import static org.junit.Assert.*;

import org.junit.Test;

import bluebot.graph.Tile;
import bluebot.util.Utils;

/**
 * 
 * @author Dieter
 *
 */
public class TestVirtualRobot {
	private static double deltaZERO = 0;
	private static double deltaNONZERO_SMALL = 4;
	private static double deltaNONZERO_LARGE = 100;


	private static Tile Tile00 = new Tile(0,0);
	private static Tile Tile01 = new Tile(0,1);
	private static Tile Tile02 = new Tile(0,2);
	private static Tile Tile03 = new Tile(0,3);
	private static Tile Tile10 = new Tile(1,0);
	private static Tile Tile11 = new Tile(1,1);
	private static Tile Tile12 = new Tile(1,2);
	private static Tile Tile13 = new Tile(1,3);
	private static Tile Tile20 = new Tile(2,0);
	private static Tile Tile21 = new Tile(2,1);
	private static Tile Tile22 = new Tile(2,2);
	private static Tile Tile23 = new Tile(2,3);
	private static Tile Tile30 = new Tile(3,0);
	private static Tile Tile31 = new Tile(3,1);
	private static Tile Tile32 = new Tile(3,2);
	private static Tile Tile33 = new Tile(3,3);
	private static Tile[] tileList= new Tile[]{	
		Tile00, 
		Tile01,
		Tile02,
		Tile03,
		Tile10,
		Tile11,
		Tile12,
		Tile13,
		Tile20,
		Tile21,
		Tile22,
		Tile23,
		Tile30,
		Tile31,
		Tile32,
		Tile33};

	
	@Test
	public void VirtualRobot2() {
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		int x = 2*VirtualRobot.TILE_SIZE + VirtualRobot.TILE_SIZE/2;
		int y = 0*VirtualRobot.TILE_SIZE + VirtualRobot.TILE_SIZE/2;
		assertEquals(x, vr.getImgStartX());
		assertEquals(y, vr.getImgStartY());
	}
	
	@Test
	public void moveForward(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		double speed = vr.getTravelSpeed();
		long startTime = System.currentTimeMillis();
		vr.moveForward();
		assertEquals(Action.TRAVEL, vr.getCurrentAction());
		assertEquals((double)Float.MAX_VALUE, vr.getCurrentArgument(),deltaZERO);
		assertEquals(Long.MAX_VALUE, vr.getCurrentActionETA());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedSeconds = (System.currentTimeMillis() - startTime)/1000;
		assertEquals(elapsedSeconds*speed, vr.getY(),deltaNONZERO_SMALL);
		assertEquals(vr.getImgStartY()+elapsedSeconds*speed, vr.getImgY(),deltaNONZERO_SMALL);

	}
	
	@Test
	public void moveForward1(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		assertEquals(0, vr.getY(),deltaZERO);
		double speed = vr.getTravelSpeed();	
		float distanceToTravel = 2;
		long durationSec = (long) (distanceToTravel/speed);
		long halfDurationSec = durationSec/2;
		vr.moveForward(distanceToTravel, false);
		
		try {
			Thread.sleep(durationSec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(distanceToTravel, vr.getY(),deltaZERO);
		assertEquals(vr.getImgStartY() + distanceToTravel, vr.getImgY(),deltaZERO);
//		
//		long halfTimeSec = durationSec/2;
//		long startTime = System.currentTimeMillis();
//		vr.moveForward(distanceToTravel, false);
//		assertEquals(Action.TRAVEL, vr.getCurrentAction());
//		assertEquals(distanceToTravel, vr.getCurrentArgument(),deltaZERO);
////		assertEquals(startTime + 1000*durationSec, vr.getCurrentActionETA(),deltaNONZERO_LARGE);
//		try {
//			Thread.sleep(1000*halfTimeSec);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		assertEquals(halfTimeSec*speed, vr.getY(),deltaNONZERO_SMALL);
//		assertEquals(vr.getImgStartY()+halfTimeSec*speed, vr.getImgY(),deltaNONZERO_SMALL);
//		try {
//			Thread.sleep(2000*halfTimeSec);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		assertEquals(durationSec*speed, vr.getY(),deltaNONZERO_SMALL);
//		assertEquals(vr.getImgStartY()+durationSec*speed, vr.getImgY(),deltaNONZERO_SMALL);		
	}
	
	@Test
	public void moveBackward(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);

		double speed = vr.getTravelSpeed();
		long startTime = System.currentTimeMillis();
		vr.moveBackward();
		assertEquals(Action.TRAVEL, vr.getCurrentAction());
		assertEquals((double)-Float.MAX_VALUE, vr.getCurrentArgument(),deltaZERO);
		assertEquals(Long.MAX_VALUE, vr.getCurrentActionETA());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedSeconds = (System.currentTimeMillis() - startTime)/1000;
		assertEquals(-(elapsedSeconds*speed), vr.getY(),deltaNONZERO_SMALL);
		assertEquals(vr.getImgStartY()+(-(elapsedSeconds*speed)), vr.getImgY(),deltaNONZERO_SMALL);
	}
	
	@Test
	public void moveBackward1(){
//		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
//
//		double speed = vr.getTravelSpeed();
//		long startTime = System.currentTimeMillis();
//		float distanceToTravel = 100;
//		long ETA = (long) (startTime+distanceToTravel/speed);
//		long halfTime = (ETA-System.currentTimeMillis())/2;
//		vr.moveBackward(distanceToTravel, false);
//		assertEquals(Action.TRAVEL, vr.getCurrentAction());
//		assertEquals(distanceToTravel, vr.getCurrentArgument(),deltaZERO);
//		assertEquals(ETA, vr.getCurrentActionETA(),deltaZERO);
//		try {
//			Thread.sleep(halfTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		long elapsedSeconds = (System.currentTimeMillis() - startTime)/1000;
//		assertEquals(-elapsedSeconds*speed, vr.getY(),deltaZERO);
//		assertEquals(vr.getImgStartY()+elapsedSeconds*speed, vr.getImgY(),deltaZERO);
//		try {
//			Thread.sleep(2*halfTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		long elapsedSeconds2 = (System.currentTimeMillis()- startTime)/1000;
//		assertEquals(-elapsedSeconds2*speed, vr.getY(),deltaZERO);
//		assertEquals(vr.getImgStartY()+(-elapsedSeconds2*speed), vr.getImgY(),deltaZERO);		
		
	}

	

	@Test
	public void turnLeft1(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		assertEquals(0, vr.getHeading(),deltaZERO);
		double speed = vr.getRotateSpeed();	
		float angleToTravel = 90;
		long durationSec = (long) (angleToTravel/speed);
		long halfDurationSec = durationSec/2;
		vr.turnLeft(angleToTravel, false);
		assertEquals(0, vr.getHeading(),deltaNONZERO_SMALL);

		try {
			Thread.sleep(halfDurationSec/2*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(vr.getHeading());
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(Utils.clampAngleDegrees(-angleToTravel/2), vr.getHeading(),deltaNONZERO_SMALL);

		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(Utils.clampAngleDegrees(-angleToTravel), vr.getHeading(),deltaZERO);
	}
	
	@Test
	public void turnRight1(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		assertEquals(0, vr.getHeading(),deltaZERO);
		double speed = vr.getRotateSpeed();	
		float angleToTravel = 90;
		long durationSec = (long) (angleToTravel/speed);
		long halfDurationSec = durationSec/2;
		vr.turnRight(angleToTravel, false);
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(angleToTravel/2, vr.getHeading(),deltaNONZERO_SMALL);

		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(angleToTravel, vr.getHeading(),deltaZERO);
	}

}
