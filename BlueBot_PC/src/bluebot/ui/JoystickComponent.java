package bluebot.ui;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;

import bluebot.core.Controller;
import bluebot.util.Resources;



/**
 * 
 * @author Ruben Feyen
 */
public class JoystickComponent extends JPanel {
	private static final long serialVersionUID = 1L;
	
	
	private Behavior behavior;
	private Button buttonBackward, buttonForward, buttonLeft, buttonRight, buttonStop;
	private boolean special;
	
	
	public JoystickComponent(final Controller controller) {
		setBehavior(new FixedBehavior(controller, FixedBehavior.DISTANCE_LONG));
		
		initComponents();
		setComponentPopupMenu(createContextMenu());
		setFocusable(true);
		addKeyListener(createKeyListener());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent event) {
				requestFocusInWindow();
			}
		});
	}
	
	
	
	private final JPopupMenu createContextMenu() {
		final JPopupMenu menu = new JPopupMenu();
		
		final ActionListener listener = new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				final String command = event.getActionCommand();
				if ((command == null) || command.isEmpty()) {
					// ignored
				} else if (command.equals("FIXED")) {
					setBehaviorFixed();
				} else if (command.equals("FREE")) {
					setBehaviorFree();
				}
			}
		};
		
		final JRadioButtonMenuItem itemFixed = new JRadioButtonMenuItem("Fixed");
		itemFixed.setActionCommand("FIXED");
		itemFixed.addActionListener(listener);
		menu.add(itemFixed);
		
		final JRadioButtonMenuItem itemFree = new JRadioButtonMenuItem("Free");
		itemFree.setActionCommand("FREE");
		itemFree.addActionListener(listener);
		menu.add(itemFree);
		
		final ButtonGroup groupMode = new ButtonGroup();
		groupMode.add(itemFixed);
		groupMode.add(itemFree);
		
		itemFixed.setSelected(true);
