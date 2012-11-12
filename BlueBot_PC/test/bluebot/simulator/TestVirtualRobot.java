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
	private static double deltaNONZERO_SMALL = 1;
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
		int centerImgStartX = 2*VirtualRobot.TILE_SIZE + VirtualRobot.TILE_SIZE/2;
		int centerImgStartY = 0*VirtualRobot.TILE_SIZE + VirtualRobot.TILE_SIZE/2;
		assertEquals(centerImgStartX, vr.getImgStartX(),VirtualRobot.randomMaxOffset);
		assertEquals(centerImgStartY, vr.getImgStartY(),VirtualRobot.randomMaxOffset);
	}
	
//	@Test
	//TODO: modify
	public void moveForward(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		double speed = vr.getTravelSpeed();
		long startTime = System.currentTimeMillis();
		vr.moveForward();
		assertEquals(Action.TRAVEL, vr.getCurrentAction());
		assertEquals((double)Float.MAX_VALUE, (double)vr.getCurrentArgument(),deltaZERO);
		assertEquals(Long.MAX_VALUE, vr.getCurrentActionETA());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedSeconds = (System.currentTimeMillis() - startTime)/1000;
		assertEquals(vr.getInitAbsoluteY()+elapsedSeconds*speed, vr.getY(),deltaNONZERO_SMALL);
		assertEquals(vr.getImgStartY()+vr.getInitAbsoluteY()+elapsedSeconds*speed, vr.getImgY(),deltaNONZERO_SMALL);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedSeconds2 = (System.currentTimeMillis() - startTime)/1000;
		assertEquals(vr.getInitAbsoluteY()+elapsedSeconds2*speed, vr.getY(),deltaNONZERO_SMALL);
		assertEquals(vr.getImgStartY()+vr.getInitAbsoluteY()+elapsedSeconds2*speed, vr.getImgY(),deltaNONZERO_SMALL);
	}
	
//	@Test
	//TODO: modify
	public void moveForward2(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
//		System.out.println("start y: " + vr.getY());
		double speed = vr.getTravelSpeed();	
		float distanceToTravel = 4;
//		System.out.println("will travel " + distanceToTravel);
		long durationSec = (long) (distanceToTravel/speed);
		long halfDurationSec = durationSec/2;
		vr.moveForward(distanceToTravel, false);
		
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("halfway:" + vr.getY());
		assertEquals(vr.getInitAbsoluteY() + distanceToTravel/2, vr.getY(),deltaNONZERO_SMALL);
//		System.out.println("halfway +some:" + vr.getY());
		assertEquals(vr.getImgStartY() +vr.getInitAbsoluteY()+distanceToTravel/2, vr.getImgY(),deltaNONZERO_SMALL);
		
		
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(vr.getInitAbsoluteY() + distanceToTravel, vr.getY(),deltaZERO);
		assertEquals(vr.getImgStartY() +vr.getInitAbsoluteY()+distanceToTravel, vr.getImgY(),deltaZERO);
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(vr.getInitAbsoluteY() + distanceToTravel, vr.getY(),deltaZERO);
		assertEquals(vr.getImgStartY() +vr.getInitAbsoluteY()+distanceToTravel, vr.getImgY(),deltaZERO);
	}
	
//	@Test
	//TODO: modify
	public void moveBackward(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		double speed = vr.getTravelSpeed();
		long startTime = System.currentTimeMillis();
		vr.moveBackward();
		assertEquals(Action.TRAVEL, vr.getCurrentAction());
		assertEquals((double)-Float.MAX_VALUE, (double)vr.getCurrentArgument(),deltaZERO);
		assertEquals(Long.MAX_VALUE, vr.getCurrentActionETA());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedSeconds = (System.currentTimeMillis() - startTime)/1000;
		assertEquals(vr.getInitAbsoluteY()-(elapsedSeconds*speed), vr.getY(),deltaNONZERO_SMALL);
		assertEquals(vr.getImgStartY()+vr.getInitAbsoluteY()-(elapsedSeconds*speed), vr.getImgY(),deltaNONZERO_SMALL);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedSeconds2 = (System.currentTimeMillis() - startTime)/1000;
		assertEquals(vr.getInitAbsoluteY()-(elapsedSeconds2*speed), vr.getY(),deltaNONZERO_SMALL);
		assertEquals(vr.getImgStartY()+vr.getInitAbsoluteY()-(elapsedSeconds2*speed), vr.getImgY(),deltaNONZERO_SMALL);
	}
	
