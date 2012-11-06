package bluebot.core;


import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.RegulatedMotor;
import bluebot.AbstractRobot;
import bluebot.Robot;
import bluebot.util.Orientation;



/**
 * The {@link Robot} implementation for the NXT brick
 * 
 * @author Ruben Feyen
 */
public class PhysicalRobot extends AbstractRobot {
	
	public static final float WHEEL_DIAMETER_LEFT  = 55.37F;
	public static final float WHEEL_DIAMETER_RIGHT = 55.00F;
	public static final float WHEEL_SPAN = 169.9F;
	
	private RegulatedMotor head = Motor.B;
	private DifferentialPilot pilot;
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
	
	
	
	public static final DifferentialPilot createPilot() {
		final DifferentialPilot pilot = new DifferentialPilot(
				WHEEL_DIAMETER_LEFT,
				WHEEL_DIAMETER_RIGHT,
				WHEEL_SPAN,
				Motor.A,
				Motor.C,
				false);
		pilot.setAcceleration(Robot.DEFAULT_ACCELERATION);
		pilot.setRotateSpeed(Robot.DEFAULT_SPEED_ROTATE);
		pilot.setTravelSpeed(Robot.DEFAULT_SPEED_TRAVEL);
		return pilot;
	}
	
	public Orientation getOrientation() {
		// TODO:	Proper implementation instead of dummy value
		return new Orientation(0F, 0F, 0F);
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
	
	protected void setSpeedRotate(final float speed) {
		getPilot().setRotateSpeed(speed);
	}
	
	protected void setSpeedTravel(final float speed) {
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
	
	@Override
	public void turnHeadCWise(int offset) {
		this.head.rotate(-Math.abs(offset));
		
	}
	@Override
	public void turnHeadCCWise(int offset) {
		this.head.rotate(Math.abs(offset));
		
	}
}