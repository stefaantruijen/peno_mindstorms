package bluebot.ui;


import static bluebot.core.ControllerFactory.getControllerFactory;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import lejos.pc.comm.NXTCommException;

import bluebot.core.Controller;
import bluebot.game.World;
import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.maze.MazeReader;



/**
 * 
 * @author Ruben Feyen
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_BRICK_NAME = "BlueBot";
	public static final String TITLE = "P&O BlueBot";
	
	private Tile[] maze;
	
	
	public MainFrame() {
		super(TITLE);
		initComponents();
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	
	
	private final void connectToBrick() {
		final String name = JOptionPane.showInputDialog("What is the name of the NXT brick?", DEFAULT_BRICK_NAME);
		if ((name != null) && !name.isEmpty()) {
			connectToBrick(name);
		}
	}
	
	private final void connectToBrick(final String name) {
		final Tile[] maze = getMaze();
		if (maze == null) {
			return;
		}
		if (maze.length == 0) {
			SwingUtils.showWarning("Invalid maze (no tiles)");
			return;
		}
		
		try {
			showController(getControllerFactory().connectToBrick(new World(maze), name));
		} catch (final IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Connection failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (final NXTCommException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Connection failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	private final void connectToSimulator() {
		final Tile[] maze = getMaze();
		if (maze == null) {
			return;
		}
		if (maze.length == 0) {
			this.maze = null;
			SwingUtils.showWarning("Invalid maze (no tiles)");
			return;
		}
		
		try {
			showController(getControllerFactory().connectToSimulator(new World(maze)));
		} catch (final IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Connection failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
//	@SuppressWarnings("unused")
//	private final void connectToTestDummy() {
//		final List<Tile> tiles = loadMaze();
//		if (tiles == null) {
//			return;
//		}
//		if (tiles.isEmpty()) {
//			SwingUtils.showWarning("Invalid maze (no tiles)");
//			return;
//		}
//		
//		final Maze maze = new Maze();
//		for (final Tile tile : tiles) {
//			maze.addTile(tile.getX(), tile.getY()).copyBorders(tile);
//		}
//		
//		showController(getControllerFactory().connectToTestDummy(maze));
//	}
	
	private static final JButton createButton(final String text) {
		final JButton button = new JButton(text);
		button.setMinimumSize(new Dimension(1, 64));
		return button;
	}
	
	protected World createWorld() {
		if (maze == null) {
			maze = loadMaze();
		}
		return ((maze == null) ? null : new World(maze));
	}
	
	private final Tile[] getMaze() {
		if (maze == null) {
			maze = loadMaze();
		}
		return maze;
	}
	
	private final void initComponents() {
		final JButton btnBrick = createButton("Connect to NXT brick");
		btnBrick.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				connectToBrick();
			}
		});
		
		final JButton btnSim = createButton("Connect to simulator");
		btnSim.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				connectToSimulator();
			}
		});
		
		/*
		final JButton btnTestDummy = createButton("Connect to Test Dummy");
		btnTestDummy.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				connectToTestDummy();
			}
		});
		*/
		
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1D };
		layout.rowHeights = new int[] { 64, 64, /* 64 */ };
		layout.rowWeights = new double[] { 1D, 1D, 1D };
		setLayout(layout);
		
		final GridBagConstraints gbc = SwingUtils.createGBC();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(btnBrick, gbc);
		
		gbc.gridy++;
		add(btnSim, gbc);
		
//		gbc.gridy++;
//		add(btnTestDummy, gbc);
	}
	
	private final Tile[] loadMaze() {
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
		
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
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
	
	private final void showController(final Controller controller) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JFrame frame =
//						new ControllerFrame(controller);
						new ControllerFrame(controller);
				frame.setVisible(true);
			}
		});
	}
	
}