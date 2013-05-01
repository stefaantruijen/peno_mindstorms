package bluebot;


import bluebot.operations.OperationException;
import bluebot.operations.impl.BarcodeOperation;
import bluebot.operations.impl.CalibrationOperation;
import bluebot.operations.impl.PickUpOperation;
import bluebot.operations.impl.SeesawOperation;
import bluebot.operations.impl.WhiteLineOperation;
import bluebot.sensors.Calibration;
import bluebot.sensors.CalibrationException;
import bluebot.util.Orientation;



/**
 * Represents a local {@link Operator}
 * 
 * @author Ruben Feyen
 */
public class LocalOperator extends AbstractOperator {
	
	private Calibration calibration = new Calibration();
	private Robot robot;
	
	
	public LocalOperator(final Robot robot) {
		this.robot = robot;
	}
	
	
	
	public void doCalibrate() throws InterruptedException, OperationException {
		try {
			new CalibrationOperation().execute(this);
		} catch (final CalibrationException e) {
			throw new Error(e);
		}
	}
	
	public void doPickUp()
			throws CalibrationException, InterruptedException, OperationException {
		new PickUpOperation().execute(this);
	}
	
	public void doSeesaw()
			throws CalibrationException, InterruptedException, OperationException {
		new SeesawOperation().execute(this);
	}
	
	public void doWhiteLine()
			throws CalibrationException, InterruptedException, OperationException {
		new WhiteLineOperation().execute(this);
	}
	
	public float getAngleIncrement() {
		return getRobot().getAngleIncrement();
	}
	
	public float getArcLimit() {
		return getRobot().getArcLimit();
	}
	
	public Calibration getCalibration() {
		return calibration;
	}
	
	public Orientation getOrientation() {
		return getRobot().getOrientation();
	}
	
	public String getPlayerType() {
		return "VIRTUAL";
	}
	
	private final Robot getRobot() {
		return robot;
	}
	
	public int getSpeed() {
		return getRobot().getSpeed();
	}
	
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
	
	public int readSensorInfrared() {
		return getRobot().getInfraredDirection();
	}
	
	public int readSensorLight() {
		return getRobot().readSensorLightValue();
	}
	
	public boolean readSensorTouch() {
		return getRobot().isPressed();
	}
	
	public int readSensorUltrasonic() {
		return getRobot().readSensorUltraSonic();
	}
	
	public void resetOrientation() {
		getRobot().resetOrientation();
	}
	
	public int scanBarcode()
			throws CalibrationException, InterruptedException, OperationException {
		return new BarcodeOperation().execute(this).intValue();
	}
	
	public void setSpeed(final int percentage) {
		getRobot().setSpeed(percentage);
	}
	
	public void setStartLocation(final int x, final int y, final float angle) {
		getRobot().setStartLocation(x, y, angle);
	}
	
	public void stop() {
		getRobot().stop();
	}
	
	public void turnHeadClockWise(final int angle) {
		getRobot().turnHeadClockWise(angle);
	}
	
	public void turnHeadCounterClockWise(final int angle) {
		getRobot().turnHeadCounterClockWise(angle);
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
	
	public void turnRight(float angle, boolean wait) {
		getRobot().turnRight(angle, wait);
	}
	
}