//	@Test
	//TODO: modify
	public void moveBackward2(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
//		System.out.println("start y: " + vr.getY());
		double speed = vr.getTravelSpeed();	
		float distanceToTravel = 4;
//		System.out.println("will travel " + distanceToTravel);
		long durationSec = (long) (distanceToTravel/speed);
		long halfDurationSec = durationSec/2;
		vr.moveBackward(distanceToTravel, false);
		
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("halfway:" + vr.getY());
		assertEquals(vr.getInitAbsoluteY() - distanceToTravel/2, vr.getY(),deltaNONZERO_SMALL);
//		System.out.println("halfway +some:" + vr.getY());
		assertEquals(vr.getImgStartY() +vr.getInitAbsoluteY()-distanceToTravel/2, vr.getImgY(),deltaNONZERO_SMALL);
		
		
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(vr.getInitAbsoluteY() - distanceToTravel, vr.getY(),deltaZERO);
		assertEquals(vr.getImgStartY() +vr.getInitAbsoluteY()-distanceToTravel, vr.getImgY(),deltaZERO);
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(vr.getInitAbsoluteY() - distanceToTravel, vr.getY(),deltaZERO);
		assertEquals(vr.getImgStartY() +vr.getInitAbsoluteY()-distanceToTravel, vr.getImgY(),deltaZERO);
	}

//	@Test 
	public void turnLeft(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		assertEquals(0, vr.getHeading(),deltaZERO);
		double speed = vr.getRotateSpeed();
		long startTime = System.currentTimeMillis();
		vr.turnLeft();
		assertEquals(Action.ROTATE, vr.getCurrentAction());
		assertEquals((double)-Float.MAX_VALUE, (double)vr.getCurrentArgument(),deltaZERO);
		assertEquals(Long.MAX_VALUE, vr.getCurrentActionETA());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedMS = System.currentTimeMillis() - startTime;
		float rotatedAngle = (float) (elapsedMS*speed/(double)(1000));
		float totalAngle = vr.getInitAbsoluteHeading()-rotatedAngle; //Minus because going left.
		assertEquals(Utils.clampAngleDegrees(totalAngle), vr.getHeading(),deltaNONZERO_SMALL);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedMS2 = System.currentTimeMillis() - startTime;
		float rotatedAngle2 = (float) (elapsedMS2*speed/(double)(1000));
		float totalAngle2 = vr.getInitAbsoluteHeading()-rotatedAngle2; //Minus because going left.
		assertEquals(Utils.clampAngleDegrees(totalAngle2), vr.getHeading(),deltaNONZERO_SMALL);
	}

//Under construction
//	@Test
	public void turnLeft2(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		assertEquals(0, vr.getHeading(),deltaZERO);
		double speed = vr.getRotateSpeed();
		float distanceToTravel = 120;
		long durationSec = (long) (distanceToTravel/speed);
		long halfDurationSec = durationSec/2;
		long startTime = System.currentTimeMillis();
		vr.turnLeft(distanceToTravel,false);
		assertEquals(Action.ROTATE, vr.getCurrentAction());
		assertEquals((double)-distanceToTravel, vr.getCurrentArgument(),deltaZERO);
//TODO: fix this assert
//		assertEquals(distanceToTravel, vr.getCurrentActionETA());
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("distanceToTravel/2: " + distanceToTravel/2);
		assertEquals(Utils.clampAngleDegrees(vr.getInitAbsoluteHeading()-distanceToTravel/2), vr.getHeading(),deltaNONZERO_SMALL);
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		long elapsedMS2 = System.currentTimeMillis() - startTime;
//		float rotatedAngle2 = (float) (elapsedMS2*speed/(double)(1000));
//		float totalAngle2 = vr.getInitAbsoluteHeading()-rotatedAngle2; //Minus because going left.
		assertEquals(Utils.clampAngleDegrees(vr.getInitAbsoluteHeading()-distanceToTravel), vr.getHeading(),deltaNONZERO_SMALL);
	}

