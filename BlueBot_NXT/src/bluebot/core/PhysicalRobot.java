package bluebot.core;


import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

import bluebot.Robot;



/**
 * The {@link Robot} implementation for the NXT brick
 * 
 * @author Ruben Feyen
 */
public class PhysicalRobot implements Robot {
	
	private final static int DEFAULT_ACCELERATION	 = 500;
	private final static double DEFAULT_SPEED_ROTATE = 75;
	private final static double DEFAULT_SPEED_TRAVEL = 200;
	
	private final DifferentialPilot pilot;
	private LightSensor sensorLight;
	private UltrasonicSensor sensorUltraSonic;
	
	
	public PhysicalRobot() {
		this(SensorPort.S1, SensorPort.S2);
	}
	public PhysicalRobot(final SensorPort light, final SensorPort ultraSonic) {
		pilot = createPilot();
		sensorLight = new LightSensor(light);
		sensorUltraSonic = new UltrasonicSensor(ultraSonic);
	}
	
	
	
	protected DifferentialPilot createPilot() {
		final DifferentialPilot pilot = new DifferentialPilot(55.37F, 55F,167.78F, Motor.A, Motor.C, false);
		pilot.setAcceleration(DEFAULT_ACCELERATION);
		pilot.setRotateSpeed(DEFAULT_SPEED_ROTATE);
		pilot.setTravelSpeed(DEFAULT_SPEED_TRAVEL);
		return pilot;
	}
	
	private final DifferentialPilot getPilot() {
		return pilot;
	}
	
	public boolean isMoving() {
		return getPilot().isMoving();
	}
	
	public void moveBackward() {
		getPilot().backward();
	}
	
	public void moveBackward(final float distance, final boolean wait) {
		getPilot().travel(-Math.abs(distance), !wait);
	}
	
	public void moveForward() {
		getPilot().forward();
	}
	
	public void moveForward(final float distance, final boolean wait) {
		getPilot().travel(Math.abs(distance), !wait);
	}
	
	public int readSensorLight() {
		return sensorLight.readValue();
	}
	
	public int readSensorUltraSonic() {
		return sensorUltraSonic.getDistance();
	}
	
	public void setTravelSpeed(final double speed) {
		getPilot().setTravelSpeed(speed);
	}
	
	public void stop() {
//		getPilot().quickStop();
		getPilot().stop();
	}
	
	public void turnLeft() {
		getPilot().rotateLeft();
	}
	
	public void turnLeft(final float angle, final boolean wait) {
		getPilot().rotate(Math.abs(angle), !wait);
	}
	
	public void turnRight() {
		getPilot().rotateRight();
	}
	
	public void turnRight(final float angle, final boolean wait) {
		getPilot().rotate(-Math.abs(angle), !wait);
	}
	@Override
	public float getAngleIncrement() {
		return getPilot().getAngleIncrement();
	}
	
}