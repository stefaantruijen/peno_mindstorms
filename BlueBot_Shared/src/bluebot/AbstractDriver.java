package bluebot;


import java.io.File;

import bluebot.graph.Tile;
import bluebot.io.Connection;
import bluebot.io.ServerTranslator;
import bluebot.sensors.Brightness;
import bluebot.sensors.Calibration;
import bluebot.sensors.CalibrationException;
import bluebot.sensors.SensorType;
import bluebot.util.Orientation;



/**
 * Skeletal implementation of the {@link Driver} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractDriver implements Driver {
	
	private static final int UPDATES_PER_SECOND = 25;
	
	private Calibration calibration = new Calibration();
	private Robot robot;
	private ServerTranslator translator;
	private Thread updater;
	
	
	public AbstractDriver(final Robot robot, final Connection connection) {
		this(robot, new ServerTranslator(connection));
	}
	private AbstractDriver(final Robot robot, final ServerTranslator translator) {
		setRobot(robot);
		setTranslator(translator);
		
		startUpdater();
	}
	
	
	
	public void dispose() {
		stopUpdater();
	}
	
	// TODO: Remove and use getHeading() instead
	public float getAngleIncrement(){
		return getRobot().getAngleIncrement();
	}
	
	public Calibration getCalibration() {
		return calibration;
	}
	
	public Orientation getOrientation() {
		return getRobot().getOrientation();
	}
	
	/**
	 * Returns the robot
	 * 
	 * @return a {@link Robot} object
	 */
	private final Robot getRobot() {
		return robot;
	}
	
	public int getSpeed() {
		return getRobot().getSpeed();
	}
	
	private final ServerTranslator getTranslator() {
		return translator;
	}
	
	/**
	 * Determines whether or not the robot is moving
	 * 
	 * @return <code>TRUE</code> if the robot is moving,
	 * 				<code>FALSE</code> otherwise
	 * 
	 * @see {@link Robot#isMoving()}
	 */
	public boolean isMoving() {
		return getRobot().isMoving();
	}
	
	public void modifyOrientation() {
		getRobot().modifyOrientation();
	}
	
	public void moveBackward() {
		getRobot().moveBackward();
	}
	
	public void moveBackward(final float distance, final boolean wait) {
		getRobot().moveBackward(distance, wait);
	}
	
	public void moveForward() {
		getRobot().moveForward();
	}
	
	public void moveForward(final float distance, final boolean wait) {
		getRobot().moveForward(distance, wait);
	}
	
	/**
	 * This method is called whenever a sensor value is requested
	 * 
	 * @param type - the {@link SensorType} of the request
	 */
	public void onSensorRequest(final SensorType type) {
		switch (type) {
			case LIGHT:
				sendSensorLight(readSensorLight());
				break;
			case ULTRA_SONIC:
				break;
			default:
				sendError("Invalid sensor type:  " + type);
				break;
		}
	}
	
	public int readSensorLight() {
		return getRobot().readSensorLight();
	}
	
	public Brightness readSensorLightBrightness() throws CalibrationException {
		final Calibration calibration = getCalibration();
		if (!calibration.isCalibrated()) {
			throw new CalibrationException("The light sensor has not been calibrated");
		}
		
		final int value = readSensorLightValue();
		if (value >= calibration.getLightThresholdWhite()) {
			return Brightness.WHITE;
		} else if (value > calibration.getLightThresholdBlack()) {
			return Brightness.GRAY;
		} else {
			return Brightness.BLACK;
		}
	}
	
	public int readSensorLightValue() {
		return getRobot().readSensorLightValue();
	}
	
	public int readSensorUltraSonic() {
		return getRobot().readSensorUltraSonic();
	}
	
	public void resetOrientation() {
		getRobot().resetOrientation();
	}
	
	public void sendDebug(final String msg) {
		getTranslator().sendDebug(msg);
	}
	
	public void sendError(final String msg) {
		getTranslator().sendError(msg);
	}
	
	/**
	 * Sends a message (with title "Info")
	 * 
	 * @param msg - the message
	 */
	protected void sendMessage(final String msg) {
		sendMessage(msg, "Info");
	}
	
	public void sendMessage(final String msg, final String title) {
		getTranslator().sendMessage(msg, title);
	}
	
	/**
	 * Sends a motion update
	 * 
	 * @param x - the position on the X axis
	 * @param y - the position on the Y axis
	 * @param body - the heading of the body (in degrees), zero equals north
	 * @param head - the heading of the head (in degrees), relative to the body
	 */
	protected void sendMotion(final float x, final float y,
			final float body, final float head) {
		getTranslator().sendMotion(x, y, body, head);
	}
	
	private final void sendSensor(final int value, final SensorType type) {
		getTranslator().sendSensorValue(type, value);
	}
	
	/**
	 * Sends the current value of the light sensor
	 */
	protected void sendSensorLight() {
		sendSensorLight(readSensorLight());
	}
	
	/**
	 * Sends a value from the light sensor
	 * 
	 * @param value - a sensor value
	 */
	protected void sendSensorLight(final int value) {
		sendSensor(value, SensorType.LIGHT);
	}
	
	/**
	 * Sends a value from the ultrasonic sensor
	 * 
	 * @param value - a sensor value
	 */
	protected void sendSensorUltraSonic(final int value) {
		sendSensor(value, SensorType.ULTRA_SONIC);
	}
	
	public void sendTile(final Tile tile) {
		getTranslator().sendTile(tile);
	}
	
	private final void setRobot(final Robot robot) {
		if (robot == null) {
			throw new NullPointerException();
		}
		this.robot = robot;
	}
	
	public void setSpeed(final int percentage) {
		getRobot().setSpeed(percentage);
		getTranslator().notifySpeed(percentage);
	}
	
	protected void setSpeedHigh() {
		setSpeed(100);
	}
	
	protected void setSpeedLow() {
		setSpeed(12);
	}
	
	protected void setSpeedMedium() {
		setSpeed(50);
	}
	
	private final void setTranslator(final ServerTranslator translator) {
		if (translator == null) {
			throw new NullPointerException();
		}
		this.translator = translator;
	}
	
	protected synchronized void startUpdater() {
		if (updater == null) {
			updater = new Thread(new Updater());
			updater.setDaemon(true);
			updater.start();
		}
	}
	
	public void stop() {
		getRobot().stop();
	}
	
	protected synchronized void stopUpdater() {
		if (updater != null) {
			updater.interrupt();
			updater = null;
		}
	}
	
	public void turnHeadClockWise(final int offset) {
		getRobot().turnHeadClockWise(offset);
	}
	
	public void turnHeadCounterClockWise(final int offset) {
		getRobot().turnHeadCounterClockWise(offset);
	}
	
	public void turnLeft() {
		getRobot().turnLeft();
	}
	
	public void turnLeft(final float angle, final boolean wait) {
		getRobot().turnLeft(angle, wait);
	}
	
	public void turnRight() {
		getRobot().turnRight();
	}
	
	public void turnRight(final float angle, final boolean wait) {
		getRobot().turnRight(angle, wait);
	}
	
	
	
	
	
	
	
	
	
	
	private final class Updater implements Runnable {
		
		private float body, head, x, y;
		
		
		
		public void run() {
			final long interval = Math.max(1L, (1000L / UPDATES_PER_SECOND));
			for (long time;;) {
				time = System.currentTimeMillis();
				tick();
				time += (interval - System.currentTimeMillis());
				if (time > 0l) {
					try {
						Thread.sleep(time);
					} catch (final InterruptedException e) {
						return;
					}
				}
			}
		}
		
		private final void tick() {
			final Orientation o = getOrientation();
			if ((o.getX() != x) || (o.getY() != y)
					|| (o.getHeadingBody() != body)
					|| (o.getHeadingHead() != head)) {
				sendMotion(
						(x = o.getX()),
						(y = o.getY()),
						(body = o.getHeadingBody()),
						(head = o.getHeadingHead()));
			}
			
			try {
				sendSensorLight(readSensorLight());
				sendSensorUltraSonic(readSensorUltraSonic());
			} catch (final RuntimeException e) {
				// The robot is not in a happy place right now
			}
		}
		
	}
	/**
	 * plays the given file (.wav)
	 */
	public void playSound(File file){
		getRobot().playSound(file);
	}
	
}
