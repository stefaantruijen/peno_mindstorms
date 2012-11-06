package bluebot.actions.impl;


import bluebot.Driver;
import bluebot.MazeExplorer;
import bluebot.actions.Action;



/**
 * {@link Action} implementation for the maze exploration algorithm
 */
public class MazeAction extends Action {
	
	public void execute(final Driver driver) throws InterruptedException {
		MazeExplorer me = new MazeExplorer(driver);
		me.run();
	}
	
}