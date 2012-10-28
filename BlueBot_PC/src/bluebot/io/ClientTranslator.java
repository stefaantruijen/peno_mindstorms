package bluebot.io;



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
	
	
	
	public void doCalibrate() {
		sendPacket(getPacketFactory().createCommandCalibrate());
	}
	
	public void doWhiteLineOrientation() {
		sendPacket(getPacketFactory().createCommandWhiteLineOrientation());
	}
	
	public void moveBackward() {
		sendPacket(getPacketFactory().createMoveBackward());
	}
	
	public void moveBackward(final float distance) {
		sendPacket(getPacketFactory().createMoveBackward(distance));
	}
	
	public void moveForward() {
		sendPacket(getPacketFactory().createMoveForward());
	}
	
	public void moveForward(final float distance) {
		sendPacket(getPacketFactory().createMoveForward(distance));
	}
	
	public void setSpeedHigh() {
		sendPacket(getPacketFactory().createConfigSpeedHigh());
	}
	
	public void setSpeedLow() {
		sendPacket(getPacketFactory().createConfigSpeedLow());
	}
	
	public void setSpeedMedium() {
		sendPacket(getPacketFactory().createConfigSpeedMedium());
	}
	
	public void stop() {
		sendPacket(getPacketFactory().createStop());
	}
	
	public void turnLeft() {
		sendPacket(getPacketFactory().createTurnLeft());
	}
	
	public void turnLeft(final float angle) {
		sendPacket(getPacketFactory().createTurnLeft(angle));
	}
	
	public void turnRight() {
		sendPacket(getPacketFactory().createTurnRight());
	}
	
	public void turnRight(final float angle) {
		sendPacket(getPacketFactory().createTurnRight(angle));
	}
	
}