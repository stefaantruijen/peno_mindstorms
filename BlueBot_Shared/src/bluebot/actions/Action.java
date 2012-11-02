package bluebot.actions;


import bluebot.Driver;



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
	 * Determines whether or not this action has been aborted
	 * 
	 * @return <code>TRUE</code> if aborted, <code>FALSE</code> otherwise
	 */
	public boolean isAborted() {
		return aborted;
	}
	
}