package bluebot;


import static bluebot.Operator.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import bluebot.io.protocol.Channel;
import bluebot.operations.OperationException;
import bluebot.sensors.CalibrationException;
import bluebot.sensors.SensorType;
import bluebot.util.Orientation;



/**
 * 
 * @author Ruben Feyen
 */
public class OperatorHandler implements Runnable {
	
	private Channel channel;
	private Operator operator;
	
	
	public OperatorHandler(final Operator operator, final Channel channel) {
		this.channel = channel;
		this.operator = operator;
	}
	
	
	
	private final Channel getChannel() {
		return channel;
	}
	
	private final DataInputStream getInput() {
		return getChannel().getInput();
	}
	
	private final Operator getOperator() {
		return operator;
	}
	
	private final DataOutputStream getOutput() {
		return getChannel().getOutput();
	}
	
	private final void handle(final int opcode) throws IOException {
		switch (opcode) {
			case OP_DO_BARCODE:
				handleDoBarcode();
				break;
			case OP_DO_CALIBRATE:
				handleDoCalibrate();
				break;
			case OP_DO_PICKUP:
				handleDoPickUp();
				break;
			case OP_DO_SEESAW:
				handleDoSeesaw();
				break;
			case OP_DO_WHITELINE:
				handleDoWhiteLine();
				break;
			case OP_MOVE_BACKWARD:
				getOperator().moveBackward(getInput().readFloat(), true);
				sendAck();
				break;
			case OP_MOVE_FORWARD:
				getOperator().moveForward(getInput().readFloat(), true);
				sendAck();
				break;
			case OP_MOVING:
				getOutput().writeBoolean(getOperator().isMoving());
				break;
			case OP_ORIENTATION_GET:
				handleOrientationGet();
				break;
			case OP_ORIENTATION_MODIFY:
				getOperator().modifyOrientation();
				sendAck();
				break;
			case OP_ORIENTATION_RESET:
				getOperator().resetOrientation();
				sendAck();
				break;
			case OP_SENSOR:
				handleSensor();
				break;
			default:
				System.out.println("IGNORED: " + opcode);
				break;
		}
		//	TODO
	}
	
	private final void handleDoBarcode() throws IOException {
		try {
			final int barcode = getOperator().scanBarcode();
			getOutput().writeByte(0);
			getOutput().writeInt(barcode);
		} catch (final CalibrationException e) {
			sendError(1, e.getMessage());
		} catch (final InterruptedException e) {
			sendError(2, e.getMessage());
		} catch (final OperationException e) {
			sendError(3, e.getMessage());
		}
	}
	
	private final void handleDoCalibrate() throws IOException {
		try {
			getOperator().doCalibrate();
			getOutput().writeByte(0);
//		} catch (final CalibrationException e) {
//			sendError(1, e.getMessage());
		} catch (final InterruptedException e) {
			sendError(2, e.getMessage());
		} catch (final OperationException e) {
			sendError(3, e.getMessage());
		} catch (final Throwable e) {
			sendError(1, e.getMessage());
		}
	}
	
	private final void handleDoPickUp() throws IOException {
		try {
			getOperator().doPickUp();
			getOutput().writeByte(0);
		} catch (final CalibrationException e) {
			sendError(1, e.getMessage());
		} catch (final InterruptedException e) {
			sendError(2, e.getMessage());
		} catch (final OperationException e) {
			sendError(3, e.getMessage());
		}
	}
	
	private final void handleDoSeesaw() throws IOException {
		try {
			getOperator().doSeesaw();
			getOutput().writeByte(0);
		} catch (final CalibrationException e) {
			sendError(1, e.getMessage());
		} catch (final InterruptedException e) {
			sendError(2, e.getMessage());
		} catch (final OperationException e) {
			sendError(3, e.getMessage());
		}
	}
	
	private final void handleDoWhiteLine() throws IOException {
		try {
			getOperator().doWhiteLine();
			getOutput().writeByte(0);
		} catch (final CalibrationException e) {
			sendError(1, e.getMessage());
		} catch (final InterruptedException e) {
			sendError(2, e.getMessage());
		} catch (final OperationException e) {
			sendError(3, e.getMessage());
		}
	}
	
	private final void handleOrientationGet() throws IOException {
		final Orientation orientation = getOperator().getOrientation();
		getOutput().writeFloat(orientation.getX());
		getOutput().writeFloat(orientation.getY());
		getOutput().writeFloat(orientation.getHeadingBody());
		getOutput().writeFloat(orientation.getHeadingHead());
	}
	
	private final void handleSensor() throws IOException {
		final int ordinal = getInput().readUnsignedByte();
		final SensorType sensor = SensorType.values()[ordinal];
		
		final int value;
		switch (sensor) {
			case INFRARED:
				value = getOperator().readSensorInfrared();
				break;
			case LIGHT:
				value = getOperator().readSensorLight();
				break;
			case TOUCH:
				value = (getOperator().readSensorTouch() ? 1 : 0);
				break;
			case ULTRA_SONIC:
				value = getOperator().readSensorUltrasonic();
				break;
			default:
				value = -1;
		}
		getOutput().writeShort(value);
	}
	
	private final int receiveOpcode() throws IOException {
		return getInput().readUnsignedByte();
	}
	
	public void run() {
		for (;;) {
			try {
				handle(receiveOpcode());
			} catch (final IOException e) {
				System.out.println("DISCONNECTED");
				return;
			}
		}
	}
	
	private final void sendAck() throws IOException {
		getOutput().writeByte(0xFF);
	}
	
	private final void sendError(final int id, final String msg) throws IOException {
		getOutput().writeByte(id);
		getOutput().writeUTF(msg);
	}
	
}
