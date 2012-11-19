package bluebot.nxt;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractWheel
//		extends AbstractEventDispatcher<WheelListener>
		implements Wheel {
	
	private static final double RATIO = (Math.PI / 360D);
	
	private float diameter;
	private WheelListener listener;
	
	
	public AbstractWheel(final float diameter) {
		setDiameter(diameter);
	}
	
	
	
	public void addListener(final WheelListener listener) {
		this.listener = listener;
	}
	
	protected float convertAngleToDistance(final float angle) {
		return (float)(RATIO * angle * getDiameter());
	}
	
	protected void fireStarted(final long time, final float delta) {
//		for (final WheelListener listener : getListeners()) {
//			listener.onWheelStarted(time, delta);
//		}
		if (listener != null) {
			listener.onWheelStarted(time, delta);
		}
	}
	
	protected void fireStopped(final long time, final float delta) {
//		for (final WheelListener listener : getListeners()) {
//			listener.onWheelStopped(time, delta);
//		}
		if (listener != null) {
			listener.onWheelStopped(time, delta);
		}
	}
	
	public float getDiameter() {
		return diameter;
	}
	
	public void removeListener(final WheelListener listener) {
		if (this.listener == listener) {
			this.listener = null;
		}
	}
	
	public void removeListeners() {
		this.listener = null;
	}
	
	private final void setDiameter(final float diameter) {
		this.diameter = diameter;
	}
	
}