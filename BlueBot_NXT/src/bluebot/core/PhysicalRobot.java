package bluebot.core;


import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.IRSeekerV2.Mode;
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
	
	
	public static final long INTERVAL_ULTRA_SONIC = 50L;
	public static final float WHEEL_DIAMETER_LEFT  = 55.18F;
	public static final float WHEEL_DIAMETER_RIGHT = 54.82F;
	public static final float WHEEL_SPAN = 111.38F;
	
	private final Object lockUltraSonic = new Object();
	
	private RegulatedMotor head = Motor.B;
	private int lastUltraSonic;
	private long nextUltraSonic;
	private DifferentialPilot pilot;
	private LightSensor sensorLight;
	private UltrasonicSensor sensorUltraSonic;
	private TouchSensor touchSensor;
	private Tracker tracker;
	private IRSeekerV2 infraredSensor;
	
	
	public PhysicalRobot() {
		this(createPilot(), SensorPort.S1, SensorPort.S2, SensorPort.S3, SensorPort.S4);
	}
	public PhysicalRobot(final DifferentialPilot pilot, final SensorPort light, final SensorPort ultraSonic, final SensorPort touch, final SensorPort infra) {
		this.pilot = pilot;
		this.sensorLight = new LightSensor(light);
		this.sensorUltraSonic = createSensorUltraSonic(ultraSonic);
		this.tracker = new Tracker(pilot, head);
		this.touchSensor = new TouchSensor(touch);
		this.infraredSensor = new IRSeekerV2(infra, Mode.AC);
		
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
	
	protected float getSpeedTravel() {
		return (float)pilot.getTravelSpeed();
	}
	
	private final Tracker getTracker() {
		return tracker;
	}
	
	public boolean isMoving() {
		return getPilot().isMoving();
	}
	
	@Override
	public void modifyOrientation() {
		getTracker().modify();
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
	
	public void playSound() {
//		File file = new File("Bells9.wav");
//		Sound.playSample(file,100);
	}
	
	public int readSensorLight() {
		return sensorLight.getLightValue();
//		return sensorLight.getNormalizedLightValue();
	}
	
	@Override
	public int readSensorLightValue() {
		return sensorLight.getNormalizedLightValue();
	}
	
	public int readSensorUltraSonic() {
//		return sensorUltraSonic.getDistance();
		
		// Forced interval of 50 ms between requests
		// This should decrease the amount of incorrect values
		final long time = System.currentTimeMillis();
		synchronized (lockUltraSonic) {
			if (time >= nextUltraSonic) {
				lastUltraSonic = sensorUltraSonic.getDistance();
				nextUltraSonic = (time + INTERVAL_ULTRA_SONIC);
			}
		}
		return lastUltraSonic;
	}
	
	public void resetOrientation() {
		getTracker().reset();
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
	public float getArcLimit() {
		return 150;
	}
	
	@Override
	public void turnHeadClockWise(int offset) {
		this.head.rotate(-Math.abs(offset));
		
	}
	@Override
	public void turnHeadCounterClockWise(int offset) {
		this.head.rotate(Math.abs(offset));
	}
	
	
	
	
	
	
	
	
	
	
	public static final class Tracker extends OdometryPoseProvider {
		
		private RegulatedMotor head;
		
		
		public Tracker(final MoveProvider mp, final RegulatedMotor head) {
			super(mp);
			this.head = head;
		}
		
		
		
		public Orientation getOrientation() {
			final Pose pose = getPose();
			// TODO: Provide data about the heading of the US sensor
			return new Orientation(-pose.getY(), pose.getX(),
					(360F - Utils.clampAngleDegrees(pose.getHeading())),
					Utils.clampAngleDegrees(-head.getTachoCount()));
		}
		
		public void modify() {
			final Pose pose = getPose();
			pose.setLocation(
					round(pose.getX(), 400F), 
					round(pose.getY(), 400F)); //400F=Tile.SIZE
			pose.setHeading(round(pose.getHeading(), 90F));
			setPose(pose);
		}
		
		public void reset() {
			setPose(new Pose(0F, 0F, 0F));
			head.resetTachoCount();
		}
		
		private static final float round(final float value,
				final float accuracy) {
			return (accuracy * Math.round(value / accuracy));
		}
		
	}

	@Override
	public boolean isPressed() {
		return touchSensor.isPressed();
	}
	@Override
	public boolean seeInfrared() {
		int direction = this.getInfraredDirection();
		if(direction > 2 && direction <8){
			return true;
		}
		return false;
	}
	
	@Override
	public int getInfraredDirection() {
		return this.infraredSensor.getDirection();
	}
	@Override
	public void setStartLocation(int x, int y, float heading) {
		//nix doen
	}
	
}