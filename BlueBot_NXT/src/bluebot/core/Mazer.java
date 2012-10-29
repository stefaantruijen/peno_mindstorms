package bluebot.core;

import lejos.nxt.Button;
import bluebot.MazeExplorer;

public class Mazer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PhysicalRobot rob = new PhysicalRobot();
		MazeExplorer me = new MazeExplorer(rob);
		System.out.println("Exploring the maze");
		me.run();
		Button.waitForAnyPress();
	}
}
