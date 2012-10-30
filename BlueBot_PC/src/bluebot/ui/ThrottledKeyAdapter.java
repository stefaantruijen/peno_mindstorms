package bluebot.ui;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.BitSet;



/**
 * 
 * @author Ruben Feyen
 */
public class ThrottledKeyAdapter extends KeyAdapter {
	
	private KeyListener delegate;
	private BitSet pressedKeys;
	
	
	public ThrottledKeyAdapter(final KeyListener delegate) {
		if (delegate == null) {
			throw new NullPointerException();
		}
		this.delegate = delegate;
		this.pressedKeys = new BitSet(0xFF);
	}
	
	
	
	@Override
	public void keyPressed(final KeyEvent event) {
		final int code = event.getKeyCode();
		if ((code < 0xFF) && !pressedKeys.get(code)) {
			pressedKeys.set(code);
			delegate.keyPressed(event);
		}
	}
	
	@Override
	public void keyReleased(final KeyEvent event) {
		final int code = event.getKeyCode();
		if (code < 0xFF) {
			pressedKeys.clear(code);
		}
		delegate.keyReleased(event);
	}
	
}