package bluebot;



/**
 * Skeletal implementation of the {@link Robot} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractRobot implements Robot {
	
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
	
}