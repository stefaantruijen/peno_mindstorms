package bluebot;


import static bluebot.Operator.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import bluebot.io.Link;
import bluebot.operations.OperationException;
import bluebot.sensors.CalibrationException;
import bluebot.sensors.SensorType;
import bluebot.util.Orientation;



/**
 * 
 * @author Ruben Feyen
 */
public class OperatorHandler implements Runnable {
	
	private final Object lock = new Object();
	
	private Link link;
	private Operator operator;
	
	
	public OperatorHandler(final Operator operator, final Link link) {
		this.link = link;
		this.operator = operator;
		
		operator.addListener(new OperatorMonitor());
	}
	
	
	
	protected Handler createHandler(final int opcode) {
		switch (opcode) {
			case OP_DO_BARCODE:
				return createHandlerDoBarcode();
				
			case OP_DO_CALIBRATE:
			case OP_DO_PICKUP:
			case OP_DO_SEESAW:
			case OP_DO_WHITELINE:
				return createHandlerDoRoutine(opcode);
				
			case OP_MOVE_BACKWARD:
			case OP_MOVE_FORWARD:
			case OP_TURN_LEFT:
			case OP_TURN_RIGHT:
				return createHandlerMotionBody(opcode);
				
			case OP_TURN_HEAD_CCW:
			case OP_TURN_HEAD_CW:
				return createHandlerMotionHead(opcode);
				
			case OP_MOVING:
				return createHandlerMoving();
				
			case OP_ORIENTATION_GET:
				return createHandlerOrientationGet();
			case OP_ORIENTATION_MODIFY:
			case OP_ORIENTATION_RESET:
				return createHandlerOrientation(opcode);
				
			case OP_SENSOR:
				return createHandlerSensor();
			case OP_SENSORS:
				return createHandlerSensors();
				
			case OP_SPEED_GET:
				return createHandlerSpeedGet();
			case OP_SPEED_SET:
				return createHandlerSpeedSet();
				
			case OP_STOP:
				return createHandlerStop();
				
			default:
				return null;
		}
	}
	
	private final Handler createHandlerDoBarcode() {
		return new Handler() {
			
			private int barcode;
			private String msg;
			
			
			
			public void read(final DataInputStream stream) throws IOException {
				//	no args
			}
			
			public void handle() {
				try {
					barcode = getOperator().scanBarcode();
				} catch (final CalibrationException e) {
					barcode = -2;
					msg = e.getMessage();
				} catch (final InterruptedException e) {
					barcode = -3;
					msg = e.getMessage();
				} catch (final OperationException e) {
					barcode = -4;
					msg = e.getMessage();
				}
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				if (msg == null) {
					stream.writeByte(0);
					stream.writeInt(barcode);
				} else {
					stream.writeByte(-barcode - 1);
					stream.writeUTF(msg);
				}
			}
		};
	}
	
	private final Handler createHandlerDoRoutine(final int opcode) {
		return new Handler() {
			
			private int error;
			private String msg;
			
			
			
			public void handle() {
				try {
					switch (opcode) {
						case OP_DO_CALIBRATE:
							getOperator().doCalibrate();
							break;
						case OP_DO_PICKUP:
							getOperator().doPickUp();
							break;
						case OP_DO_SEESAW:
							getOperator().doSeesaw();
							break;
						case OP_DO_WHITELINE:
							getOperator().doWhiteLine();
							break;
					}
				} catch (final CalibrationException e) {
					error = 1;
					msg = e.getMessage();
				} catch (final InterruptedException e) {
					error = 2;
					msg = e.getMessage();
				} catch (final OperationException e) {
					error = 3;
					msg = e.getMessage();
				}
			}
			
			public void read(final DataInputStream stream) throws IOException {
				//	no args
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				if (msg == null) {
					stream.writeByte(0);
				} else {
					stream.writeByte(error);
					stream.writeUTF(msg);
				}
			}
			
		};
	}
	
	private final Handler createHandlerMotionBody(final int opcode) {
		return new Handler() {
			
			private float quantity;
			private boolean wait;
			
			
			
			public void handle() {
				switch (opcode) {
					case OP_MOVE_BACKWARD:
						getOperator().moveBackward(quantity, wait);
						break;
					case OP_MOVE_FORWARD:
						getOperator().moveForward(quantity, wait);
						break;
					case OP_TURN_LEFT:
						getOperator().turnLeft(quantity, wait);
						break;
					case OP_TURN_RIGHT:
						getOperator().turnRight(quantity, wait);
						break;
				}
			}
			
			public void read(final DataInputStream stream) throws IOException {
				quantity = stream.readFloat();
				wait = stream.readBoolean();
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				//	void
			}
			
		};
	}
	
	private final Handler createHandlerMotionHead(final int opcode) {
		return new Handler() {
			
			private int angle;
			
			
			
			public void handle() {
				switch (opcode) {
					case OP_TURN_HEAD_CCW:
						getOperator().turnHeadCounterClockWise(angle);
						break;
					case OP_TURN_HEAD_CW:
						getOperator().turnHeadClockWise(angle);
						break;
				}
			}
			
			public void read(final DataInputStream stream) throws IOException {
				angle = stream.readInt();
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				//	void
			}
			
		};
	}
	
