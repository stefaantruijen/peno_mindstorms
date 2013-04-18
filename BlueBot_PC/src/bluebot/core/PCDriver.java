package bluebot.core;

import bluebot.DriverException;
import bluebot.actions.ActionException;
import bluebot.actionsimpl.MazeActionV2;
import bluebot.maze.MazeListener;


public class PCDriver {
	
	private Controller controller;
	
	PCDriver(Controller controller){
		this.controller = controller;
	}
	
	/**
	 * Makes a mazeAction and runs it.
	 * 
	 * @param 	playerNumber
	 * 			The playerNumber for the mazeAction.
	 * @param 	itemNumber
	 * 			The itemNumber for the mazeAcction.
	 * @throws InterruptedException 
	 * @throws DriverException 
	 * @throws ActionException 
	 */
	public void doMaze(int playerNumber, int itemNumber, MazeListener mazeListener) throws ActionException, DriverException, InterruptedException{
		MazeActionV2 maze = new MazeActionV2(controller, playerNumber, itemNumber, mazeListener);
		maze.execute();
	}
}
