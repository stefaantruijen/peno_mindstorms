package bluebot.nxt;


import bluebot.util.Orientation;
import bluebot.util.Utils;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultAxle extends AbstractAxle {
	
	private final Object lock = new Object();
	
	private float heading;
	private Struct left, right;
	private float x, y;
	
	
	public DefaultAxle(final float span, final Wheel left, final Wheel right) {
		super(span);
		this.left = createStruct(left);
		this.right = createStruct(right);
		
		left.addListener(new WheelAdapter() {
			@Override
			public void onWheelStarted(final long time, final float delta) {
				synchronized (lock) {
					updateLeft(delta);
				}
			}
		});
		right.addListener(new WheelAdapter() {
			@Override
			public void onWheelStarted(final long time, final float delta) {
				synchronized (lock) {
					updateRight(delta);
				}
			}
		});
		
		reset();
	}
	
	
	
	private static final Struct createStruct(final Wheel wheel) {
		final Struct struct = new Struct();
		struct.wheel = wheel;
		return struct;
	}
	
	private final float[] diff() {
		return diff(left.wheel.getDelta(), right.wheel.getDelta());
	}
	
	private final float[] diff(final float deltaLeft, final float deltaRight) {
		if (left.first || right.first) {
			return null;
		}
		
		final float dl = (deltaLeft - left.delta);
		final float dr = (deltaRight - right.delta);
		if (Utils.equals(dl, 0F) && Utils.equals(dr, 0F)) {
			return null;
		}
		
		if ((dl * dr) > 0F) {
			// Moved
			final float distance = ((dl + dr) / 2F);
			return new float[]  {
					(float)(distance * Math.sin(heading)),
					(float)(distance * Math.cos(heading)),
					0F
			};
		} else {
			// Turned
			final float distance = ((Math.abs(dl) + Math.abs(dr)) / 2F);
			final float angle = convertDistanceToAngle(distance);
			return new float[] {
					0F,
					0F,
					((dl > 0F) ? angle : -angle)
			};
		}
	}
	
	public Orientation getOrientation() {
		synchronized (lock) {
			final float[] diff = diff();
			if (diff == null) {
				return new Orientation(x, y, heading);
			} else {
				return new Orientation(
						(x + diff[0]),
						(y + diff[1]),
						(heading + diff[2]));
			}
		}
	}
	
	public void reset() {
		left.wheel.reset();
		right.wheel.reset();
		left.first = right.first = true;
		
		x = y = heading = 0F;
	}
	
	private final void update(final float deltaLeft, final float deltaRight) {
		final float[] diff = diff(deltaLeft, deltaRight);
		if (diff == null) {
			return;
		}
		
		left.delta = deltaLeft;
		right.delta = deltaRight;
		
		x += diff[0];
		y += diff[1];
		heading += diff[2];
	}
	
	private final void updateLeft(final float delta) {
		if (left.first) {
			left.delta = delta;
			left.first = false;
			return;
		}
		if (right.first) {
			return;
		}
		update(delta, right.wheel.getDelta());
	}
	
	private final void updateRight(final float delta) {
		if (right.first) {
			right.delta = delta;
			right.first = false;
			return;
		}
		if (left.first) {
			return;
		}
		update(left.wheel.getDelta(), delta);
	}
	
	
	
	
	
	
	
	
	
	
	private static final class Struct {
		
		private float delta;
		private boolean first = true;
		private Wheel wheel;
		
	}
	
}