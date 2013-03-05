package bluebot.simulator;


import java.io.IOException;

import bluebot.DefaultDriver;
import bluebot.Robot;
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
			new MazeActionV2(id).execute(this);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendMQMessage(final String msg) {
		//	TODO:
		//	Send the message somehow?
		//	Otherwise ghost robots are unable to provide
		//	any RabbitMQ communication
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
