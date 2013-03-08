package bluebot.actions;


import bluebot.Driver;
import bluebot.DriverException;
import bluebot.actions.impl.PickUpAction;
import bluebot.actions.impl.WhiteLineAction;
import bluebot.sensors.Brightness;
import bluebot.sensors.CalibrationException;



/**
 * Represents an action performed on the {@link Driver}
 * 
 * @author Ruben Feyen
 */
public abstract class Action {
	
	private boolean aborted;
	private Driver driver;
	
	
	
	/**
	 * Aborts the execution of this action
	 */
	public void abort() {
		this.aborted = true;
	}
	
	protected void checkAborted() throws InterruptedException {
		if (isAborted()) {
			throw new InterruptedException("Action aborted");
		}
	}
	
	protected abstract void execute()
			throws ActionException, DriverException, InterruptedException;
	
	/**
	 * Executes this action on a {@link Driver}
	 * 
	 * @param driver - the {@link Driver} to be executed on
	 * 
	 * @throws ActionException if an error occurs during execution
	 * @throws DriverException if an error occurs in the {@link Driver} API
	 * @throws InterruptedException if interrupted
	 */
	public final void execute(final Driver driver)
			throws ActionException, DriverException, InterruptedException {
		this.driver = driver;
		
		final int speed = driver.getSpeed();
		try {
			execute();
		} finally {
			driver.setSpeed(speed);
		}
	}
	
	/**
	 * Executes the white-line algorithm
	 * 
	 * @throws ActionException if an error occurs during execution
	 * @throws DriverException
	 * @throws InterruptedException if interrupted
	 */
	protected void executeWhiteLine()
			throws ActionException, DriverException, InterruptedException {
		new WhiteLineAction().execute(getDriver());
	}
	
	/**
	 * Executes the PickUp algorithm
	 * After the PickUp algorithm, the robot is turned around and ends on the place where
	 * he started.
	 */
	protected void executePickUp()
			throws ActionException, DriverException, InterruptedException {
		new PickUpAction().execute(getDriver());
	}
	
	/**
	 * DO NOT use this method in the constructor of an {@link Action} subclass
	 * 
	 * @return the {@link Driver} instance
	 */
	protected Driver getDriver() {
		return driver;
	}
	
	/**
	 * Determines whether or not this action has been aborted
	 * 
	 * @return <code>TRUE</code> if aborted, <code>FALSE</code> otherwise
	 */
	public boolean isAborted() {
		return aborted;
	}
	
	protected void waitForLightSensor(final Brightness color, final boolean detect)
			throws CalibrationException, InterruptedException {
		final Driver driver = getDriver();
		if (detect) {
			while (driver.isMoving()
					&& (driver.readSensorLightBrightness() != color)) {
				checkAborted();
			}
		} else {
			while (driver.isMoving()
					&& (driver.readSensorLightBrightness() == color)) {
				checkAborted();
			}
		}
	}
	
	/**
	 * Waits for the driver to perform a motion
	 * 
	 * @throws InterruptedException if interrupted while waiting
	 * 
	 * @see {@link #waitForMoving(boolean)}
	 */
	protected void waitForMotion() throws InterruptedException {
		waitForMoving(true);
		waitForMoving(false);
	}
	
	/**
	 * Waits for the driver to either start or stop moving
	 * 
	 * @param moving - the condition to wait for ({@link Driver#isMoving()} == <b>moving</b>)
	 * 
	 * @throws InterruptedException if interrupted while waiting
	 * 
	 * @see {@link #waitForMotion()}
	 */
	protected void waitForMoving(final boolean moving)
			throws InterruptedException {
		while (!isAborted() && (getDriver().isMoving() != moving)) {
			Thread.sleep(10);
		}
	}
	
}