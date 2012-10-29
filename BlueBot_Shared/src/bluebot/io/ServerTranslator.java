package bluebot.io;


import bluebot.sensors.SensorType;



/**
 * This {@link Translator} implementation provides translation
 * for any server-to-client traffic
 * 
 * @author Ruben Feyen
 */
public class ServerTranslator extends Translator {
	
	public ServerTranslator(final Connection connection) {
		super(connection);
	}
	
	
	
	public void notifySpeedHigh() {
		sendPacket(getPacketFactory().createConfigSpeedHigh());
	}
	
	public void notifySpeedLow() {
		sendPacket(getPacketFactory().createConfigSpeedLow());
	}
	
	public void notifySpeedMedium() {
		sendPacket(getPacketFactory().createConfigSpeedMedium());
	}
	
	public void sendError(final String msg) {
		sendPacket(getPacketFactory().createError(msg));
	}
	
	public void sendMotion(final float x, final float y, final float heading) {
		sendPacket(getPacketFactory().createMotion(x, y, heading));
	}
	
	public void sendSensorValue(final SensorType type, final int value) {
		sendPacket(getPacketFactory().createSensorResponse(type, value));
	}
	
}