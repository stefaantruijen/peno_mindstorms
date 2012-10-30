package bluebot.core;


import bluebot.util.AbstractEventDispatcher;



/**
 * Skeletal implementation of the {@link Controller} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractController
		extends AbstractEventDispatcher<ControllerListener>
		implements Controller {
	
	public void doPolygon(final int corners, final float length) {
		final float angle = (360F / corners);
		for (int i = corners; i > 0; i--) {
			moveForward(length);
			turnRight(angle);
		}
	}
	
	protected void fireError(final String msg) {
		for (final ControllerListener listener : getListeners()) {
			listener.onError(msg);
		}
	}
	
	protected void fireMessage(final String msg, final String title) {
		for (final ControllerListener listener : getListeners()) {
			listener.onMessage(msg, title);
		}
	}
	
}