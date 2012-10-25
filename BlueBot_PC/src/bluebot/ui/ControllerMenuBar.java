package bluebot.ui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import bluebot.core.Controller;



/**
 * 
 * @author Ruben Feyen
 */
public class ControllerMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	
	
	public ControllerMenuBar(final Controller controller) {
		this.controller = controller;
		
		init();
	}
	
	
	
	private final JMenu createMenuCommands() {
		final JMenu menu = new JMenu("Commands");
		
		final JMenuItem itemCalibrate = new JMenuItem("Calibrate");
		itemCalibrate.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				controller.doCalibrate();
			}
		});
		menu.add(itemCalibrate);
		
		final JMenuItem itemWhiteLine = new JMenuItem("Orientate on white line");
		itemWhiteLine.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				controller.doWhiteLineOrientation();
			}
		});
		menu.add(itemWhiteLine);
		
		final JMenuItem itemPolygon = new JMenuItem("Polygon");
		itemPolygon.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doPolygon();
			}
		});
		menu.add(itemPolygon);
		
		return menu;
	}
	
	private final void doPolygon() {
		final PolygonDialog dialog = new PolygonDialog();
		if (dialog.confirm()) {
			controller.doPolygon(dialog.getCorners(), dialog.getLength());
		}
	}
	
	private final void init() {
		add(createMenuCommands());
	}
	
}