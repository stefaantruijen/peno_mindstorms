package bluebot;



/**
 * Skeletal implementation of the {@link Robot} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractRobot implements Robot {
	
	protected float getMaximumSpeedRotate() {
		return 75F;
	}
	
	protected float getMaximumSpeedTravel() {
		return 150F;
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