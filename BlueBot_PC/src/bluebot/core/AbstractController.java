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
		for (int i = corners; i > 0; i++) {
			moveForward(length);
			turnRight(angle);
		}
	}
	
	protected void fireMessageIncoming(final String msg) {
		for (final ControllerListener listener : getListeners()) {
			listener.onMessageIncoming(msg);
		}
	}
	
	protected void fireMessageOutgoing(final String msg) {
		for (final ControllerListener listener : getListeners()) {
			listener.onMessageOutgoing(msg);
		}
	}
	
	protected abstract void moveBackward(float distance);
	
	protected abstract void moveForward(float distance);
	
	protected abstract void turnLeft(float angle);
	
	protected abstract void turnRight(float angle);
	
}