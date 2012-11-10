package bluebot.simulator;


import bluebot.AbstractRobot;
import bluebot.Robot;
import bluebot.graph.Tile;
import bluebot.util.Orientation;
import bluebot.util.Utils;



/**
 * The {@link Robot} implementation for the simulator
 * 
 * @author Dieter Castel, Ruben Feyen, Michiel Ruelens 
 */
@SuppressWarnings("unused")
public class VirtualRobot extends AbstractRobot {
	/**
	 * 
	 */
	public static int TILE_SIZE = 40; //Probably get this value from other class.//TODO: see what this is irl
	/**
	 * Static that holds the standard travel speed in mm/s. This is the speed we measured in the real NXT robot.
	 */
	public static double STANDARD_TRAVEL_SPEED = 30; //Probably get this value from other class.//TODO: see what this is irl
	/**
	 * Static that holds the standard rotate speed in degrees/s. This is the speed we measured in the real NXT robot.
	 */
	public static double STANDARD_ROTATE_SPEED = 30; //Probably get this value from other class.//TODO: see what this is irl
	//TODO: STANDARD_SONAR_ROTATE_SPEED
	
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
	 * Variable hodling the rotate speed of the sonar.
	 */
	private double sonarDirection;
	
	
	/**
	 * Variable representing the absolute heading of this robot in degrees (0° being North) at the start of the movement. 
	 * 	initAbsoluteHeading is initialized to 0 at construct. (So it always starts pointing 'north').
	 */
	private float initAbsoluteHeading;

	/**
	 * Variable representing the absolutehorizontal coordinate at the start of the current move in the global coordinate system (Origin being the center of the first tile).
	 */
	private float initAbsoluteX;
	
	/**
	 * Variable representing the vertical coordinate at the start of the current move in the global coordinate system 
	 * Origin being the center of the first tile.
	 */
	private float initAbsoluteY;
	
	/**
	 * Variable representing the current action of the simulator.
	 */
	private Action currentAction;
	/**
	 * Variable holding the argument corresponding to the <code>currentAction</code>.
	 */
	private double currentArgument;
	/**
	 * Boolean declaring whether the current action is blocking.
	 */
	private boolean isWaiting;
	/**
	 * Variable holding the start time of the {@link currentAction} in milliseconds.
	 */
	private long timestamp;
	
	/**
	 * Variable holding the time in milliseconds at which the current action will be completed.
	 */
	private long currentActionETA;
	
	private Tile[] tilesList;
//	private VirtualLightSensor lightSensor;
//	private VirtualSonar sonar;
	private int imgStartX;
	private int imgStartY;
	private VirtualLightSensor lightSensor;
	private VirtualSonar sonar;
	private Sensors sensors;
	
	// TODO: Modify or remove this temporary default constructor
	public VirtualRobot() {
		this(new Tile[0], null);
	}
	/**
	 * Creates a new VirtualRobot object with given tiles and start tile
	 * 
	 * @param tilesList
	 *			List of Tile objects that will be used for generating sensor values. 
	 * @param startTile
	 * 			Tile at which the robot will start
	 */
	public VirtualRobot(Tile[] tilesList,Tile startTile){
		this.tilesList= tilesList;
		int startOffset = TILE_SIZE/2;
		//Assuming start in middle of the startTile.
		this.imgStartX = Math.round(startTile.getX()*TILE_SIZE + startOffset);
		this.imgStartY = Math.round(startTile.getY()*TILE_SIZE + startOffset);
		this.sensors = new Sensors(this.tilesList);
		lightSensor = sensors.getLightSensor();
		sonar = sensors.getSonar();
	}
	
	//Getters and setters of fields.
	private double getRotateSpeed() {
		return rotateSpeed;
	}
	
	/**
	 * Returns the heading (in degrees) of the Robot at the start of the current move.
	 * @return
	 */
	private float getInitAbsoluteHeading() {
		return initAbsoluteHeading;
	}

	private void setInitAbsoluteHeading(float absoluteHeading) {
		this.initAbsoluteHeading = absoluteHeading;
	}
	
	/**
	 * Returns the horizontal coordinate at the start of the current move in the global coordinate system (Origin being the center of the first tile).
	 * @return
	 */
	public float getInitAbsoluteX(){
		return initAbsoluteX;
	}
	
