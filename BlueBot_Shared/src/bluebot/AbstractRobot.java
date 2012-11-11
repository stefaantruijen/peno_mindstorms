package bluebot;

import bluebot.util.Orientation;
import bluebot.util.Utils;



/**
 * Skeletal implementation of the {@link Robot} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractRobot implements Robot {
	
	private int head;
	
	
	
	public float getHeading() {
		return getOrientation().getHeading();
	}
	
	/**
	 * Returns the maximum rotate speed of the robot
	 * 
	 * @return the maximum rotate speed (in degrees/s)
	 */
	protected float getMaximumSpeedRotate() {
		return 75F;
	}
	
	/**
	 * Returns the maximum travel speed of the robot
	 * 
	 * @return the maximum travel speed (in mm/s)
	 */
	protected float getMaximumSpeedTravel() {
		return 300F;
	}
	
	public float getX() {
		return getOrientation().getX();
	}
	
	public float getY() {
		return getOrientation().getY();
	}
	
	public int readSensorLight() {
		final Orientation pos = getOrientation();
		
		float x = pos.getX();
		float y = pos.getY();
		double z = Math.toRadians(pos.getHeading());
		
		x += (OFFSET_SENSOR_LIGHT * Math.sin(z));
		y += (OFFSET_SENSOR_LIGHT * Math.cos(z));
		
		return readSensorLight(x, y);
	}
	
	protected int readSensorLight(final float x, final float y) {
		throw new UnsupportedOperationException("Implementation required");
	}
	
	public int readSensorUltraSonic() {
		final Orientation pos = getOrientation();
		
		float x = pos.getX();
		float y = pos.getY();
		float z = Utils.clampAngleDegrees(pos.getHeading() + head);
		
		final double a = Math.toRadians(z);
		x += (OFFSET_SENSOR_ULTRASONIC * Math.sin(a));
		y += (OFFSET_SENSOR_ULTRASONIC * Math.cos(a));
		
		return readSensorUltraSonic(x, y, z);
	}
	
	protected int readSensorUltraSonic(final float x, final float y,
			final float heading) {
		throw new UnsupportedOperationException("Implementation required");
	}
	
	public void setSpeed(final int percentage) {
		if ((percentage < 0) || (percentage > 100)) {
			throw new IllegalArgumentException("Invalid percentage:  " + percentage);
		}
		
		final float factor = (percentage / 100F);
		setSpeedRotate(factor * getMaximumSpeedRotate());
		setSpeedTravel(factor * getMaximumSpeedTravel());
	}
	
	protected abstract void setSpeedRotate(float speed);
	
	protected abstract void setSpeedTravel(float speed);
	
	public void turnHeadClockWise(final int offset) {
		if (offset <= 0) {
			throw new IllegalArgumentException("Angle must be greater than zero");
		}
		
		head += offset;
		for (; head >= 360; head -= 360);
	}
	
	public void turnHeadCounterClockWise(final int offset) {
		if (offset <= 0) {
			throw new IllegalArgumentException("Angle must be greater than zero");
		}
		
		head -= offset;
		for (; head < 0; head += 360);
	}
	
}