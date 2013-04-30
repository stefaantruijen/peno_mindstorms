package bluebot;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import bluebot.io.protocol.Channel;
import bluebot.operations.OperationException;
import bluebot.sensors.Calibration;
import bluebot.sensors.CalibrationException;
import bluebot.sensors.SensorType;
import bluebot.util.Orientation;



/**
 * 
 * @author Ruben Feyen
 */
public class RemoteOperator extends AbstractOperator {
	
	private final Object lock = new Object();
	
	private Channel channel;
	
	
	public RemoteOperator(final Channel channel) {
		this.channel = channel;
	}
	
	
	
	public void doCalibrate() throws InterruptedException, OperationException {
		synchronized (lock) {
			try {
				sendOpcode(OP_DO_CALIBRATE);
				switch (getInput().readUnsignedByte()) {
					case 0:
						break;
					case 1:
						//	CalibrationException
						throw new Error(getInput().readUTF());
					case 2:
						throw new InterruptedException(getInput().readUTF());
					case 3:
						throw new OperationException(getInput().readUTF());
				}
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void doPickUp()
			throws CalibrationException, InterruptedException, OperationException {
		synchronized (lock) {
			try {
				sendOpcode(OP_DO_PICKUP);
				switch (getInput().readUnsignedByte()) {
					case 0:
						break;
					case 1:
						//	CalibrationException
						throw new Error(getInput().readUTF());
					case 2:
						throw new InterruptedException(getInput().readUTF());
					case 3:
						throw new OperationException(getInput().readUTF());
				}
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void doSeesaw()
			throws CalibrationException, InterruptedException, OperationException {
		synchronized (lock) {
			try {
				sendOpcode(OP_DO_SEESAW);
				switch (getInput().readUnsignedByte()) {
					case 0:
						break;
					case 1:
						//	CalibrationException
						throw new Error(getInput().readUTF());
					case 2:
						throw new InterruptedException(getInput().readUTF());
					case 3:
						throw new OperationException(getInput().readUTF());
				}
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void doWhiteLine()
			throws CalibrationException, InterruptedException, OperationException {
		synchronized (lock) {
			try {
				sendOpcode(OP_DO_WHITELINE);
				switch (getInput().readUnsignedByte()) {
					case 0:
						break;
					case 1:
						//	CalibrationException
						throw new Error(getInput().readUTF());
					case 2:
						throw new InterruptedException(getInput().readUTF());
					case 3:
						throw new OperationException(getInput().readUTF());
				}
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public float getAngleIncrement() {
		throw new UnsupportedOperationException();
	}
	
	public float getArcLimit() {
		throw new UnsupportedOperationException();
	}
	
	public Calibration getCalibration() {
		throw new UnsupportedOperationException();
	}
	
	private final Channel getChannel() {
		return channel;
	}
	
	private final DataInputStream getInput() {
		return getChannel().getInput();
	}
	
	public Orientation getOrientation() {
		final float x, y, body, head;
		synchronized (lock) {
			try {
				sendOpcode(OP_ORIENTATION_GET);
				
				x = getInput().readFloat();
				y = getInput().readFloat();
				body = getInput().readFloat();
				head = getInput().readFloat();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
		return new Orientation(x, y, body, head);
	}
	
	private final DataOutputStream getOutput() {
		return getChannel().getOutput();
	}
	
	public int getSpeed() {
		synchronized (lock) {
			try {
				sendOpcode(OP_SPEED_GET);
				return getInput().readUnsignedByte();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public boolean isMoving() {
		synchronized (lock) {
			try {
				sendOpcode(OP_MOVING);
				return getInput().readBoolean();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void modifyOrientation() {
		synchronized (lock) {
			try {
				sendOpcode(OP_ORIENTATION_MODIFY);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void moveBackward() {
		throw new UnsupportedOperationException();
	}
	
	public void moveBackward(final float distance, final boolean wait) {
		if (!wait) {
			throw new UnsupportedOperationException();
		}
		
		synchronized (lock) {
			try {
				sendOpcode(OP_MOVE_BACKWARD);
				getOutput().writeFloat(distance);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void moveForward() {
		throw new UnsupportedOperationException();
	}
	
	public void moveForward(final float distance, final boolean wait) {
		if (!wait) {
			throw new UnsupportedOperationException();
		}
		
		synchronized (lock) {
			try {
				sendOpcode(OP_MOVE_FORWARD);
				getOutput().writeFloat(distance);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	private final int readSensor(final SensorType sensor) {
		final int value;
		synchronized (lock) {
			try {
				sendOpcode(OP_SENSOR);
				getOutput().writeByte(sensor.ordinal());
				value = getInput().readShort();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
		return value;
	}
	
	public int readSensorInfrared() {
		return readSensor(SensorType.INFRARED);
	}
	
	public int readSensorLight() {
		return readSensor(SensorType.LIGHT);
	}
	
	public boolean readSensorTouch() {
		return (readSensor(SensorType.TOUCH) != 0);
	}
	
	public int readSensorUltrasonic() {
		return readSensor(SensorType.ULTRA_SONIC);
	}
	
	private final void receiveAck() throws IOException {
		getInput().readUnsignedByte();
	}
	
	public void resetOrientation() {
		synchronized (lock) {
			try {
				sendOpcode(OP_ORIENTATION_RESET);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public int scanBarcode()
			throws CalibrationException, InterruptedException, OperationException {
		synchronized (lock) {
			try {
				sendOpcode(OP_DO_BARCODE);
				switch (getInput().readUnsignedByte()) {
					case 0:
						return getInput().readInt();
					case 1:
						throw new CalibrationException(getInput().readUTF());
					case 2:
						throw new InterruptedException(getInput().readUTF());
					default:
						throw new OperationException(getInput().readUTF());
				}
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	private final void sendOpcode(final int opcode) throws IOException {
		getOutput().writeByte(opcode);
	}
	
	public void setSpeed(final int percentage) {
		synchronized (lock) {
			try {
				sendOpcode(OP_SPEED_SET);
				getOutput().writeByte(percentage);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void setStartLocation(final int x, final int y, final float angle) {
		//	ignored
	}
	
	public void stop() {
		synchronized (lock) {
			try {
				sendOpcode(OP_STOP);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void turnHeadClockWise(final int angle) {
		synchronized (lock) {
			try {
				sendOpcode(OP_TURN_HEAD_CW);
				getOutput().writeInt(angle);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void turnHeadCounterClockWise(final int angle) {
		synchronized (lock) {
			try {
				sendOpcode(OP_TURN_HEAD_CCW);
				getOutput().writeInt(angle);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void turnLeft() {
		throw new UnsupportedOperationException();
	}
	
	public void turnLeft(final float angle, final boolean wait) {
		if (!wait) {
			throw new UnsupportedOperationException();
		}
		
		synchronized (lock) {
			try {
				sendOpcode(OP_TURN_LEFT);
				getOutput().writeFloat(angle);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void turnRight() {
		throw new UnsupportedOperationException();
	}
	
	public void turnRight(float angle, boolean wait) {
		if (!wait) {
			throw new UnsupportedOperationException();
		}
		
		synchronized (lock) {
			try {
				sendOpcode(OP_TURN_RIGHT);
				getOutput().writeFloat(angle);
				receiveAck();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
}
