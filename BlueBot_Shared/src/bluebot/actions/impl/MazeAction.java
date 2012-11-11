package bluebot.actions.impl;

import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.actions.ActionException;






/**
 * {@link Action} implementation for the maze exploration algorithm
 */
public class MazeAction extends Action {

	@Override
	public void execute(Driver driver) throws ActionException,
			InterruptedException {
		
		WallFollower wf = new WallFollower();
		wf.execute(driver);
	}
	
}