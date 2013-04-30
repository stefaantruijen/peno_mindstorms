package bluebot;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import peno.htttp.SpectatorClient;

import com.jtattoo.plaf.luna.LunaLookAndFeel;
import com.rabbitmq.client.Connection;

import bluebot.game.World;
import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.io.rabbitmq.RabbitMQ;
import bluebot.maze.MazeReader;
import bluebot.ui.MainFrame;
import bluebot.ui.RepeatingKeyReleasedEventsFix;
import bluebot.ui.SwingUtils;
import bluebot.ui.WorldFrame;



/**
 * This class represents the entry point of the client-side application
 * 
 * @author Ruben Feyen
 */
public class Launcher {
	
	private static final void init() {
		RepeatingKeyReleasedEventsFix.install();
		setLookAndFeel();
	}
	
	private static final Tile[] loadMaze() {
		final JFileChooser fc = new JFileChooser(new File("."));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		for (final FileFilter filter : fc.getChoosableFileFilters()) {
			fc.removeChoosableFileFilter(filter);
		}
		fc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(final File file) {
				return (file.isDirectory() || file.getName().endsWith(".txt"));
			}
			
			public String getDescription() {
				return "Maze files (.txt)";
			}
		});
		
		fc.setApproveButtonText("Load");
		fc.setDialogTitle("Load a maze file");
		
		if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		
		final File file = fc.getSelectedFile();
		
		final Graph graph;
		try {
			graph = new MazeReader().parseMaze(file.getAbsolutePath());
		} catch (final Throwable e) {
			return null;
		}
		
		if (graph == null) {
			return null;
		}
		
		final List<Tile> maze = graph.getVerticies();
		final Tile[] tiles = new Tile[maze.size()];
		maze.toArray(tiles);
		return tiles;
	}
	
	public static void main(final String... args) {
		init();
		
		final Tile[] maze = loadMaze();
		if (maze == null) {
			return;
		}
		
		final Connection rabbit;
		try {
			rabbit = RabbitMQ.connect();
		} catch (final IOException e) {
			e.printStackTrace();
			SwingUtils.showError(e.getMessage());
			return;
		}
		
		final String gameId =
				JOptionPane.showInputDialog("Please enter the game ID:", "bluebot");
		if ((gameId == null) || gameId.isEmpty()) {
			return;
		}
		
		final World world = new World(maze);
		
		final SpectatorClient spectator;
		try {
			spectator = new SpectatorClient(rabbit, world.createSpectatorHandler(), gameId);
			spectator.start();
		} catch (final IOException e) {
			e.printStackTrace();
			SwingUtils.showError(e.getMessage());
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new WorldFrame(world).setVisible(true);
				
				final MainFrame frame = new MainFrame(world, gameId);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(final WindowEvent event) {
						try {
							spectator.stop();
						} catch (final NullPointerException e) {
//							e.printStackTrace();
						}
					}
				});
				frame.setVisible(true);
			}
		});
	}
	
	private static final void setLookAndFeel() {
		final LookAndFeel laf;
		try {
			laf =
//				new AcrylLookAndFeel();
//				new AeroLookAndFeel();
//				new FastLookAndFeel();
//				new HiFiLookAndFeel();
				new LunaLookAndFeel();
//				new NoireLookAndFeel();
//				new TextureLookAndFeel();
		} catch (final Throwable e) {
			// L&F not available, perhaps the JTattoo library is missing
			e.printStackTrace();
			return;
		}
		
		try {
			UIManager.setLookAndFeel(laf);
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
}