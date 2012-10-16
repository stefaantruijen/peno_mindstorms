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
			System.out.println("dialog confirmed");
			System.out.println("corners:  " + dialog.getCorners());
			System.out.println("length:  " + dialog.getLength());
			controller.doPolygon(dialog.getCorners(), dialog.getLength());
		} else {
			System.out.println("dialog has been cancelled");
		}
	}
	
	private final void init() {
		add(createMenuCommands());
	}
	
}