	@Override
	public float getX() {
		float currentHeading = getHeading();
		float absoluteX = getInitAbsoluteX();
		float result = absoluteX;
		
		if (getCurrentAction() == Action.TRAVEL){
			Long elapsedTime = System.currentTimeMillis() - getTimestamp();
			double arg = getCurrentArgument();
			int sign = (int)(arg/Math.abs(arg));
			float distance = (float) (elapsedTime*getTravelSpeed());
			double headingRadians = Math.toRadians(getInitAbsoluteHeading());
			result += sign*(((float)(Math.sin(headingRadians)))*distance);
		}
		return result;
	}
	
	public int getImgStartX(){
		return imgStartX;
	}
	
	public int getImgX(){
		return Math.round(getImgStartX() + getX()) ;
	}
	public int getImgStartY(){
		return imgStartY;
	}
	
	public int getImgY(){
		return Math.round(getImgStartY() + getY()) ;
	}
	private void setInitAbsoluteX(float x) {
		this.initAbsoluteX = x;
	}
	
	/**
	 *  Returns the vertical coordinate at the start of the current move in the global coordinate system (Origin being the center of the first tile).
	 * @return
	 */
	public float getInitAbsoluteY(){
		return initAbsoluteY;
	}
	
	/**
	 * 
	 */
	@Override
	public float getY() {
		float currentHeading = getHeading();
		float absoluteY = getInitAbsoluteY();
		float result = absoluteY;
		
		if (getCurrentAction() == Action.TRAVEL){
			Long elapsedTime = System.currentTimeMillis() - getTimestamp();
			double arg = getCurrentArgument();
			int sign = (int)(arg/Math.abs(arg));
			float distance = (float) (elapsedTime*getTravelSpeed());
			double headingRadians = Math.toRadians(getInitAbsoluteHeading());
			result += sign*(((float)(Math.cos(headingRadians)))*distance);
		}
		return result;
	}

	private void setInitAbsoluteY(float y) {
		this.initAbsoluteY = y;
	}

	private Action getCurrentAction() {
		return currentAction;
	}

