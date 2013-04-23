package bluebot;

import static bluebot.core.ControllerFactory.getControllerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import lejos.pc.comm.NXTCommException;

import peno.htttp.SpectatorClient;
import bluebot.core.Controller;
import bluebot.core.DefaultController;
import bluebot.game.World;
import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.io.rabbitmq.RabbitMQ;
import bluebot.maze.MazeReader;
import bluebot.ui.SwingUtils;

import com.rabbitmq.client.Connection;

public class CommandLineAttempt {
	
	public static void main(String ... args0){
		
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
		System.out.println("setup done-waiting for command");
		String CurLine = ""; // Line read from standard in
		Controller c = connectToBrick(world);
		System.out.println("Enter a line of text (type 'quit' to exit): ");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);

		while (true){
			try {
				CurLine = in.readLine();
				if ((CurLine.equals("quit"))){
						break;			
				}
				if((CurLine.equals("forward"))){
					c.moveForward();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new WorldFrame(world).setVisible(true);
				
				final MainFrame frame = new MainFrame(world);
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
		});*/
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
	
	private static final Controller connectToBrick(World world) {
		final String name = JOptionPane.showInputDialog(
				"What is the name of the NXT brick?", "BlueBot");
		if ((name != null) && !name.isEmpty()) {
			return connectToBrick(name,world);
		}
		
		return null;
	}
	
	private static final Controller connectToBrick(final String name,World world) {
		
		if (world == null) {
			return null;
		}
		
		try {
			Controller c = getControllerFactory().connectToBrick(world, name);
			return c;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		} catch (final NXTCommException e) {
			e.printStackTrace();
			return null;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
