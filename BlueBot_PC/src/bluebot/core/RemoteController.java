package bluebot.core;


import static bluebot.io.protocol.PacketFactory.getPacketFactory;

import java.io.IOException;

import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.protocol.DelegatingPacketHandler;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;



/**
 * 
 * @author Ruben Feyen
 */
public class RemoteController implements Controller {
	
	private Communicator communicator;
	private Connection connection;
	
	
	public RemoteController(final Connection connection) {
		this.communicator = createCommunicator(connection);
		this.connection = connection;
		
		getCommunicator().start();
	}
	
	
	
	private final Communicator createCommunicator(final Connection connection) {
		return new Communicator(connection, createPacketHandler());
	}
	
	protected PacketHandler createPacketHandler() {
		final DelegatingPacketHandler handlers = new DelegatingPacketHandler();
		// TODO: Register packet handlers
		return handlers;
	}
	
	public void disconnect() throws IOException {
		getCommunicator().stop();
		getConnection().close();
	}
	
	public void doPolygon(final int corners, final int length) {
		sendPacket(getPacketFactory().createPolygon(corners, length));
	}
	
	private final Communicator getCommunicator() {
		return communicator;
	}
	
	private final Connection getConnection() {
		return connection;
	}
	
	public void moveBackward() {
		sendPacket(getPacketFactory().createMove(2, 1000F));
	}
	
	public void moveForward() {
		sendPacket(getPacketFactory().createMove(8, 1000F));
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
		sendPacket(getPacketFactory().createMove(4, 90F));
	}
	
	public void turnRight() {
		sendPacket(getPacketFactory().createMove(6, 90F));
	}
	
}