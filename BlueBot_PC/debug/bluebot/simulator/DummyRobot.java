package bluebot.simulator;


import bluebot.Robot;
import bluebot.util.Orientation;



/**
 * 
 * @author Ruben Feyen
 */
public class DummyRobot implements Robot {
	
	private Motion motion;
	private Pos pos = new Pos();
	private double speedRotate = (DEFAULT_SPEED_ROTATE * Math.PI / 180D);
	private double speedTravel = (DEFAULT_SPEED_TRAVEL * 4D);
	
	
	
	private final void finishMotion() {
		motion.finish();
	}
	
	public float getAngleIncrement() {
		final Motion motion = this.motion;
		if (motion instanceof Turn) {
			final Turn turn = (Turn)motion;
			return (float)(turn.getAngleRotated() * 180D / Math.PI);
		}
		return 0F;
	}
	
	public Orientation getOrientation() {
		final Motion motion = this.motion;
		
		Pos pos = this.pos;
		if (motion != null) {
			pos = motion.update(pos);
		}
		
		return new Orientation((float)pos.x, (float)pos.y,
				(float)(pos.z * 180D / Math.PI));
	}
	
	public synchronized boolean isMoving() {
		return ((motion != null) && !motion.isFinished());
	}
	
	public void moveBackward() {
		startMotion(new Move(speedTravel, false));
	}
	
	public void moveBackward(float distance, boolean wait) {
		startMotion(new Move(speedTravel, false, distance));
		if (wait) {
			finishMotion();
		}
	}
	
	public void moveForward() {
		startMotion(new Move(speedTravel, true));
	}
	
	public void moveForward(float distance, boolean wait) {
		startMotion(new Move(speedTravel, true, distance));
		if (wait) {
			finishMotion();
		}
	}
	
	public int readSensorLight() {
		// TODO
		return (45 + (int)(Math.random() * 10));
	}
	
	public int readSensorUltraSonic() {
		// TODO
		return 255;
	}
	
	public void setTravelSpeed(final double speed) {
		this.speedTravel = speed;
	}
	
	private synchronized void startMotion(final Motion motion) {
		stopMotion();
		this.motion = motion;
	}
	
	private synchronized void stopMotion() {
		if (motion != null) {
			pos = motion.update(pos);
			motion = null;
		}
	}
	
	public void stop() {
		stopMotion();
	}
	
	public void turnLeft() {
		startMotion(new Turn(speedRotate, false));
	}
	
	public void turnLeft(float angle, boolean wait) {
		startMotion(new Turn(speedRotate, false, (angle * Math.PI / 180D)));
		if (wait) {
			finishMotion();
		}
	}
	
	public void turnRight() {
		startMotion(new Turn(speedRotate, true));
	}
	
	public void turnRight(float angle, boolean wait) {
		startMotion(new Turn(speedRotate, true, (angle * Math.PI / 180D)));
		if (wait) {
			finishMotion();
		}
	}
	
	
	
	
	
	
	
	
	
	
	private static abstract class Motion {
		
		private long duration;
		private double speed;
		private long start = System.currentTimeMillis();
		
		
		public Motion(final double speed) {
			this(speed, -1L);
		}
		public Motion(final double speed, final long duration) {
			this.duration = duration;
			this.speed = speed;
		}
		
		
		
		public void finish() {
			if (isFinite()) {
				final long end = (start + duration);
				while (System.currentTimeMillis() < end) {
					try {
						Thread.sleep(10);
					} catch (final InterruptedException e) {
						// ignored
					}
				}
			}
		}
		
		public long getDuration() {
			return duration;
		}
		
		public double getSpeed() {
			return speed;
		}
		
		public long getTimePassed() {
			long time = (System.currentTimeMillis() - start);
			if (isFinite() && (time > duration)) {
				time = duration;
			}
			return time;
		}
		
		public boolean isFinished() {
			return (isFinite() && (getTimePassed() >= getDuration()));
		}
		
		protected boolean isFinite() {
			return (getDuration() > 0);
		}
		
		public abstract Pos update(Pos pos);
		
	}
	
	
	
	
	
	private static final class Move extends Motion {
		
		private double distance;
		private boolean forward;
		
		
		public Move(final double speed, final boolean forward) {
			super(speed);
			this.forward = forward;
		}
		public Move(final double speed, final boolean forward,
				final double distance) {
			super(speed, Math.round(1000D * distance / speed));
			this.distance = distance;
			this.forward = forward;
		}
		
		
		
		public double getDistanceTravelled() {
			final long time = getTimePassed();
			
			final double distance;
			if (isFinite() && (time >= getDuration())) {
				distance = this.distance;
			} else {
				distance = (time * getSpeed() / 1000D);
			}
			
			return (forward ? distance : -distance);
		}
		
		public Pos update(final Pos pos) {
			final double distance = getDistanceTravelled();
			return new Pos(
					(pos.x + (distance * Math.sin(pos.z))),
					(pos.y + (distance * Math.cos(pos.z))),
					pos.z);
		}
		
	}
	
	
	
	
	
	private static final class Pos {
		
		private final double x, y, z;
		
		
		private Pos() {
			this(0D, 0D, 0D);
		}
		private Pos(final double x, final double y, final double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
	}
	
	
	
	
	
	private static final class Turn extends Motion {
		
		private double angle;
		private boolean right;
		
		
		public Turn(final double speed, final boolean right) {
			super(speed);
			this.right = right;
		}
		public Turn(final double speed, final boolean right,
				final double angle) {
			super(speed, Math.round(1000D * angle / speed));
			this.angle = angle;
			this.right = right;
		}
		
		
		
		public double getAngleRotated() {
			final long time = getTimePassed();
			
			final double angle;
			if (isFinite() && (time >= getDuration())) {
				angle = this.angle;
			} else {
				angle = (time * getSpeed() / 1000D);
			}
			
			return (right ? angle : -angle);
		}
		
		public Pos update(final Pos pos) {
			return new Pos(pos.x, pos.y, (pos.z + getAngleRotated()));
		}
		
	}





	@Override
	public void turnHeadCWise(int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnHeadCCWise(int offset) {
		// TODO Auto-generated method stub
		
	}
	
}