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
	
}