	private void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}

	private double getCurrentArgument() {
		return currentArgument;
	}

	private void setCurrentArgument(double currentArgument) {
		this.currentArgument = currentArgument;
	}

	private boolean isWaiting() {
		return isWaiting;
	}

	private void setWaiting(boolean isWaiting) {
		this.isWaiting = isWaiting;
	}

	private long getTimestamp() {
		return timestamp;
	}

	private void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	private long getCurrentActionETA() {
		return currentActionETA;
	}

	private void setCurrentActionETA(long currentActionETA) {
		this.currentActionETA = currentActionETA;
	}

	private double getTravelSpeed() {
		return travelSpeed;
	}
	
	//Implementation of abstract methods
	
	/**
	 * Returns whether or not the simulator robot is still moving.
	 */
	public boolean isMoving() {
		return System.currentTimeMillis()<getCurrentActionETA();
	}
	
	public void moveBackward() {
		moveBackward(Float.MAX_VALUE, false);
	}
	
	public void moveBackward(final float distance, final boolean wait) {
		setCurrentAction(Action.TRAVEL);
		setCurrentArgument(-distance);
		initializeMove(wait);	
	}
	
	public void moveForward() {
		moveForward(Float.MAX_VALUE, false);
	}
	
	public void moveForward(final float distance, final boolean wait) {
		setCurrentAction(Action.TRAVEL);
		setCurrentArgument(distance);
		initializeMove(wait);	
	}

	/**
	 * Initializes the fields that are universal to all moves. Sleeps the current thread if needed.
	 * @param wait
	 */
	private void initializeMove(final boolean wait) {
		setWaiting(wait);
		setTimestamp(System.currentTimeMillis());
		calculateCurrentActionETA();
		if(wait == true) {
			try {
				Thread.sleep(System.currentTimeMillis()-getCurrentActionETA());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setWaiting(false);
		}
	}
	
	public int readSensorLight() {
		return lightSensor.getLightValue(getImgX(), getImgY());
	}
	
	public int readSensorUltraSonic() {
		float sonarHeading = Utils.clampAngleDegrees(getHeading()+getSonarDirection());
		return sonar.getSonarValue(getImgX(), getImgY(),sonarHeading);
	}
	
	//TODO check absolute vs relative...
	public void resetOrientation() {
		setInitAbsoluteX(0);
		setInitAbsoluteY(0);
		setInitAbsoluteHeading(0);
	}
	
	public void setTravelSpeed(double speed) {
		this.travelSpeed = speed;
	}
	
	//TODO:@Override
	public void setRotateSpeed(double speed) {
		this.rotateSpeed = speed;
	}
	
	protected void setSpeedRotate(final float speed) {
		this.rotateSpeed = speed;
	}
	
	protected void setSpeedTravel(final float speed) {
		this.travelSpeed = speed;
	}
	
	@Override
	public void stop() {
		// TODO
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void turnLeft() {
		turnLeft(Float.MAX_VALUE,false);
	}
	
	public void turnLeft(final float angle, final boolean wait) {
		setCurrentAction(Action.ROTATE);
		setCurrentArgument(angle);
		initializeMove(wait);		
	}
	
	public void turnRight() {
		turnLeft(Float.MAX_VALUE,false);
	}
	
	public void turnRight(final float angle, final boolean wait) {
		setCurrentAction(Action.ROTATE);
		setCurrentArgument(-angle);
		initializeMove(wait);
	}

	@Override
	public float getAngleIncrement() {
		//TODO: dependant on how real NXT side works Math.abs(...).
		return  getHeading() - getInitAbsoluteHeading();
	}
	
	/**
	 *TODO: check this with teammates
	 * Is does 0° means North???? 
	 */
	@Override
	public float getHeading(){
		float result = getInitAbsoluteHeading();
		if(getCurrentAction() == Action.ROTATE){
			Long elapsedTime = System.currentTimeMillis() - getTimestamp();
			double arg = getCurrentArgument();
			result += (arg/Math.abs(arg)) * elapsedTime*getRotateSpeed();
		}
		return Utils.clampAngleDegrees(result);
	}
	
//	//TODO:@Override
//	public getX(){
//		
//	}
//	
//	//TODO:@Override
//	public getY(){
//		
//	}
	
	public Orientation getOrientation() {
		return new Orientation(getX(), getY(), getHeading());
	}

	//TODO:@Override
	public float[] getPosition(){
		throw new UnsupportedOperationException();
	}
	
//TODO:
/*
 * Idee: 
 * Voor Licht sensor:
 * 
	 * Gebruik afbeelding. (Standaard een wit kruis en de borders) 
	 * Voor barcode genereer op de afbeelding die barcode.
	 * Voor elke x en y coordinaat (voor het gegeven doolhof) return de value van die afbeelding.
	 * 
	 * OF
	 * 
	 * Bereken elke waarde aangezien de plaatsing van het witte kruis (en de randen) altijd bekend is.
	 * 
 * 
 * Voor Sonar:
 * 
 	* Bereken de afstand tot de eerste volgende muur in de huidige heading.
 * 
 * 
 * 
 * 
 * 
 * 
 */
	/**
	 * Calculates the ETA of the current action. If action is infinitely long (e.g. "moveForward()") the ETA is set to Long.MAX_VALUE.
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
				currentActionETA +=  (long) (arg/getTravelSpeed());
				break;
			case ROTATE:
				currentActionETA +=  (long) (arg/getRotateSpeed());
				break;
			case SONAR:
				currentActionETA +=  (long) (arg/getSonarRotateSpeed());
				break;
			default:
				throw new UnsupportedOperationException("This movement does not exist!");
			}
		}
		setCurrentActionETA(currentActionETA);	
	}
	
	/**
	 * Gets the rotate speed of the sonar
	 * @return
	 */
	public double getSonarRotateSpeed() {
		return sonarRotateSpeed;
	}
	
	/**
	 * Sets the rotate speed of the sonar
	 * @return
	 */
	public void setSonarRotateSpeed(double speed) {
		 sonarRotateSpeed = speed;
	}
	
	//TODO decide true or false for waiting?
	/**
	 * Turns the head of the robot (the sonar) clock wise (right).
	 * Same convention Right (CWise) results in a subtraction.
	 */
	@Override
	public void turnHeadCWise(int offset) {
		setCurrentAction(Action.SONAR);
		setCurrentArgument(-offset);
		initializeMove(false);	
	}

	//TODO decide true or false for waiting?
	/**
	 * Turns the head of the robot (the sonar) counter clock wise (left).
	 * Same convention Left (CCWise) results in a addition.
	*/
	@Override
	public void turnHeadCCWise(int offset) {
		setCurrentAction(Action.SONAR);
		setCurrentArgument(offset);
		initializeMove(false);	
	}
	
	/**
	 * This is the direction of the sonar head with respect to the heading of the robot.
	 * 
	 * Can either be negative or positive.
	 * @return
	 */
	public float getSonarDirection(){
		float result = getInitialSonarDirection();
		if(getCurrentAction() == Action.SONAR){
			Long elapsedTime = System.currentTimeMillis() - getTimestamp();
			double arg = getCurrentArgument();
			result += (arg/Math.abs(arg)) * elapsedTime* getSonarRotateSpeed();
		}
		return result;		
	}
	private float getInitialSonarDirection() {
		return (float) sonarDirection;
	}
	
}