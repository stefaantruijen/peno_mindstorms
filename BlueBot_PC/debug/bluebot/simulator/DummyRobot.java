package bluebot.simulator;


import java.awt.Toolkit;
import java.io.File;
import java.lang.reflect.Method;

import bluebot.AbstractRobot;
import bluebot.Robot;
import bluebot.graph.Border;
import bluebot.graph.Tile;
import bluebot.maze.Maze;
import bluebot.util.Constants;
import bluebot.util.Orientation;
import bluebot.util.Utils;



/**
 * {@link Robot} implementation for debugging purposes
 * 
 * @author Ruben Feyen
 */
public class DummyRobot extends AbstractRobot {
	
	private static final float FACTOR_SPEED = 5F;
	
	private Maze maze;
	private Motion motion;
	private Pos pos = new Pos();
	private double speedRotate = (DEFAULT_SPEED_ROTATE * Constants.RADIANS_PER_DEGREE);
	private double speedTravel = (DEFAULT_SPEED_TRAVEL * 4D);
	
	
	public DummyRobot(final Maze maze) {
		this.maze = maze;
	}
	
	
	
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
	
	@Override
	protected float getMaximumSpeedRotate() {
		return (FACTOR_SPEED * super.getMaximumSpeedRotate());
	}
	
	@Override
	protected float getMaximumSpeedTravel() {
		return (FACTOR_SPEED * super.getMaximumSpeedTravel());
	}
	
	private final Maze getMaze() {
		return maze;
	}
	
	public Orientation getOrientation() {
		final Motion motion = this.motion;
		
		Pos pos = this.pos;
		if (motion != null) {
			pos = motion.update(pos);
		}
		
		final float z = Utils.clampAngleDegrees((float)Math.toDegrees(pos.z));
		return new Orientation((float)pos.x, (float)pos.y,
				z, getHeadingHead());
	}
	
	protected float getSpeedTravel() {
		return (float)speedTravel;
	}
	
	private final Tile getTile(float x, float y) {
		final float half = (Tile.SIZE / 2F);
		
		x += half;
		y += half;
		
		final int tx = (int)Math.floor(x / Tile.SIZE);
		final int ty = (int)Math.floor(y / Tile.SIZE);
		
		return getMaze().getTile(tx, ty);
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
	
	public void playSound() {
		Toolkit.getDefaultToolkit().beep();
	}
	
	public void playSound(final File file) {
		// ignored
	}
	
	@Override
	protected int readSensorLight(float x, float y) {
		final Tile tile = getTile(x, y);
		if (tile == null) {
			// We're not even on a tile!?
			throw new RuntimeException("Oh dear ...");
		}
		
		x -= (tile.getX() * Tile.SIZE);
		y -= (tile.getY() * Tile.SIZE);
		
		final float threshold = 199F;
		if ((Math.abs(x) >= threshold) || (Math.abs(y) >= threshold)) {
			// We're on a white line on the edge of the tile
			return 65;
		}
		
		// We're somewhere random on the tile
		return 50;
	}
	
	// TODO: Remove asap
	public int readSensorLightValue() {
		return (int)Math.round(10.23D * readSensorLight());
	}
	
	@Override
	protected int readSensorUltraSonic(float x, float y,
			final float heading) {
		Tile tile = getTile(x, y);
		if (tile == null) {
			// We're not on a tile!?
			throw new RuntimeException("Oh dear ...");
		}
		
		final bluebot.graph.Orientation dir =
				bluebot.graph.Orientation.forHeading(heading);
		
		float distance = ((Tile.SIZE / 2F) - OFFSET_SENSOR_ULTRASONIC);
		
		final int dx, dy;
		final Method method;
		try {
			switch (dir) {
				case NORTH:
					dx = 0;
					dy = 1;
					method = Tile.class.getDeclaredMethod("getBorderNorth");
					break;
				case EAST:
					dx = 1;
					dy = 0;
					method = Tile.class.getDeclaredMethod("getBorderEast");
					break;
				case SOUTH:
					dx = 0;
					dy = -1;
					method = Tile.class.getDeclaredMethod("getBorderSouth");
					break;
				case WEST:
					dx = -1;
					dy = 0;
					method = Tile.class.getDeclaredMethod("getBorderWest");
					break;
				default:
					// Error
					return 255;
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		
		Border border;
		while (tile != null) {
			try {
				border = (Border)method.invoke(tile);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
			
			if (border != Border.OPEN) {
				break;
			}
			
			distance += Tile.SIZE;
			if (distance >= 2550F) {
				return 255;
			}
			
			tile = getMaze().getTile((tile.getX() + dx), (tile.getY() + dy));
		}
		
		return Math.round(distance / 10F);
	}
	
	public void resetOrientation() {
		this.pos = new Pos();
	}
	
	public void setTravelSpeed(final double speed) {
		this.speedTravel = speed;
	}
	
	protected void setSpeedRotate(final float speed) {
		this.speedRotate = (speed * Constants.RADIANS_PER_DEGREE);
	}
	
	protected void setSpeedTravel(final float speed) {
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
	public float getArcLimit() {
		return 100;
	}
	
}