package bluebot;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import bluebot.actionsimpl.MazeActionV2;
import bluebot.game.Game;
import bluebot.game.GameCallback;
import bluebot.game.World;
import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.maze.MazeReader;
import bluebot.operations.OperationException;
import bluebot.sensors.CalibrationException;
import bluebot.ui.DefaultVisualizationComponent;



/**
 * 
 * @author Ruben Feyen
 */
public class Debugger {
	
	private static final String	GAME_ID			= "bluegame";
	private static final String	MAZE_FILE		= "voorbeeldMaze_Steven.txt";
	private static final int	OBJECT_NUMBER	= 0;
	private static final String	PLAYER_ID		= "bluebot";
	private static final int	PLAYER_NUMBER	= 1;
	
	
	
	public static void main(final String... args) throws Exception {
		final File file = new File(MAZE_FILE);
		
		final Graph graph = new MazeReader().parseMaze(file.getAbsolutePath());
		final List<Tile> list = graph.getVerticies();
		final Tile[] array = new Tile[list.size()];
		list.toArray(array);
		
		final World world = new World(array);
		
		final Operator operator = OperatorFactory.connectToSimulator(world);
		
		final Game game = new Game(operator, GAME_ID, PLAYER_ID, new GameCallback() {
			public boolean prepareForGameStart(int playerNumber, int objectNumber) {
				
				return true;
			}
		});
		
		final Tile start = world.getStart(PLAYER_NUMBER);
		operator.setStartLocation(start.getX(), start.getY(), (int) start.getStartOrientation().getDouble());
		
		//	[EDIT]
		//	TODO:	Uncomment code & fill in blanks
		final MazeActionV2 maze =new MazeActionV2(PLAYER_NUMBER, OBJECT_NUMBER, game.createMazeCallback());
		final Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					maze.execute(operator);
				} catch (CalibrationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.setDaemon(true);
		//	[/EDIT]
		
		final Field field = Game.class.getDeclaredField("explorer");
		field.setAccessible(true);
		field.set(game, maze);
		field.setAccessible(false);
		
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				final DefaultVisualizationComponent canvas = new DefaultVisualizationComponent();
				canvas.setBackground(Color.WHITE);
				canvas.setGame(game);
				canvas.setPreferredSize(new Dimension(800, 600));
				
				final JFrame frame = new JFrame("Debugger");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.add(canvas);
				frame.pack();
				
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowOpened(final WindowEvent event) {
						canvas.startRendering();
						thread.start();
					}
				});
				frame.setVisible(true);
			}
		});
	}
	
}
