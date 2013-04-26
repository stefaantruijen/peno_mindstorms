package bluebot.io;

import bluebot.io.protocol.Packet;



/**
 * This {@link Translator} implementation provides translation
 * for any client-to-server traffic
 * 
 * @author Ruben Feyen
 */
public class ClientTranslator extends Translator {
	
	public ClientTranslator(final Connection connection) {
		super(connection);
	}
	
	
	
	public void disconnect() {
		sendPacket(getPacketFactory().createDisconnect());
	}
	
	public void doCalibrate() {
		sendPacket(getPacketFactory().createCommandCalibrate());
	}

	
	public void doPolygon(final int corners, final float length) {
		sendPacket(getPacketFactory().createPolygon(corners, length));
	}
	
	public void doTile() {
		sendPacket(getPacketFactory().createCommandTile());
	}
	
	public void doWhiteLineOrientation() {
		sendPacket(getPacketFactory().createCommandWhiteLineOrientation());
	}
	
	public void doSeeSaw(){
		sendPacket(getPacketFactory().createSeesaw());
	}
	
	public void moveBackward() {
		sendPacket(getPacketFactory().createMoveBackward());
	}
	
	public void moveBackward(final float distance,boolean b) {
		sendPacket(getPacketFactory().createMoveBackward(distance,b));
	}
	
	public void moveForward() {
		sendPacket(getPacketFactory().createMoveForward());
	}
	
	public void moveForward(final float distance,boolean b) {
		sendPacket(getPacketFactory().createMoveForward(distance,b));
	}
	
	public void reset() {
		sendPacket(getPacketFactory().createReset());
	}
	
	@Override
	protected void sendPacket(final Packet packet) {
		sendPacket(packet, true);
	}
	
	protected void sendPacket(final Packet packet, final boolean stop) {
		if (stop) {
			stop();
		}
		super.sendPacket(packet);
	}
	
	public void setSpeed(final int percentage) {
		sendPacket(getPacketFactory().createConfigSpeed(percentage), false);
	}
	
	public void stop() {
		sendPacket(getPacketFactory().createStop(), false);
	}
	
	public void turnLeft() {
		sendPacket(getPacketFactory().createTurnLeft());
	}
	
	public void turnLeft(final float angle,boolean b) {
		sendPacket(getPacketFactory().createTurnLeft(angle,b));
	}
	
	public void turnRight() {
		sendPacket(getPacketFactory().createTurnRight());
	}
	
	public void turnRight(final float angle,boolean b) {
		sendPacket(getPacketFactory().createTurnRight(angle,b));
	}
	
	public void readBarcode(){
		sendPacket(getPacketFactory().createReadBarcode());
		//sendPacket(getPacketFactory().cr)
	}

	public void doPickUp() {
		sendPacket(getPacketFactory().createPickUp());
		
	}

	public void turnHeadClockwise(int offset) {
		sendPacket(getPacketFactory().createTurnHeadClockwise(offset));
		
	}

	public void turnHeadcounterClockwise(int offset) {
		sendPacket(getPacketFactory().createTurnHeadCounterClockwise(offset));
		
	}

	public void modifyOrientation() {
		sendPacket(getPacketFactory().createModifyOrientation());
	}
}