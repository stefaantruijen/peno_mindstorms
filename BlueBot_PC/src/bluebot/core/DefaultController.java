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
	
	
	
	@Override
	public void addListener(final ControllerListener listener) {
		super.addListener(listener);
		getCommunicator().addListener(listener);
	}
	
	protected PacketHandler createPacketHandler() {
		return new PacketHandler() {
			public void handlePacket(final Packet packet) {
				// TODO
			}
		};
	}
	
	public void doCalibrate() {
		getTranslator().doCalibrate();
	}
	
	public void doWhiteLineOrientation() {
		getTranslator().doWhiteLineOrientation();
	}
	
	private final Communicator getCommunicator() {
		return communicator;
	}
	
	private final ClientTranslator getTranslator() {
		return translator;
	}
	
	public void moveBackward() {
		getTranslator().moveBackward();
	}
	
	public void moveBackward(final float distance) {
		getTranslator().moveBackward(distance);
	}
	
	public void moveForward() {
		getTranslator().moveForward();
	}
	
	public void moveForward(final float distance) {
		getTranslator().moveForward(distance);
	}
	
	@Override
	public void removeListener(final ControllerListener listener) {
		super.removeListener(listener);
		getCommunicator().removeListener(listener);
	}
	
	public void stop() {
		getTranslator().stop();
	}

	@Override
	public void turnLeft() {
		getTranslator().turnLeft();
	}
	
	public void turnLeft(final float angle) {
		getTranslator().turnLeft(angle);
	}
	
	public void turnRight() {
		getTranslator().turnRight();
	}
	
	public void turnRight(final float angle) {
		getTranslator().turnRight(angle);
	}

}