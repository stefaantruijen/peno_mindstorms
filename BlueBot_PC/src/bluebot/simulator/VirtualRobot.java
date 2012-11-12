package bluebot.simulator;
import java.util.Random;

import bluebot.AbstractRobot;
import bluebot.Robot;
import bluebot.graph.Tile;
import bluebot.util.Orientation;
import bluebot.util.Utils;

/*
 * Idee: getX() getY() is telkens t.o.v. het midden van de eerste Tile die noemen we (0,0)
 * 
 * Initieel zetten we de robot random binnen die eerste tile. 
 * 
 * Robot.Orientate() zorgt er dan voor dat de robot terug in het midden van de eerste Tile komt staan   
 */

/**
 * The {@link Robot} implementation for the simulator
 * 
 * @author Dieter Castel, Ruben Feyen, Michiel Ruelens 
 */
public class VirtualRobot extends AbstractRobot {
	/**
 	 *Static that holds the maximum travel speed in degrees/s.
	 */
	private static final float MaxRotateSpeed = 90;
	/**
 	 *Static that holds the maximum travel speed in mm/s.
	 */
	private static final float MaxTravelSpeed = 400;
	/**
	 * The size of the tiles on which the VirtualRobot will be driving.
	 */
	public static int TILE_SIZE = 40;
	/**
	 * Static that holds the standard travel speed in mm/s. This is the speed we measured in the real NXT robot.
	 */
	public static double STANDARD_TRAVEL_SPEED = 200; //Probably get this value from other class.//TODO: see what this is irl
	/**
	 * Static that holds the standard rotate speed in degrees/s. This is the speed we measured in the real NXT robot.
	 */
	public static double STANDARD_ROTATE_SPEED = 30; //Probably get this value from other class.//TODO: see what this is irl
	/**
	 * Static that holds the standard rotate speed in degrees/s. This is the speed we measured in the real NXT robot.
	 */
	public static double STANDARD_SONAR_ROTATE_SPEED = 60; //Probably get this value from other class.//TODO: see what this is irl
	

	/**
	 * Distance from the center of the VirtualRobot to the position of the lightSensor in centimeters.
	 */
	public static int LIGHT_SENSOR_OFFSET_CM = (int)(Robot.OFFSET_SENSOR_LIGHT)/10;
	/**
	 * Distance from the center of the VirtualRobot to the position of the Sonar in centimeters.
	 */
	public static int SONAR_OFFSET_CM = (int)(Robot.OFFSET_SENSOR_ULTRASONIC)/10;
	
	/**
	 * Variable holding the travel speed of the robot.
	 */
	private double travelSpeed;
	
	/**
	 * Variable holding the rotate speed of the robot.
	 */
	private double rotateSpeed;
	
	/**
	 * Variable hodling the rotate speed of the sonar.
	 */
	private double sonarRotateSpeed;
	
	/**
	 * Variable representing the absolute horizontal coordinate at the start of the current move in the global coordinate system.
	 * Origin being the center of the first tile.
	 */
	private float initAbsoluteX;
	/**
	 * Variable representing the vertical coordinate at the start of the current move in the global coordinate system 
	 * Origin being the center of the first tile.
	 */
	private float initAbsoluteY;
	/**
	 * Starting x coordinate of the robot in the global plane (or on the 'image').
	 */
	private int imgStartX;
	/**
	 * Starting y coordinate of the robot in the global plane (or on the 'image').
	 */
	private int imgStartY;
	/**
	 * Variable representing the absolute heading of this robot in degrees (0� being North) at the start of the movement. 
	 * 	initAbsoluteHeading is initialized to 0 at construct. (So it always starts pointing 'north').
	 */
	private float initAbsoluteHeading;