//Under construction
//	@Test
	public void turnRight(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		assertEquals(0, vr.getHeading(),deltaZERO);
		double speed = vr.getRotateSpeed();
		long startTime = System.currentTimeMillis();
		vr.turnRight();
		assertEquals(Action.ROTATE, vr.getCurrentAction());
		assertEquals((double)Float.MAX_VALUE, (double)vr.getCurrentArgument(),deltaZERO);
		assertEquals(Long.MAX_VALUE, vr.getCurrentActionETA());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedMS = System.currentTimeMillis() - startTime;
		float rotatedAngle = (float) (elapsedMS*speed/(double)(1000));
		float totalAngle = vr.getInitAbsoluteHeading()+rotatedAngle; //Addition because going Right.
		assertEquals(Utils.clampAngleDegrees(totalAngle), vr.getHeading(),deltaNONZERO_SMALL);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long elapsedMS2 = System.currentTimeMillis() - startTime;
		float rotatedAngle2 = (float) (elapsedMS2*speed/(double)(1000));
		float totalAngle2 = vr.getInitAbsoluteHeading()+rotatedAngle2; //Addition because going Right.
		assertEquals(Utils.clampAngleDegrees(totalAngle2), vr.getHeading(),deltaNONZERO_SMALL);
	}

//Under construction
//	@Test
	public void turnRight2(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		assertEquals(0, vr.getHeading(),deltaZERO);
		double speed = vr.getRotateSpeed();
		float distanceToTravel = 120;
		long durationSec = (long) (distanceToTravel/speed);
		long halfDurationSec = durationSec/2;
		long startTime = System.currentTimeMillis();
		vr.turnRight(distanceToTravel,false);
		assertEquals(Action.ROTATE, vr.getCurrentAction());
		assertEquals((double)distanceToTravel, vr.getCurrentArgument(),deltaZERO);
//TODO: fix this assert
//		assertEquals(distanceToTravel, vr.getCurrentActionETA());
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("distanceToTravel/2: " + distanceToTravel/2);
		assertEquals(Utils.clampAngleDegrees(vr.getInitAbsoluteHeading()+distanceToTravel/2), vr.getHeading(),deltaNONZERO_SMALL);
		try {
			Thread.sleep(halfDurationSec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		long elapsedMS2 = System.currentTimeMillis() - startTime;
//		float rotatedAngle2 = (float) (elapsedMS2*speed/(double)(1000));
//		float totalAngle2 = vr.getInitAbsoluteHeading()-rotatedAngle2; //Minus because going left.
		assertEquals(Utils.clampAngleDegrees(vr.getInitAbsoluteHeading()+distanceToTravel), vr.getHeading(),deltaNONZERO_SMALL);
	}
	
//	@Test
	//Under construction
	public void testGetLightValue(){
		Tile20.setAllBordersOpen(false);
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		int r1 = vr.readSensorLight();
		
	}
	
	//Under construction
	@Test
	public void testGetSonarValue(){
		Tile t00 = new Tile(0,0);
		t00.setAllBordersOpen(false);
		
		VirtualRobot vr = new VirtualRobot(new Tile[]{t00},t00);
		//Minus one cause intersection is
		int maxDistanceNotMoved = (int) (VirtualRobot.TILE_SIZE/2 - VirtualRobot.SONAR_OFFSET_CM)-1;
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadClockWise(90);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadClockWise(90);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadClockWise(90);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(270);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		assertEquals(0, vr.getSonarDirection(),deltaNONZERO_SMALL);
		
		vr.setSpeedRotate(vr.getMaximumSpeedRotate());
		vr.turnRight(90, true);
		assertEquals(0, vr.getSonarDirection(),deltaZERO);
		assertEquals(90, vr.getHeading(),deltaZERO);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);

		vr.setSpeedRotate(vr.getMaximumSpeedRotate());
		vr.turnRight(90, true);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		
		vr.setSpeedRotate(vr.getMaximumSpeedRotate());
		vr.turnRight(90, true);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		
		vr.setSpeedRotate(vr.getMaximumSpeedRotate());
		vr.turnRight(90, true);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
	}
}
