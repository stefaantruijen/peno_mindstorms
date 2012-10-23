package bluebot;


import bluebot.maze.MazeGenerator;
import lejos.nxt.Button;



/**
 * 
 * @author Ruben Feyen
 */
public class MazeTimer {
	
	public static void main(final String... args) throws Exception {
		final MazeGenerator generator = new MazeGenerator();
		do {
			System.out.println(generator.time());
		} while (Button.waitForAnyPress() != Button.ID_ESCAPE);
	}
	
}