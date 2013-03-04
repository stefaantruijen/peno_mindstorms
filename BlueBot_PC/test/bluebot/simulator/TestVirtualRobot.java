package bluebot.simulator;

import static org.junit.Assert.*;

import org.junit.Test;

import bluebot.graph.Border;
import bluebot.graph.Tile;
import bluebot.util.Utils;

/**
 * 
 * @author Dieter
 *
 */
@SuppressWarnings("unused")
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
		int centerImgStartX = 2*VirtualRobot.TILE_SIZE_CM + VirtualRobot.TILE_SIZE_CM/2;
		int centerImgStartY = 0*VirtualRobot.TILE_SIZE_CM + VirtualRobot.TILE_SIZE_CM/2;
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
	
	/**
	 * Tests the sonar in a single tile.
	 */
//	@Test
	public void testGetSonarValue(){
		Tile t00 = new Tile(0,0);
		t00.setAllBordersOpen(false);
		
		VirtualRobot vr = new VirtualRobot(new Tile[]{t00},t00);
		//Minus one cause intersection is
		int maxDistanceNotMoved = (int) (VirtualRobot.TILE_SIZE_CM/2 - VirtualRobot.SONAR_OFFSET_CM)-1;
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadClockWise(90);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadClockWise(90);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadClockWise(90);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(270);
		assertEquals(maxDistanceNotMoved,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		assertEquals(0, vr.getRelativeSonarDirection(),deltaNONZERO_SMALL);
		
		vr.setSpeedRotate(vr.getMaximumSpeedRotate());
		vr.turnRight(90, true);
		assertEquals(0, vr.getRelativeSonarDirection(),deltaZERO);
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
	
	
	
	/**
	 * Tests the sonar in a formation of tiles.
	 * 
	 * This map represents the borders of the tiles.
	 * 
	 * 		 _ _ _
	 * 	1	|    _|
	 * 	0	|  _|_|
	 *       0 1 2 
	 *           
	 * Line is wall
	 * no line is no border.
	 * 
	 * The robot moves around in it (and it's head too) in several directions and checks every time.
	 * 
	 * 
	 */
//	@Test
	public void testGetSonarValue_2(){
		Tile t00 = new Tile(0,0);
		t00.setAllBordersOpen(true);
		t00.setBorderWest(Border.CLOSED);
		Tile t01 = new Tile(0,1);
		t01.setAllBordersOpen(true);
		t01.setBorderWest(Border.CLOSED);
		t01.setBorderNorth(Border.CLOSED);
		Tile t10 = new Tile(1,0);
		t10.setAllBordersOpen(true);
		t10.setBorderEast(Border.CLOSED);
		t10.setBorderSouth(Border.CLOSED);
		Tile t11 = new Tile(1,1);
		t11.setAllBordersOpen(true);
		t11.setBorderNorth(Border.CLOSED);
		Tile t20 = new Tile(2,0);
		t20.setAllBordersOpen(false);
		Tile t21 = new Tile(2,1);
		t21.setAllBordersOpen(false);
		t21.setBorderWest(Border.OPEN);
		
		Tile[] tl = new Tile[]{t00,t01,t10,t11,t20,t21};
		
		VirtualRobot vr = new VirtualRobot(tl,t00);
		//MAKE HIM FAAAASTTT!!
		vr.setSpeedRotate(vr.getMaximumSpeedRotate());
		vr.setSpeedTravel(vr.getMaximumSpeedTravel());
		
		//Minus one cause intersection is ???
		int maxDistanceNotMoved = (int) (VirtualRobot.TILE_SIZE_CM/2 - VirtualRobot.SONAR_OFFSET_CM)-1;
		int halfTile = VirtualRobot.TILE_SIZE_CM/2;
		int oneTileMM =VirtualRobot.TILE_SIZE_CM*10;
		int offsettCM = (int) (VirtualRobot.OFFSET_SENSOR_ULTRASONIC/10);
		
		assertEquals(VirtualRobot.TILE_SIZE_CM + halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadClockWise(180);
		assertEquals(VirtualSonar.NOT_IN_RANGE,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(90);
		assertEquals(VirtualRobot.TILE_SIZE_CM + halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.moveForward(oneTileMM, true);
		assertEquals(VirtualRobot.TILE_SIZE_CM*2 + halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(90);
		assertEquals(halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnLeft(90,true);
		assertEquals(halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.moveBackward(oneTileMM, true);
		assertEquals(VirtualRobot.TILE_SIZE_CM + halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.moveBackward(oneTileMM, true);
		assertEquals(VirtualRobot.TILE_SIZE_CM*2 + halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(90);
		assertEquals(halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(90);
		assertEquals(halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(90);
		assertEquals(halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(90);
		assertEquals(VirtualRobot.TILE_SIZE_CM*2 + halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.moveForward(oneTileMM, true);
		assertEquals(VirtualRobot.TILE_SIZE_CM + halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.turnHeadCounterClockWise(90);
		assertEquals(VirtualRobot.TILE_SIZE_CM + halfTile - offsettCM,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
		vr.moveForward(oneTileMM, true);
		assertEquals(VirtualSonar.NOT_IN_RANGE,vr.readSensorUltraSonic(),deltaNONZERO_SMALL);
	}
	
	@Test
	public void getSonarDirection(){
		VirtualRobot vr = new VirtualRobot(tileList,Tile20);
		assertEquals(0,vr.getHeading(),deltaZERO);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadClockWise(90);
		assertEquals(90,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(90);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(90);
		assertEquals(-90,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(0,vr.getHeading(),deltaZERO);
		assertEquals(270,Utils.clampAngleDegrees((float)(vr.getHeading()+vr.getRelativeSonarDirection())),deltaZERO);
		vr.turnHeadClockWise(90);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		System.out.println("sonardirection before turning right: " + vr.getRelativeSonarDirection() );
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		
		
		
		//RIGHT 360 -- 4 times
		
		vr.turnRight(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(90,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(360);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnRight(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(180,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(360);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnRight(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(270,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(360);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnRight(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(0,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(360);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		//LEFT 360 -- 4 times
		
		vr.turnLeft(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(270,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(360);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnLeft(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(180,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(360);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnLeft(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(90,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(360);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		
		vr.turnLeft(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(0,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(360);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		
		//RIGHT 360 --3 times
		
		vr.turnRight(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(90,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnRight(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(180,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnRight(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(270,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnRight(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(0,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		//LEFT 360 --3 times
		
		vr.turnLeft(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(270,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnLeft(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(180,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnLeft(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(90,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		
		vr.turnLeft(90,true);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		assertEquals(0,vr.getHeading(),deltaZERO);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		vr.turnHeadClockWise(90);
		assertEquals(270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(270);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		
		
		//check negative getSonarDirection()
		vr.turnHeadCounterClockWise(90);
		assertEquals(-90,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(90);
		assertEquals(-180,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(90);
		assertEquals(-270,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(90);
		assertEquals(-360,vr.getRelativeSonarDirection(),deltaZERO);
		vr.turnHeadCounterClockWise(90);
		assertEquals(-450,vr.getRelativeSonarDirection(),deltaZERO);
		
		vr.turnHeadClockWise(450);
		assertEquals(0,vr.getRelativeSonarDirection(),deltaZERO);
		
		
	}

	
	
}
