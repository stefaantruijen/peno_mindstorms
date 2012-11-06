package bluebot.actions;


import bluebot.Driver;
import bluebot.actions.impl.WhiteLineAction;



/**
 * Represents an action performed on the {@link Driver}
 * 
 * @author Ruben Feyen
 */
public abstract class Action {
	
	private boolean aborted;
	
	
	
	/**
	 * Aborts the execution of this action
	 */
	public void abort() {
		this.aborted = true;
	}
	
	/**
	 * Executes this action on a {@link Driver}
	 * 
	 * @param driver - the {@link Driver} to be executed on
	 * 
	 * @throws InterruptedException if interrupted
	 */
	public abstract void execute(Driver driver) throws InterruptedException;
	
	/**
	 * Executes the white-line algorithm
	 * 
	 * @param driver - the {@link Driver} to be executed on
	 * 
	 * @throws InterruptedException if interrupted
	 */
	protected void executeWhiteLine(final Driver driver) throws InterruptedException {
		new WhiteLineAction().execute(driver);
	}
	
	/**
	 * Determines whether or not this action has been aborted
	 * 
	 * @return <code>TRUE</code> if aborted, <code>FALSE</code> otherwise
	 */
	public boolean isAborted() {
		return aborted;
	}
	
	/**
	 * Waits for the driver to perform a motion
	 * 
	 * @param driver - a {@link Driver} object
	 * 
	 * @throws InterruptedException if interrupted while waiting
	 * 
	 * @see {@link #waitForMoving(Driver, boolean)}
	 */
	protected void waitForMotion(final Driver driver) throws InterruptedException {
		waitForMoving(driver, true);
		waitForMoving(driver, false);
	}
	
	/**
	 * Waits for the driver to either start or stop moving
	 * 
	 * @param driver - a {@link Driver} object
	 * @param moving - the condition to wait for ({@link Driver#isMoving()} == <b>moving</b>)
	 * 
	 * @throws InterruptedException if interrupted while waiting
	 * 
	 * @see {@link #waitForMotion(Driver)}
	 */
	protected void waitForMoving(final Driver driver, final boolean moving)
			throws InterruptedException {
		while (!isAborted() && (driver.isMoving() != moving)) {
			Thread.sleep(10);
		}
	}
	
}