	private final Handler createHandlerMoving() {
		return new Handler() {
			
			private boolean moving;
			
			
			
			public void handle() {
				moving = getOperator().isMoving();
			}
			
			public void read(final DataInputStream stream) throws IOException {
				//	 no args
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				stream.writeBoolean(moving);
			}
			
		};
	}
	
	private final Handler createHandlerOrientation(final int opcode) {
		return new Handler() {
			
			public void handle() {
				switch (opcode) {
					case OP_ORIENTATION_MODIFY:
						getOperator().modifyOrientation();
						break;
					case OP_ORIENTATION_RESET:
						getOperator().resetOrientation();
						break;
				}
			}
			
			public void read(final DataInputStream stream) throws IOException {
				//	no args
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				//	void
			}
			
		};
	}
	
	private final Handler createHandlerOrientationGet() {
		return new Handler() {
			
			private Orientation o;
			
			
			
			public void handle() {
				o = getOperator().getOrientation();
			}
			
			public void read(final DataInputStream stream) throws IOException {
				//	no args
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				stream.writeFloat(o.getX());
				stream.writeFloat(o.getY());
				stream.writeFloat(o.getHeadingBody());
				stream.writeFloat(o.getHeadingHead());
			}
			
		};
	}
	
	private final Handler createHandlerSensor() {
		return new Handler() {
			
			private SensorType sensor;
			private int value;
			
			
			
			public void handle() {
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
						break;
				}
			}
			
			public void read(final DataInputStream stream) throws IOException {
				final int ordinal = stream.readUnsignedByte();
				sensor = SensorType.values()[ordinal];
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				stream.writeShort(value);
			}
			
		};
	}
	
	private final Handler createHandlerSensors() {
		return new Handler() {
			
			private int[] values;
			
			
			
			public void handle() {
				values = getOperator().readSensors();
			}
			
			public void read(final DataInputStream stream) throws IOException {
				//	no args
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				for (final SensorType sensor : SensorType.values()) {
					writeValue(stream, sensor, values[sensor.ordinal()]);
				}
			}
			
			private final void writeValue(final DataOutputStream stream,
					final SensorType sensor, final int value) throws IOException {
				switch (sensor) {
					case TOUCH:
						stream.writeByte(value);
						break;
					default:
						stream.writeShort(value);
						break;
				}
			}
			
		};
	}
	
	private final Handler createHandlerSpeedGet() {
		return new Handler() {
			
			private int speed;
			
			
			
			public void handle() {
				speed = getOperator().getSpeed();
			}
			
			public void read(final DataInputStream stream) throws IOException {
				//	no args
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				stream.writeByte(speed);
			}
			
		};
	}
	
	private final Handler createHandlerSpeedSet() {
		return new Handler() {
			
			private int speed;
			
			
			
			public void handle() {
				getOperator().setSpeed(speed);
			}
			
			public void read(final DataInputStream stream) throws IOException {
				speed = stream.readUnsignedByte();
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				//	void
			}
			
		};
	}
	
	private final Handler createHandlerStop() {
		return new Handler() {
			
			public void handle() {
				getOperator().stop();
			}
			
			public void read(final DataInputStream stream) throws IOException {
				//	no args
			}
			
			public void write(final DataOutputStream stream) throws IOException {
				// void
			}
			
		};
	}
	
	private static final void execute(final Runnable task) {
		final Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}
	
	private final DataInputStream getInput() {
		return getLink().getInput();
	}
	
	private final Link getLink() {
		return link;
	}
	
	private final Operator getOperator() {
		return operator;
	}
	
	private final DataOutputStream getOutput() {
		return getLink().getOutput();
	}
	
	private final void handle(final int opcode) throws IOException {
		final Handler handler = createHandler(opcode);
		if (handler == null) {
			System.out.println("IGNORED: " + opcode);
		} else {
			handle(opcode, handler);
		}
	}
	
	private final void handle(final int opcode, final Handler handler) throws IOException {
		handler.read(getInput());
		execute(new Runnable() {
			public void run() {
				handler.handle();
				
				final DataOutputStream stream = getOutput();
				synchronized (lock) {
					try {
						stream.writeByte(opcode);
						handler.write(stream);
						stream.flush();
					} catch (final IOException e) {
						//	ignored
					}
				}
			}
		});
	}
	
	public void run() {
		for (;;) {
			try {
				handle(getInput().readUnsignedByte());
			} catch (final IOException e) {
				System.out.println("DISCONNECTED");
				return;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	private static abstract class Handler {
		
		public abstract void handle();
		
		public abstract void read(DataInputStream stream) throws IOException;
		
		public abstract void write(DataOutputStream stream) throws IOException;
		
	}
	
	
	
	
	
	private final class OperatorMonitor implements OperatorListener {
		
		public void onSpeedChanged(final int percentage) {
			try {
				final DataOutputStream stream = getOutput();
				synchronized (lock) {
					stream.writeByte(OP_EVENT_SPEED);
					stream.writeByte(percentage);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
