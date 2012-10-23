package bluebot.core;


import bluebot.io.ClientTranslator;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultController extends AbstractController {
	
	private Communicator communicator;
	private ClientTranslator translator;
	
	
	public DefaultController(final Connection connection) {
		this.communicator = new Communicator(connection, createPacketHandler());
		this.translator = new ClientTranslator(connection);
		
		this.communicator.start();
	}
	
	
	
	protected PacketHandler createPacketHandler() {
		return new PacketHandler() {
			public void handlePacket(final Packet packet) {
				// TODO
			}
		};
	}
	
	private final ClientTranslator getTranslator() {
		return translator;
	}
	
	public void moveBackward() {
		getTranslator().moveBackward();
	}
	
	protected void moveBackward(final float distance) {
		getTranslator().moveBackward(distance);
	}
	
	public void moveForward() {
		getTranslator().moveForward();
	}
	
	protected void moveForward(final float distance) {
		getTranslator().moveForward(distance);
	}
	
	public void stop() {
		getTranslator().stop();
	}

	@Override
	public void turnLeft() {
		getTranslator().turnLeft();
	}
	
	protected void turnLeft(final float angle) {
		getTranslator().turnLeft(angle);
	}
	
	public void turnRight() {
		getTranslator().turnRight();
	}
	
	protected void turnRight(final float angle) {
		getTranslator().turnRight(angle);
	}

}