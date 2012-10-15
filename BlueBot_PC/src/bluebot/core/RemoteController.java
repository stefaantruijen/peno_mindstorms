package bluebot.core;


import static bluebot.io.protocol.PacketFactory.getPacketFactory;

import java.io.IOException;

import lejos.pc.comm.NXTCommException;

import bluebot.io.ClientConnection;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;



/**
 * Implementation of the {@link Controller} interface
 * to be used when connecting to an NXT brick
 * 
 * @author Ruben Feyen
 */
public class RemoteController extends AbstractController {
	
	private Communicator communicator;
	private Connection connection;
	
	
	public RemoteController(final Connection connection) {
		this.communicator = createCommunicator(connection);
		this.connection = connection;
		
		getCommunicator().start();
	}
	
	
	
	@Override
	public void addListener(final ControllerListener listener) {
		super.addListener(listener);
		getConnection().addListener(listener);
	}
	
	public static Controller connect(final String name) throws NXTCommException {
		return new RemoteController(ClientConnection.create(name));
	}
	
	private final Communicator createCommunicator(final Connection connection) {
		return new Communicator(connection, createPacketHandler());
	}
	
	protected PacketHandler createPacketHandler() {
		// TODO: Return proper packet handler
		return new PacketHandler() {
			public void handlePacket(final Packet packet) {
				// ignored
			}
		};
	}
	
	public void disconnect() throws IOException {
		getCommunicator().stop();
		getConnection().close();
	}
	
	private final Communicator getCommunicator() {
		return communicator;
	}
	
	private final Connection getConnection() {
		return connection;
	}
	
	public void moveBackward() {
		sendPacket(getPacketFactory().createMoveBackward());
	}
	
	protected void moveBackward(final float distance) {
		sendPacket(getPacketFactory().createMoveBackward(distance));
	}
	
	public void moveForward() {
		sendPacket(getPacketFactory().createMoveForward());
	}
	
	protected void moveForward(final float distance) {
		sendPacket(getPacketFactory().createMoveForward(distance));
	}
	
	@Override
	public void removeListener(final ControllerListener listener) {
		super.removeListener(listener);
		getConnection().removeListener(listener);
	}
	
	private final void sendPacket(final Packet packet) {
		try {
			getConnection().writePacket(packet);
		} catch (final IOException e) {
			e.printStackTrace();
			// TODO: Handle the error appropriately
		}
	}
	
	public void stop() {
		sendPacket(getPacketFactory().createStop());
	}
	
	public void turnLeft() {
		sendPacket(getPacketFactory().createTurnLeft());
	}
	
	protected void turnLeft(final float degrees) {
		sendPacket(getPacketFactory().createTurnLeft(degrees));
	}
	
	public void turnRight() {
		sendPacket(getPacketFactory().createTurnRight());
	}
	
	protected void turnRight(final float degrees) {
		sendPacket(getPacketFactory().createTurnRight(degrees));
	}
	
}