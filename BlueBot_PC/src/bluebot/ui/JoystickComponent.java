package bluebot.ui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * 
 * @author Ruben Feyen
 */
public class JoystickComponent extends JPanel {
	private static final long serialVersionUID = 1L;
	
	
	private Button buttonBackward, buttonForward, buttonLeft, buttonRight, buttonStop;
	
	
	public JoystickComponent() {
		initComponents();
		setFocusable(true);
		addKeyListener(createKeyListener());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent event) {
				requestFocusInWindow();
			}
		});
	}
	
	
	
	public void addListener(final JoystickListener listener) {
		listenerList.add(JoystickListener.class, listener);
	}
	
	private final KeyListener createKeyListener() {
		return new ThrottledKeyAdapter(new KeyMonitor());
	}
	
	private final void fireBackward(final boolean pressed, final boolean mod) {
//		Debug.print("Move backward:  %s", pressed);
		buttonBackward.setPressed(pressed);
		for (final JoystickListener listener : getListeners()) {
			listener.onJoystickBackward(pressed, mod);
		}
	}
	
	private final void fireForward(final boolean pressed, final boolean mod) {
//		Debug.print("Move forward:  %s", pressed);
		buttonForward.setPressed(pressed);
		for (final JoystickListener listener : getListeners()) {
			listener.onJoystickForward(pressed, mod);
		}
	}
	
	private final void fireLeft(final boolean pressed, final boolean mod) {
//		Debug.print("Turn left:  %s", pressed);
		buttonLeft.setPressed(pressed);
		for (final JoystickListener listener : getListeners()) {
			listener.onJoystickLeft(pressed, mod);
		}
	}
	
	private final void fireRight(final boolean pressed, final boolean mod) {
//		Debug.print("Turn right:  %s", pressed);
		buttonRight.setPressed(pressed);
		for (final JoystickListener listener : getListeners()) {
			listener.onJoystickRight(pressed, mod);
		}
	}
	
	private final void fireStop(final boolean pressed) {
		buttonStop.setPressed(pressed);
		if (pressed) {
//			Debug.print("STOP");
			for (final JoystickListener listener : getListeners()) {
				listener.onJoystickStop();
			}
		}
	}
	
	private final JoystickListener[] getListeners() {
		return listenerList.getListeners(JoystickListener.class);
	}
	
	private final void initComponents() {
		buttonForward = new Button("Z");
		buttonBackward = new Button("S");
		buttonLeft = new Button("Q");
		buttonRight = new Button("D");
		buttonStop = new Button("H");
		
		setLayout(new GridBagLayout());
		
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
	
	public void removeListener(final JoystickListener listener) {
		listenerList.remove(JoystickListener.class, listener);
	}
	
	
	
	
	
	
	
	
	
	
	private static final class Button extends JLabel {
		private static final long serialVersionUID = 1L;
		
		private static final Dimension DEFAULT_SIZE = new Dimension(64, 64);
		
		
		public Button(final String label) {
			super(label, CENTER);
			setBorder(BorderFactory.createEtchedBorder());
			setOpaque(true);
			setPreferredSize(DEFAULT_SIZE);
			setPressed(false);
		}
		
		
		
		public void setPressed(final boolean pressed) {
			if (pressed) {
				setBackground(Color.BLACK);
				setForeground(Color.WHITE);
			} else {
				setBackground(Color.WHITE);
				setForeground(Color.BLACK);
			}
			repaint(0L);
		}
		
	}
	
	
	
	
	
	private final  class KeyMonitor extends KeyAdapter {
		
		@Override
		public void keyPressed(final KeyEvent event) {
//			Debug.print("KEY CODE:  " + event.getKeyCode());
			switch (event.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_Z:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_NUMPAD8:
					fireForward(true, event.isControlDown());
					break;
					
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_NUMPAD2:
					fireBackward(true, event.isControlDown());
					break;
					
				case KeyEvent.VK_A:
				case KeyEvent.VK_Q:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_NUMPAD4:
					fireLeft(true, event.isControlDown());
					break;
					
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_NUMPAD6:
					fireRight(true, event.isControlDown());
					break;
					
				case KeyEvent.VK_H:
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_NUMPAD5:
					fireStop(true);
					break;
			}
		}
		
		@Override
		public void keyReleased(final KeyEvent event) {
			switch (event.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_Z:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_NUMPAD8:
					fireForward(false, event.isControlDown());
					break;
					
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_NUMPAD2:
					fireBackward(false, event.isControlDown());
					break;
					
				case KeyEvent.VK_A:
				case KeyEvent.VK_Q:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_NUMPAD4:
					fireLeft(false, event.isControlDown());
					break;
					
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_NUMPAD6:
					fireRight(false, event.isControlDown());
					break;
					
				case KeyEvent.VK_H:
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_NUMPAD5:
					fireStop(false);
					break;
			}
		}
		
	}
	
}