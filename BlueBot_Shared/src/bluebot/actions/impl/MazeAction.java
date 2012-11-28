package bluebot.actions.impl;


import bluebot.Driver;
import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;



/**
 * {@link Action} implementation for the maze exploration algorithm
 */
public class MazeAction extends WallFollower {
	
	/*
	 * // Ruben
	 * 
	 * Turned this into a sub-class of WallFollower
	 * until both classes are merged.
	 * 
	 * This eliminates the need for a member variable.
	 */
	
	@Override
	public void execute(Driver driver)
			throws ActionException, DriverException, InterruptedException {
		if (!driver.getCalibration().isCalibrated()) {
			throw new ActionException("Calibration of the light sensor is required to run the maze algorithm.");
		}
		super.execute(driver);
	}
	
}