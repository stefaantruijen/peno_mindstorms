package bluebot.ui;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import algorithms.PathFinder;



/**
 * 
 * @author Ruben Feyen
 */
public class MazeDialog extends AbstractDialog<Integer> {
	private static final long serialVersionUID = 1L;
	
	
	public MazeDialog(final Window owner) {
		super(owner, "Maze");
		setResult(-1);
	}
	
	
	
	protected void initComponents() {
		final JButton buttonAStar = new JButton("A*");
		buttonAStar.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				setResult(PathFinder.ASTAR);
				dispose();
			}
		});
		
		final JButton buttonDijkstra = new JButton("Dijkstra");
		buttonDijkstra.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				setResult(PathFinder.DIJKSTRA);
				dispose();
			}
		});
		
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1D, 1D };
		layout.columnWidths = new int[] { 128, 128 };
		layout.rowHeights = new int[] { 64 };
		
		setLayout(layout);
		
		final GridBagConstraints gbc = SwingUtils.createGBC();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets.set(5, 5, 5, 5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(buttonAStar, gbc);
		
		gbc.gridx++;
		add(buttonDijkstra, gbc);
	}
	
}