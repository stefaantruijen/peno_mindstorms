package bluebot.actions.impl;

import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.actions.ActionException;






/**
 * {@link Action} implementation for the maze exploration algorithm
 */
public class MazeAction extends Action {
	WallFollower wf;
	@Override
	public void execute(Driver driver) throws ActionException,
			InterruptedException {
		if (!driver.getCalibration().isCalibrated()) {
			throw new ActionException("Calibration of the light sensor is required to run the maze algorithm.");
		}
		
		wf = new WallFollower();
		wf.execute(driver);
	}
	
	@Override
	public void abort(){
		super.abort();
		wf.abort();
		
	}
	
}