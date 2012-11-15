package bluebot;


import static bluebot.core.PhysicalRobot.*;
import static bluebot.io.protocol.PacketFactory.getPacketFactory;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

import bluebot.io.Connection;
import bluebot.io.ServerConnection;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.impl.MovePacket;
import bluebot.nxt.Axle;
import bluebot.nxt.DefaultAxle;
import bluebot.nxt.DefaultWheel;



/**
 * This NXT program is used to perform testing
 * 
 * @author Ruben Feyen
 */
public class TestBot implements Runnable {
	
	private Connection connection;
	
	
	private TestBot(final Connection connection) {
		this.connection = connection;
	}
	
	
	
	public static void main(final String... args) throws Exception {
		System.out.println("Waiting ...");
		final TestBot bot = new TestBot(ServerConnection.create());
		
		System.out.println("Running ...");
		final Thread thread = new Thread(bot);
		thread.setDaemon(true);
		thread.start();
		
		Button.waitForAnyPress();
		
		System.out.println("Stopping ...");
		thread.interrupt();
	}
	
	public void run() {
		final DifferentialPilot pilot = createPilot();
		
		final OdometryPoseProvider opp = new OdometryPoseProvider(pilot);
		final Axle axle = new DefaultAxle(WHEEL_SPAN,
				new DefaultWheel(WHEEL_DIAMETER_LEFT, Motor.A),
				new DefaultWheel(WHEEL_DIAMETER_RIGHT, Motor.C));
		
		final Thread sender = new Thread(new Runnable() {
			public void run() {
				for (Pose pose;;) {
					try {
						Thread.sleep(100);
						pose = opp.getPose();
						sendMotion(pose.getX(), pose.getY(), pose.getHeading());
						sendMotion(axle.getX(), axle.getY(), axle.getHeading());
					} catch (final InterruptedException e) {
						return;
					} catch (final IOException e) {
						System.out.println("Disconnected!");
						return;
					}
				}
			}
		});
		sender.setDaemon(true);
		sender.start();
		
		for (;;) {
			try {
				final Packet packet = connection.readPacket();
				if (packet instanceof MovePacket) {
					final MovePacket move = (MovePacket)packet;
					switch (move.getDirection()) {
						case 2:
							pilot.backward();
							break;
						case 4:
							pilot.rotateLeft();
							break;
						case 6:
							pilot.rotateRight();
							break;
						case 8:
							pilot.forward();
							break;
					}
				} else {
					pilot.stop();
				}
			} catch (final IOException e) {
				System.out.println("Disconnected!");
				break;
			}
		}
	}
	
	private final void sendMotion(final float x, final float y,
			final float heading) throws IOException {
		connection.writePacket(getPacketFactory().createMotion(x, y, heading, 0F));
	}
	
}