	/**
	 * Variable hodling the rotate speed of the sonar.
	 */
	private double sonarDirection;
	/**
	 * Variable representing the current action of the simulator.
	 */
	private Action currentAction;
	/**
	 * Variable holding the argument corresponding to the <code>currentAction</code>.
	 */
	private float currentArgument;
	/**
	 * Variable holding the time in milliseconds at which the current action will be completed.
	 */
	private long currentActionETA;
	/**
	 * Variable holding the start time of the {@link currentAction} in milliseconds.
	 */
	private long timestamp;
	/**
	 * Boolean declaring whether the current action is blocking.
	 */
	private boolean isWaiting;
	/**
	 * List of tiles that this VirtualRobot will use.
	 */
	private Tile[] tilesList;
	/**
	 * Sensors object that holds the sensors of the VirtualRobot.
	 */
	private Sensors sensors;
	/**
	 * VirtualLightSensor object that holds the light sensor of the VirtualRobot.
	 */
	private VirtualLightSensor lightSensor;
	/**
	 * VirtualSonar object that holds the sonar of the VirtualRobot.
	 */
	private VirtualSonar sonar;
	private int tileImgStartX;
	private int tileImgStartY;
	protected static int randomMaxOffset = 10;
	
	
	//CONSTRUCTORS: 
	/**
	 * Creates a new VirtualRobot object with given tiles and start tile
	 * 
	 * @param tilesList
	 *			List of Tile objects that will be used for generating sensor values. 
	 * @param startTile
	 * 			Tile at which the robot will start.
	 */
	public VirtualRobot(Tile[] tilesList,Tile startTile){
		if(isValid(tilesList,startTile)){
			this.tilesList= tilesList;
			//Sets the robot random in the startTile
			setRandomInStartTile(startTile);
			this.tileImgStartX = startTile.getX()*TILE_SIZE + TILE_SIZE/2;
			this.tileImgStartY = startTile.getY()*TILE_SIZE + TILE_SIZE/2;
			setImgStartX(tileImgStartX);
			setImgStartY(tileImgStartY);
			this.sensors = new Sensors(this.tilesList);
			lightSensor = sensors.getLightSensor();
			sonar = sensors.getSonar();
			setTravelSpeed(STANDARD_TRAVEL_SPEED);
			setRotateSpeed(STANDARD_ROTATE_SPEED);
			setSonarRotateSpeed(STANDARD_SONAR_ROTATE_SPEED);
			clearAction();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private void setImgStartX(int x){
		imgStartX = x;
	}
	
	public int getImgStartX(){
		return imgStartX;
	}
	
	private void setImgStartY(int y){
		imgStartY = y;
	}
	public int getImgStartY(){
		return imgStartY;
	}

	public int getImgX(){
//		System.out.println("getX = " + getX() + "(rounded = " + (int)(Math.round(getX())) +")");
//		System.out.println("getImgStartX" + getImgStartX());
		int XinCM = Math.round(getX()/10);
		return getImgStartX() + XinCM ;
	}
	public int getImgY(){
//		System.out.println("getY = " + getY() + "(rounded = " + (int)(Math.round(getY())) +")");
//		System.out.println("getImgStartY" + getImgStartY());
		int YinCM = Math.round(getY()/10);
		return getImgStartY() + YinCM;
	}
	//GETTERS AND SETTERS
	private void setInitAbsoluteX(float x) {
		this.initAbsoluteX = x;
	}

	/**
	 * Returns the horizontal coordinate at the start of the current move in the global coordinate system (Origin being the center of the first tile).
	 * @return
	 */
	public float getInitAbsoluteX(){
		return initAbsoluteX;
	}

	/**
	 * Sets the initial absolute Y.
	 * @param y
	 */
	private void setInitAbsoluteY(float y) {
		this.initAbsoluteY = y;
	}

	/**
	 *  Returns the vertical coordinate at the start of the current move in the global coordinate system (Origin being the center of the first tile).
	 * @return
	 */
	public float getInitAbsoluteY(){
		return initAbsoluteY;
	}
	
	private void setInitSonarDirection(float dir) {
		sonarDirection = dir;
	}

	public float getInitSonarDirection() {
		return (float) sonarDirection;
	}

	/**
	 * Sets the initial Absolute heading.
	 * 
	 * To be used when finishing a move.
	 * @param absoluteHeading
	 */
	private void setInitAbsoluteHeading(float absoluteHeading) {
		this.initAbsoluteHeading = absoluteHeading;
	}

	/**
	 * Returns the heading (in degrees) of the Robot at the start of the current move.
	 * @return
	 */
	public float getInitAbsoluteHeading() {
		return initAbsoluteHeading;
	}

	private void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}

	public Action getCurrentAction() {
		return currentAction;
	}

	private void setCurrentArgument(float currentArgument) {
		this.currentArgument = currentArgument;
	}

	public float getCurrentArgument() {
		return currentArgument;
	}

	private void setWaiting(boolean isWaiting) {
		this.isWaiting = isWaiting;
	}

	public boolean isWaiting() {
		return isWaiting;
	}

	private void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	private void setCurrentActionETA(long currentActionETA) {
		this.currentActionETA = currentActionETA;
	}

	public long getCurrentActionETA() {
		return currentActionETA;
	}

	/**
	 * Sets the travelspeed to the given speed.
	 * 
	 * @param speed
	 */
	//TODO:@Override?
	public void setTravelSpeed(double speed) {
		this.travelSpeed = speed;
	}

	/**
	 * Returns the travel speed of the VirtualRobot.
	 * 
	 * @return
	 */
	//TODO:@Override?
	public double getTravelSpeed() {
		return travelSpeed;
	}

	/**
	 * Sets the rotate speed to the given speed.
	 * @param speed
	 */
	//TODO:@Override?
	public void setRotateSpeed(double speed) {
		this.rotateSpeed = speed;
	}

	/**
	 * The rotate speed of this VirtualRobot.
	 * @return
	 */
	//TODO:@Override?
	public double getRotateSpeed() {
		return rotateSpeed;
	}

	
	/**
	 * Gets the rotate speed of the sonar
	 * @return
	 */
	//TODO:@Override?
	public double getSonarRotateSpeed() {
		return sonarRotateSpeed;
	}

	/**
	 * Sets the rotate speed of the sonar
	 * @return
	 */
	//TODO:@Override?
	public void setSonarRotateSpeed(double speed) {
		 sonarRotateSpeed = speed;
	}

	/**
	 * This is the direction of the sonar head relative to the heading of the VirtualRobot.
	 * 
	 * Can either be negative or positive.
	 * @return
	 */
	//TODO:@Override?
	public float getSonarDirection(){
		float result = getInitSonarDirection();
		if(getCurrentAction() == Action.SONAR){
			if(System.currentTimeMillis() >= getCurrentActionETA()){
				result += getCurrentArgument();
			} else  {
				Long elapsedTimeMS = System.currentTimeMillis() - getTimestamp();
				float arg = getCurrentArgument();
				double sign = (arg/Math.abs(arg));
				float distance = (float)(elapsedTimeMS* getSonarRotateSpeed()/(double)(1000));
				result += sign * distance;
			}
		}
		return result;		
	}

	
	//IMPLEMENTATION OF ABSTRACT METHODS (all @Override methods)
	/**
	 * Returns the x-coordinate which is the amount of mm in the x-direction the robot is removed from (0,0)
	 */
	@Override
	public float getX() {
		float result = getInitAbsoluteX();
		if (getCurrentAction() == Action.TRAVEL){
			if(System.currentTimeMillis()>=getCurrentActionETA()){//If action is finished:
//				System.out.println("X:Action finished?");
				float arg = getCurrentArgument();
				int sign = (int)(arg/Math.abs(arg));
				float distance = Math.abs(arg);
				result += sign*additionToX(distance);
			} else {
//				System.out.println("X:Action running?");
				Long elapsedTimeMS = System.currentTimeMillis() - getTimestamp();
				float arg = getCurrentArgument();
				int sign = (int)(arg/Math.abs(arg));
				float distance = (float) (elapsedTimeMS*getTravelSpeed()/(double)(1000));
				result += sign*additionToX(distance);
			}
		}
		return result;
	}

	/**
	 * Returns the y-coordinate which is the amount of mm in the x-direction the robot is removed from (0,0)
	 */
	@Override
	public float getY(){
		float result = getInitAbsoluteY();
		if (getCurrentAction() == Action.TRAVEL){
			if(System.currentTimeMillis()>=getCurrentActionETA()){//If action is finished:
				float arg = getCurrentArgument();
				int sign = (int)(arg/Math.abs(arg));
				float distance = Math.abs(arg);
				result += sign*additionToY(distance);
			} else {
				Long elapsedTimeMS = System.currentTimeMillis() - getTimestamp();
				float arg = getCurrentArgument();
				int sign = (int)(arg/Math.abs(arg));
				float distance = (float) (elapsedTimeMS*getTravelSpeed()/(double)(1000));
				result += sign*additionToY(distance);
			}
		}
		return result;
	}

	/**
	 * Returns whether or not the simulator robot is still moving.
	 */
	@Override
	public boolean isMoving() {
		return System.currentTimeMillis()<getCurrentActionETA();
	}
	@Override
	public void moveBackward() {
		moveBackward(Float.MAX_VALUE, false);
	}
	@Override
	public void moveBackward(float distance, boolean wait) {
		commitPreviousAction();
		setCurrentAction(Action.TRAVEL);
		setCurrentArgument(-distance);
		initializeMove(wait);	
	}
	@Override
	public void moveForward() {
		moveForward(Float.MAX_VALUE, false);
	}
	@Override
	public void moveForward(float distance, boolean wait) {
		commitPreviousAction();
		setCurrentAction(Action.TRAVEL);
		setCurrentArgument(distance);
		initializeMove(wait);	
	}

	/**
	 * Returns the light sensor value at the current position and heading.
	 */
	@Override
	public int readSensorLight() {
		double radialHeading = Math.toRadians(getHeading());
		double xOffset = LIGHT_SENSOR_OFFSET_CM * Math.sin(radialHeading);
		int sensorX = (int) (getImgX() + xOffset);
		double yOffset = LIGHT_SENSOR_OFFSET_CM * Math.cos(radialHeading);
		int sensorY = (int) (getImgY() + yOffset);
		return lightSensor.getLightValue(sensorX, sensorY);
	}
	
	/**
	 * Returns the sonar value at the current position and heading.
	 */
	@Override
	public int readSensorUltraSonic() {
		float sonarHeading = Utils.clampAngleDegrees(getHeading()+getSonarDirection());
		double radialSonarHeading = Math.toRadians(sonarHeading);
		double xOffset = SONAR_OFFSET_CM*Math.sin(radialSonarHeading);
		System.out.println("xOffset: " + xOffset);
		int sensorX = (int)(getImgX() + xOffset);
		double yOffset = (int)(SONAR_OFFSET_CM * Math.cos(radialSonarHeading));
		System.out.println("yOffset: "+yOffset);
		int sensorY = (int)(getImgY() + yOffset);
		System.out.println("gona read: (" + sensorX +"," +sensorY+")" );
		return sonar.getSonarValue(sensorX, sensorY,sonarHeading);
	}
	
	/**
	 * Resets the orientation of this robot.
	 * This assumes that the robot drove to the center of the start tile with his heading north. 
	 * That is (0,0)
	 */
	@Override
	public void resetOrientation() {
		//Clear any possible action so no problems occur.
		System.out.println("Resetting orientation");
		clearAction();
		//Set every inital value correctly.
		setInitAbsoluteX(0);
		setInitAbsoluteY(0);
//		setInitAbsoluteHeading(0);
		setInitSonarDirection(0);
		setImgStartX(getImgX());
		setImgStartY(getImgY());
	}
	
	@Override
	public void stop() {
		commitPreviousAction();
		clearAction();
	}

	/**
	 * Turns the VirtualRobot left until stopped.
	 */
	@Override
	public void turnLeft() {
		turnLeft(Float.MAX_VALUE,false);
	}
	
	/**
	 * Turns the VirtualRobot left by the given angle.
	 */
	@Override
	public void turnLeft(float angle, boolean wait) {
		commitPreviousAction();
		setCurrentAction(Action.ROTATE);
		setCurrentArgument(-angle);
		initializeMove(wait);		
	}
	
	/**
	 * Turns the VirtualRobot right until stopped.
	 */
	@Override
	public void turnRight() {
		turnRight(Float.MAX_VALUE,false);
	}
	
	/**
	 * Turns the VirtualRobot right by the given angle.
	 */
	@Override
	public void turnRight(float angle, boolean wait) {
		commitPreviousAction();
		setCurrentAction(Action.ROTATE);
		setCurrentArgument(angle);
		initializeMove(wait);
	}

	/**
	 * Returns the difference between the heading at the start of the current move and the current heading.
	 */
	@Override
	public float getAngleIncrement() {
		//TODO: dependant on how real NXT side works Math.abs(...).
		return  getHeading() - getInitAbsoluteHeading();
	}
	
	/**
	 * Returns the current heading of the VirtualRobot.
	 * 
	 */
	@Override
	public float getHeading(){
		float result = getInitAbsoluteHeading();
		if(getCurrentAction() == Action.ROTATE){
			if(System.currentTimeMillis()>=getCurrentActionETA()){//If action is finished:
				result += getCurrentArgument();
			} else {
				Long elapsedTimeMS = System.currentTimeMillis() - getTimestamp();
				double arg = getCurrentArgument();
				int sign = (int)(arg/Math.abs(arg));
				float distance = (float) (elapsedTimeMS*getRotateSpeed()/(double)(1000));
				result += sign * distance;
			}
		}
		return Utils.clampAngleDegrees(result);
	}
	
	/**
	 * Returns the current orientation of VirtualRobot.
	 */
	@Override
	public Orientation getOrientation() {
		return new Orientation(getX(), getY(), getHeading());
	}
	
	/**
	 * Turns the head of the robot (the sonar) clock wise (right).
	 * Same convention Right (CWise) results in a addition.
	 *  
	 * WARNING: This is a blocking action right now!
	 */
	//TODO decide true or false for waiting?
	@Override
	public void turnHeadClockWise(int offset) {
		commitPreviousAction();
		setCurrentAction(Action.SONAR);
		setCurrentArgument((float)offset);
		initializeMove(true);	
	}

	/**
	 * Turns the head of the robot (the sonar) counter clock wise (left).
	 * Same convention Left (CCWise) results in a subtraction.
	 * 
	 * WARNING: This is a blocking action right now!
	 */
	//TODO decide true or false for waiting?
	@Override
	public void turnHeadCounterClockWise(int offset) {
		commitPreviousAction();
		setCurrentAction(Action.SONAR);
		setCurrentArgument((float)-offset);
		initializeMove(true);
	}
	
	//Refactor with setRotateSpeed?
	@Override
	public void setSpeedRotate(float speed) {
		setRotateSpeed(speed);

	}

	//Refactor with setRotateSpeed?
	@Override
	public void setSpeedTravel(float speed) {
		setTravelSpeed(speed);
	}
	
	@Override//TODO: make pulic in superclass and STATIC?
	public float getMaximumSpeedRotate() {
		return MaxRotateSpeed;
	}
	@Override
	public float getMaximumSpeedTravel() {
		return MaxTravelSpeed;
	}
	
	
	
	//PRIVATE HELP METHODS:
	private void commitPreviousAction() {
		if(getCurrentAction() != null){
			switch(getCurrentAction()){
				case TRAVEL:
					setInitAbsoluteX(getX());
					setInitAbsoluteY(getY());
					break;
				case ROTATE:
					setInitAbsoluteHeading(getHeading());
					break;
				case SONAR:
					setInitSonarDirection(getSonarDirection());
					break;
				default:
					try {
						throw new Exception("This should not happen");
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}

	/**
	 * Clears the current action, argument and ETA.
	 */
	private void clearAction(){
		setCurrentAction(null);
		setCurrentArgument(0);
		setCurrentActionETA(Long.MIN_VALUE);
	}

	/**
	 * Initializes the fields that are universal to all moves. Sleeps the current thread if needed.
	 * @param wait
	 */
	private void initializeMove(boolean wait) {
		setWaiting(wait);
		setTimestamp(System.currentTimeMillis());
		calculateCurrentActionETA();
		if(wait == true) {
			try {
				Thread.sleep(getCurrentActionETA()-System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setWaiting(false);
		}
	}

	/**
	 * Calculates the ETA of the current action. 
	 * 
	 * If action is infinitely long (e.g. "moveForward()") the ETA is set to Long.MAX_VALUE.
	 */
	private void calculateCurrentActionETA(){
		long currentActionETA;
		double arg = Math.abs(getCurrentArgument());
		if(arg == Float.MAX_VALUE){
			currentActionETA = Long.MAX_VALUE;
		} else {
	 		currentActionETA = getTimestamp();
			switch (getCurrentAction()) {
			case TRAVEL:
				currentActionETA +=  (long) 1000*(arg/getTravelSpeed());
				break;
			case ROTATE:
				currentActionETA +=  (long) 1000*(arg/getRotateSpeed());
				break;
			case SONAR:
				currentActionETA +=  (long) 1000*(arg/getSonarRotateSpeed());
				break;
			default:
				throw new UnsupportedOperationException("This movement does not exist!");
			}
		}
		setCurrentActionETA(currentActionETA);	
	}
	
	private boolean isValid(Tile[] tl, Tile startT){
		boolean result = true;
			if(startT == null){
				result = false;
			} 
			if(tl == null){
				result = false;
			}
		return result && contains(tl, startT);
	}
	
	private boolean contains(Tile[] tl, Tile startT){
		for(int i=0; i<tl.length; i++){
			if(startT.equals(tl[i])){
				return true;
			}
		}
		return false;
	}
	
	
	private void setRandomInStartTile(Tile st) {
//		setRandomXIn(st);
//		setRandomYIn(st);
		//TODO: see for a solution for the heading.
		setInitAbsoluteHeading(0);
		//Sonar will always be at 0
		setInitSonarDirection(0);
	}

	/**
	 * Sets the x coordinate of the robot random in the first tile (where (0,0) is the middle of the tile).
	 * 
	 * Under assumption that there will be an Orientate() call setting it again to (0,0)
	 * @param st
	 */
	private void setRandomXIn(Tile st) {
		Random rand = new Random();
		int randomFrom10To30 = rand.nextInt(TILE_SIZE/2) +randomMaxOffset;
		int randomPosOrNeg = randomFrom10To30 - TILE_SIZE/2;
		setInitAbsoluteX((float)(randomPosOrNeg));		
	}

	/**
	 * Sets the y coordinate of the robot random in the first tile (where (0,0) is the middle of the tile).
	 * 
	 * Under assumption that there will be an Orientate() call setting it again to (0,0)
	 * @param st
	 */
	private void setRandomYIn(Tile st) {
		Random rand = new Random();
		int randomFrom10To30 = rand.nextInt(TILE_SIZE/2) +randomMaxOffset;
		int randomPosOrNeg = randomFrom10To30 - TILE_SIZE/2;
		setInitAbsoluteY((float)(randomPosOrNeg));		
	}

	/**
	 * Returns the value to add to the x-coordinate for traveling a given distance with the current heading.
	 * @param distance
	 * @return
	 */
	private double additionToX(float distance){
		double headingRadians = Math.toRadians(getHeading());
		return Math.sin(headingRadians)*distance;
	}
	
	/**
	 * Returns the value to add to the y-coordinate for traveling a given distance with the current heading.
	 * @param distance
	 * @return
	 */
	private double additionToY(float distance){
		double headingRadians = Math.toRadians(getHeading());
		return Math.cos(headingRadians)*distance;
	}
}