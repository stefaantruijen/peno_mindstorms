package bluebot.ui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.SwingUtilities;

import bluebot.game.World;
import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.maze.MazeReader;



/**
 * 
 * @author Ruben Feyen
 */
public class WorldFrame extends RenderingFrame {
	private static final long serialVersionUID = 1L;
	
	private WorldComponent viewer;
	
	
	public WorldFrame(final World world) {
		super("World");
		this.viewer = new WorldComponent(world);
		
		initComponents();
	}
	
	
	
	private final void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		viewer.setPreferredSize(new Dimension(800, 600));
		add(viewer, BorderLayout.CENTER);
		
		pack();
	}
	
	public static void main(final String... args) {
		try {
			final String path = new File("voorbeeldMaze_Steven.txt").getAbsolutePath();
			final Graph graph = new MazeReader().parseMaze(path);
			
			final List<Tile> tiles = graph.getVerticies();
			final Tile[] maze = new Tile[tiles.size()];
			tiles.toArray(maze);
			
			final World world = new World(maze);
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new WorldFrame(world).setVisible(true);
				}
			});
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	protected void startRendering() {
		viewer.startRendering();
	}
	
	protected void stopRendering() {
		viewer.stopRendering();
	}
	
}