//		itemFree.setSelected(true);
		
		return menu;
	}
	
	private final KeyListener createKeyListener() {
		return new ThrottledKeyAdapter(new KeyMonitor());
	}
	
	/*
	private final void fireBackward(final KeyEvent event) {
		switch (event.getID()) {
			case KeyEvent.KEY_PRESSED:
				buttonBackward.setPressed(true);
				getBehavior().onBackwardPressed(event.isControlDown());
				break;
			case KeyEvent.KEY_RELEASED:
				buttonBackward.setPressed(false);
				getBehavior().onBackwardReleased();
				break;
		}
	}
	
	private final void fireForward(final KeyEvent event) {
		switch (event.getID()) {
			case KeyEvent.KEY_PRESSED:
				buttonForward.setPressed(true);
				getBehavior().onForwardPressed(event.isControlDown());
				break;
			case KeyEvent.KEY_RELEASED:
				buttonForward.setPressed(false);
				getBehavior().onForwardReleased();
				break;
		}
	}
	
	private final void fireLeft(final KeyEvent event) {
		switch (event.getID()) {
			case KeyEvent.KEY_PRESSED:
				buttonLeft.setPressed(true);
				getBehavior().onLeftPressed(event.isControlDown());
				break;
			case KeyEvent.KEY_RELEASED:
				buttonLeft.setPressed(false);
				getBehavior().onLeftReleased();
				break;
		}
	}
	
	private final void fireRight(final KeyEvent event) {
		switch (event.getID()) {
			case KeyEvent.KEY_PRESSED:
				buttonRight.setPressed(true);
				getBehavior().onRightPressed(event.isControlDown());
				break;
			case KeyEvent.KEY_RELEASED:
				buttonRight.setPressed(false);
				getBehavior().onRightReleased();
				break;
		}
	}
	
	private final void fireStop(final KeyEvent event) {
		switch (event.getID()) {
			case KeyEvent.KEY_PRESSED:
				buttonStop.setPressed(true);
				getBehavior().onStopPressed();
				break;
			case KeyEvent.KEY_RELEASED:
				buttonStop.setPressed(false);
				getBehavior().onStopReleased();
				break;
		}
	}
	*/
	
	private final Behavior getBehavior() {
		return behavior;
	}
	
	private final void initComponents() {
		buttonBackward = new Button(loadIcon("arrow_down"));
		buttonBackward.addItemListener(new ButtonListener() {
			protected void firePressed() {
				getBehavior().onBackwardPressed(false);
			}
			
			protected void fireReleased() {
				getBehavior().onBackwardReleased();
			}
		});
		buttonForward = new Button(loadIcon("arrow_up"));
		buttonForward.addItemListener(new ButtonListener() {
			protected void firePressed() {
				getBehavior().onForwardPressed(false);
			}
			
			protected void fireReleased() {
				getBehavior().onForwardReleased();
			}
		});
		buttonLeft = new Button(loadIcon("arrow_left"));
		buttonLeft.addItemListener(new ButtonListener() {
			protected void firePressed() {
				getBehavior().onLeftPressed(false);
			}
			
			protected void fireReleased() {
				getBehavior().onLeftReleased();
			}
		});
		buttonRight = new Button(loadIcon("arrow_right"));
		buttonRight.addItemListener(new ButtonListener() {
			protected void firePressed() {
				getBehavior().onRightPressed(false);
			}
			
			protected void fireReleased() {
				getBehavior().onRightReleased();
			}
		});
		buttonStop = new Button(loadIcon("stop"));
		buttonStop.addItemListener(new ButtonListener() {
			protected void firePressed() {
				getBehavior().onStopPressed();
			}
			
			protected void fireReleased() {
				getBehavior().onStopReleased();
			}
		});
		
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 0D, 0D, 0D };
		layout.rowWeights = new double[] { 0D, 0D, 0D };
		setLayout(layout);
		
		final GridBagConstraints gbc = SwingUtils.createGBC();	
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(buttonForward, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		add(buttonLeft, gbc);
		
		gbc.gridx++;
		add(buttonStop, gbc);
		
		gbc.gridx++;
		add(buttonRight, gbc);
		
		gbc.gridx = 1;
		gbc.gridy++;
		add(buttonBackward, gbc);
	}
	
	private static final Icon loadIcon(final String name) {
		return Resources.loadIcon(JoystickComponent.class, (name + ".png"));
	}
	
	private final void setBehavior(final Behavior behavior) {
		if (behavior == null) {
			throw new NullPointerException("Behavior may not be NULL");
		}
		this.behavior = behavior;
	}
	
	private final void setBehaviorFixed() {
		final Controller controller = getBehavior().getController();
		final Behavior behavior;
		if (special) {
			behavior = new FixedBehavior(controller, FixedBehavior.DISTANCE_SHORT);
		} else {
			behavior = new FixedBehavior(controller, FixedBehavior.DISTANCE_LONG);
		}
		setBehavior(behavior);
	}
	
	private final void setBehaviorFree() {
		setBehavior(new FreeBehavior(getBehavior().getController()));
	}
	
	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		buttonBackward.setEnabled(enabled);
		buttonForward.setEnabled(enabled);
		buttonLeft.setEnabled(enabled);
		buttonRight.setEnabled(enabled);
		buttonStop.setEnabled(enabled);
	}
	
	
	
	
	
	
	
	
	
	
	private static abstract class Behavior {
		
		private Controller controller;
		
		
		protected Behavior(final Controller controller) {
			this.controller = controller;
		}
		
		
		
		protected final Controller getController() {
			return controller;
		}
		
		public abstract void onBackwardPressed(boolean mod);
		
		public abstract void onBackwardReleased();
		
		public abstract void onForwardPressed(boolean mod);
		
		public abstract void onForwardReleased();
		
		public abstract void onLeftPressed(boolean mod);
		
		public abstract void onLeftReleased();
		
		public abstract void onRightPressed(boolean mod);
		
		public abstract void onRightReleased();
		
		public void onStopPressed() {
			getController().stop();
		}
		
		public void onStopReleased() {
			// ignored
		}
		
	}
	
	
	
	
	
	private static final class Button extends JToggleButton {
		private static final long serialVersionUID = 1L;
		
		private static final Dimension DEFAULT_SIZE = new Dimension(64, 64);
		
		
		public Button(final Icon icon) {
			super(icon);
//			setMinimumSize(DEFAULT_SIZE);
			setPreferredSize(DEFAULT_SIZE);
//			setPressed(false);
			setSelected(false);
		}
//		public Button(final String label) {
//			super(label, CENTER);
//			setBorder(BorderFactory.createEtchedBorder());
//			setMinimumSize(DEFAULT_SIZE);
//			setOpaque(true);
//			setPreferredSize(DEFAULT_SIZE);
//			setPressed(false);
//			addItemListener(new ItemListener() {
//				public void itemStateChanged(final ItemEvent event) {
//					
//				}
//			});
//		}
		
		
		
		@Override
		protected void processMouseEvent(final MouseEvent event) {
//			System.out.println(event);
			if (isEnabled()) {
				switch (event.getID()) {
					case MouseEvent.MOUSE_PRESSED:
						setSelected(true);
						break;
						
					case MouseEvent.MOUSE_RELEASED:
					case MouseEvent.MOUSE_EXITED:
						setSelected(false);
						break;
				}
			}
		}
		
//		public void setPressed(final boolean pressed) {
//			if (pressed) {
//				setBackground(Color.BLACK);
//				setForeground(Color.WHITE);
//			} else {
//				setBackground(Color.WHITE);
//				setForeground(Color.BLACK);
//			}
//			repaint(0L);
//		}
		
	}
	
	
	
	
	
	private static abstract class ButtonListener implements ItemListener {
		
		protected abstract void firePressed();
		
		protected abstract void fireReleased();
		
		public void itemStateChanged(final ItemEvent event) {
			switch (event.getStateChange()) {
				case ItemEvent.DESELECTED:
					fireReleased();
					break;
					
				case ItemEvent.SELECTED:
					firePressed();
					break;
			}
		}
		
	}
	
	
	
	
	
	private static final class FixedBehavior extends Behavior {
		
		private static final float ANGLE =  90F;
		private static final float DISTANCE_LONG = 400F;
		private static final float DISTANCE_SHORT = 200F;
		
		private float distance;
		
		
		private FixedBehavior(final Controller controller, final float distance) {
			super(controller);
			this.distance = distance;
		}
		
		
		
		public void onBackwardPressed(final boolean mod) {
			getController().moveBackward(distance,false);
		}
		
		public void onBackwardReleased() {
			// ignored
		}
		
		public void onForwardPressed(final boolean mod) {
			getController().moveForward(distance,false);
		}
		
		public void onForwardReleased() {
			// ignored
		}
		
		public void onLeftPressed(final boolean mod) {
			getController().turnLeft(ANGLE,false);
		}
		
		public void onLeftReleased() {
			// ignored
		}
		
		public void onRightPressed(final boolean mod) {
			getController().turnRight(ANGLE,false);
		}
		
		public void onRightReleased() {
			// ignored
		}
		
	}
	
	
	
	
	
	private static final class FreeBehavior extends Behavior {
		
		private FreeBehavior(final Controller controller) {
			super(controller);
		}
		
		
		
		public void onBackwardPressed(final boolean mod) {
			getController().moveBackward();
		}
		
		public void onBackwardReleased() {
			getController().stop();
		}
		
		public void onForwardPressed(final boolean mod) {
			getController().moveForward();
		}
		
		public void onForwardReleased() {
			getController().stop();
		}
		
		public void onLeftPressed(final boolean mod) {
			getController().turnLeft();
		}
		
		public void onLeftReleased() {
			getController().stop();
		}
		
		public void onRightPressed(final boolean mod) {
			getController().turnRight();
		}
		
		public void onRightReleased() {
			getController().stop();
		}
		
	}
	
	private final class KeyMonitor extends KeyAdapter {
		
		@Override
		public void keyPressed(final KeyEvent event) {
			if (!isEnabled()) {
				return;
			}
			if (!special && event.isControlDown()) {
				special = true;
				if (getBehavior() instanceof FixedBehavior) {
					setBehaviorFixed();
				}
			}
			switch (event.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_Z:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_NUMPAD8:
//					fireForward(event);
					buttonForward.setSelected(true);
					break;
					
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_NUMPAD2:
//					fireBackward(event);
					buttonBackward.setSelected(true);
					break;
					
				case KeyEvent.VK_A:
				case KeyEvent.VK_Q:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_NUMPAD4:
//					fireLeft(event);
					buttonLeft.setSelected(true);
					break;
					
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_NUMPAD6:
//					fireRight(event);
					buttonRight.setSelected(true);
					break;
					
				case KeyEvent.VK_H:
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_NUMPAD5:
//					fireStop(event);
					buttonStop.setSelected(true);
					break;
			}
		}
		
		@Override
		public void keyReleased(final KeyEvent event) {
			if (!isEnabled()) {
				return;
			}
			if (special && !event.isControlDown()) {
				special = false;
				if (getBehavior() instanceof FixedBehavior) {
					setBehaviorFixed();
				}
			}
			switch (event.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_Z:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_NUMPAD8:
//					fireForward(event);
					buttonForward.setSelected(false);
					break;
					
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_NUMPAD2:
//					fireBackward(event);
					buttonBackward.setSelected(false);
					break;
					
				case KeyEvent.VK_A:
				case KeyEvent.VK_Q:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_NUMPAD4:
//					fireLeft(event);
					buttonLeft.setSelected(false);
					break;
					
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_NUMPAD6:
//					fireRight(event);
					buttonRight.setSelected(false);
					break;
					
				case KeyEvent.VK_H:
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_NUMPAD5:
//					fireStop(event);
					buttonStop.setSelected(false);
					break;
			}
		}
		
	}
	
}