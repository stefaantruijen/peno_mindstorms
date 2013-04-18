package bluebot.core;

import bluebot.actions.impl.MazeAction;
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
	 */
	public void doMaze(int playerNumber, int itemNumber, MazeListener mazeListener){
		MazeAction maze = new MazeAction(controller, playerNumber, itemNumber, mazeListener);
		maze.execute();
	}
}
