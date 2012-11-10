package bluebot.core;


import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.RegulatedMotor;
import bluebot.AbstractRobot;
import bluebot.Robot;
import bluebot.util.Orientation;
import bluebot.util.Utils;



/**
 * The {@link Robot} implementation for the NXT brick
 * 
 * @author Ruben Feyen
 */
public class PhysicalRobot extends AbstractRobot {
	
	public static final float WHEEL_DIAMETER_LEFT  = 55.37F;
	public static final float WHEEL_DIAMETER_RIGHT = 55.00F;
	public static final float WHEEL_SPAN = 169.9F;
	
	private final Object lockUltraSonic = new Object();
	
	private RegulatedMotor head = Motor.B;
	private int lastUltraSonic;
	private long nextUltraSonic;
	private DifferentialPilot pilot;
	private LightSensor sensorLight;
	private UltrasonicSensor sensorUltraSonic;
	private Tracker tracker;
	
	
	public PhysicalRobot() {
		this(createPilot(), SensorPort.S1, SensorPort.S2);
	}
	public PhysicalRobot(final DifferentialPilot pilot, final SensorPort light, final SensorPort ultraSonic) {
		this.pilot = pilot;
		this.sensorLight = new LightSensor(light);
		this.sensorUltraSonic = createSensorUltraSonic(ultraSonic);
		this.tracker = new Tracker(pilot);
		
		resetOrientation();
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
	
	protected UltrasonicSensor createSensorUltraSonic(final SensorPort port) {
		final UltrasonicSensor sensor = new UltrasonicSensor(port);
//		sensor.setMode(UltrasonicSensor.MODE_PING);
//		sensor.setContinuousInterval(1);
		return sensor;
	}
	
	public Orientation getOrientation() {
		return getTracker().getOrientation();
	}
	
	private final DifferentialPilot getPilot() {
		return pilot;
	}
	
	// TODO: Make private after debugging
	public final Tracker getTracker() {
		return tracker;
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
		return sensorLight.getLightValue();
//		return sensorLight.getNormalizedLightValue();
	}
	
	public int readSensorUltraSonic() {
//		return sensorUltraSonic.getDistance();
		
		// Forced interval of 50 ms between requests
		// This should decrease the amount of incorrect values
		final long time = System.currentTimeMillis();
		synchronized (lockUltraSonic) {
			if (time >= nextUltraSonic) {
				lastUltraSonic = sensorUltraSonic.getDistance();
				nextUltraSonic = (time + 50L);
			}
		}
		return lastUltraSonic;
	}
	
	public void resetOrientation() {
		getTracker().setPose(new Pose(0F, 0F, 0F));
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
	
	
	
	
	
	
	
	
	
	
	public static final class Tracker extends OdometryPoseProvider {
		
		public Tracker(final MoveProvider mp) {
			super(mp);
		}
		
		
		
		public Orientation getOrientation() {
			final Pose pose = getPose();
			
//			float z = pose.getHeading();
//			z = Utils.clampAngleDegrees(z);
//			z = (360F - z);
			
			return new Orientation(-pose.getY(), pose.getX(),
					(360F - Utils.clampAngleDegrees(pose.getHeading())));
		}
		
	}
	
}