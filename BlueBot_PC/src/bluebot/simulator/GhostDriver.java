package bluebot.simulator;


import java.io.IOException;

import bluebot.DefaultDriver;
import bluebot.Driver;
import bluebot.DriverException;
import bluebot.Robot;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.actions.impl.CalibrationAction;
import bluebot.actions.impl.MazeActionV2;
import bluebot.io.AbstractConnection;
import bluebot.io.protocol.Packet;
import bluebot.sensors.Calibration;



/**
 * 
 * @author Ruben Feyen
 */
public class GhostDriver extends DefaultDriver implements Runnable {
	
//	private GhostAction action;
	private Thread host;
	private int id;
	
	
	public GhostDriver(final Robot robot, final int id) {
		super(robot, new GhostConnection());
//		this.action = new GhostAction(id);
		this.id = id;
		init();
	}
	
	
	
	public float getHeading() {
		return getRobot().getHeading();
	}
	
	@Override
	protected VirtualRobot getRobot() {
		return (VirtualRobot)super.getRobot();
	}
	
	public float getX() {
		return (10F * (getRobot().getImgX() - 20));
	}
	
	public float getY() {
		return (10F * (getRobot().getImgY() - 20));
	}
	
	private final void init() {
		final Calibration config = getCalibration();
		config.setLightThresholdBlack(267);
		config.setLightThresholdWhite(556);
	}
	
	public void run() {
		try {
			System.out.println("Ghost starting maze action ...");
			new MazeActionV2(id).execute(this);
			System.out.println("Ghost finished maze action!");
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void startGhost() {
		if (host == null) {
			host = new Thread(this);
			host.setDaemon(true);
			host.start();
		}
	}
	
	@SuppressWarnings("deprecation")
	public synchronized void stopGhost() {
		if (host != null) {
			host.stop();
			host = null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unused")
	private static final class GhostAction extends Action {
		
		private int id;
		
		
		public GhostAction(final int id) {
			this.id = id;
		}
		
		
		
		protected void execute()
				throws ActionException, DriverException, InterruptedException {
			final Driver driver = getDriver();
			new CalibrationAction().execute(driver);
			driver.moveBackward(400F, true);
			executeWhiteLine();
			driver.moveBackward(200F, true);
			new MazeActionV2(id).execute(driver);
		}
		
	}
	
	
	
	
	
	private static final class GhostConnection extends AbstractConnection {
		
		private static final Object lock = new Object();
		
		
		
		public void close() {
			synchronized (lock) {
				lock.notifyAll();
			}
		}
		
		protected Packet read() throws IOException {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (final InterruptedException e) {
					throw new IOException(e);
				}
			}
			throw new IOException("Ghost connection has been closed");
		}
		
		protected void write(final Packet packet) {
			// ignored
		}
		
	}
	
}