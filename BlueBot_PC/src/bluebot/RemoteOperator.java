package bluebot;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import peno.htttp.PlayerType;

import bluebot.io.Link;
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
	
	private RemoteCallManager caller;
	
	
	public RemoteOperator(final Link link) {
		this.caller = new RemoteCallManager(link);
		
		final Thread thread = new Thread(caller);
		thread.setDaemon(true);
		thread.start();
	}
	
	
	
	public void doCalibrate() throws InterruptedException, OperationException {
		try {
			doRoutine(OP_DO_CALIBRATE);
		} catch (final CalibrationException e) {
			throw new Error(e);
		}
	}
	
	private final void doMotionBody(final int opcode,
			final float quantity, final boolean wait) {
		getCallManager().makeCall(opcode, new RemoteCall<Void>() {
			protected Void read(final DataInputStream stream) throws IOException {
				return null;
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				stream.writeFloat(quantity);
				stream.writeBoolean(wait);
			}
		});
	}
	
	private final void doMotionHead(final int opcode, final int angle) {
		getCallManager().makeCall(opcode, new RemoteCall<Void>() {
			protected Void read(final DataInputStream stream) throws IOException {
				return null;
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				stream.writeInt(angle);
			}
		});
	}
	
	public void doPickUp()
			throws CalibrationException, InterruptedException, OperationException {
		doRoutine(OP_DO_PICKUP);
	}
	
	private final void doRoutine(final int opcode)
			throws CalibrationException, InterruptedException, OperationException {
		final ErrorTemplate error = new ErrorTemplate();
		getCallManager().makeCall(opcode, new RemoteCall<Void>() {
			protected Void read(final DataInputStream stream) throws IOException {
				final int errorCode = stream.readUnsignedByte();
				error.id = errorCode;
				if (errorCode > 0) {
					error.msg = stream.readUTF();
				}
				return null;
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				//	no args
			}
		});
		switch (error.id) {
			case 0:
				break;
			case 1:
				throw new CalibrationException(error.msg);
			case 2:
				throw new InterruptedException(error.msg);
			case 3:
				throw new OperationException(error.msg);
		}
	}
	
	public void doSeesaw()
			throws CalibrationException, InterruptedException, OperationException {
		doRoutine(OP_DO_SEESAW);
	}
	
	public void doWhiteLine()
			throws CalibrationException, InterruptedException, OperationException {
		doRoutine(OP_DO_WHITELINE);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		getCallManager().disconnect();
	}
	
	public float getAngleIncrement() {
		throw new UnsupportedOperationException();
	}
	
	public float getArcLimit() {
		throw new UnsupportedOperationException();
	}
	
	private final RemoteCallManager getCallManager() {
		return caller;
	}
	
	public Calibration getCalibration() {
		throw new UnsupportedOperationException();
	}
	
	public Orientation getOrientation() {
		return getCallManager().makeCall(OP_ORIENTATION_GET, new RemoteCall<Orientation>() {
			protected Orientation read(final DataInputStream stream) throws IOException {
				return new Orientation(stream.readFloat(), stream.readFloat(),
						stream.readFloat(), stream.readFloat());
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				//	no args
			}
		});
	}
	
	public String getPlayerType() {
		return PlayerType.PHYSICAL.toString();
	}
	
	public int getSpeed() {
		return getCallManager().makeCall(OP_SPEED_GET, new RemoteCall<Integer>() {
			protected Integer read(final DataInputStream stream) throws IOException {
				return stream.readUnsignedByte();
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				//	no args
			}
		});
	}
	
	public boolean isMoving() {
		return getCallManager().makeCall(OP_MOVING, new RemoteCall<Boolean>() {
			protected Boolean read(final DataInputStream stream) throws IOException {
				return stream.readBoolean();
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				//	no args
			}
		});
	}
	
	public void modifyOrientation() {
		getCallManager().makeCall(OP_ORIENTATION_MODIFY);
	}
	
	public void moveBackward() {
		throw new UnsupportedOperationException();
	}
	
	public void moveBackward(final float distance, final boolean wait) {
		doMotionBody(OP_MOVE_BACKWARD, distance, wait);
	}
	
	public void moveForward() {
		throw new UnsupportedOperationException();
	}
	
	public void moveForward(final float distance, final boolean wait) {
		doMotionBody(OP_MOVE_FORWARD, distance, wait);
	}
	
	private final int readSensor(final SensorType sensor) {
		return getCallManager().makeCall(OP_SENSOR, new RemoteCall<Integer>() {
			protected Integer read(final DataInputStream stream) throws IOException {
				return  Integer.valueOf(stream.readShort());
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				stream.writeByte(sensor.ordinal());
			}
		});
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
	
	public void resetOrientation() {
		getCallManager().makeCall(OP_ORIENTATION_RESET);
	}
	
	public int scanBarcode()
			throws CalibrationException, InterruptedException, OperationException {
		final ErrorTemplate error = new ErrorTemplate();
		final Integer code = getCallManager().makeCall(OP_DO_BARCODE,
				new RemoteCall<Integer>() {
			protected Integer read(final DataInputStream stream) throws IOException {
				final int errorCode = stream.readUnsignedByte();
				error.id = errorCode;
				if (errorCode > 0) {
					error.msg = stream.readUTF();
					return null;
				} 
				return stream.readInt();
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				//	no args
			}
		});
		switch (error.id) {
			case 0:
				return code;
			case 1:
				throw new CalibrationException(error.msg);
			case 2:
				throw new InterruptedException(error.msg);
			case 3:
				throw new OperationException(error.msg);
			default:
				throw new RuntimeException("Unexpected error-code:  " + error.id);
		}
	}
	
	public void setSpeed(final int percentage) {
		getCallManager().makeCall(OP_SPEED_SET, new RemoteCall<Void>() {
			protected Void read(final DataInputStream stream) throws IOException {
				return null;
			}
			
			protected void write(final DataOutputStream stream) throws IOException {
				stream.writeByte(percentage);
			}
		});
	}
	
	public void setStartLocation(final int x, final int y, final float angle) {
		//	ignored
	}
	
	public void stop() {
		getCallManager().makeCall(OP_STOP);
	}
	
	public void turnHeadClockWise(final int angle) {
		doMotionHead(OP_TURN_HEAD_CW, angle);
	}
	
	public void turnHeadCounterClockWise(final int angle) {
		doMotionHead(OP_TURN_HEAD_CCW, angle);
	}
	
	public void turnLeft() {
		throw new UnsupportedOperationException();
	}
	
	public void turnLeft(final float angle, final boolean wait) {
		doMotionBody(OP_TURN_LEFT, angle, wait);
	}
	
	public void turnRight() {
		throw new UnsupportedOperationException();
	}
	
	public void turnRight(float angle, boolean wait) {
		doMotionBody(OP_TURN_RIGHT, angle, wait);
	}
	
	
	
	
	
	
	
	
	
	
	private static final class ErrorTemplate {
		
		public int id;
		public String msg;
		
	}
	
	
	
	
	
	private static abstract class RemoteCall<T> {
		
		private final Object lock = new Object();
		
		private T result;
		
		
		
		public final T getResult() {
			return result;
		}
		
		public final void idle() throws InterruptedException {
			synchronized (lock) {
				lock.wait();
			}
		}
		
		public final void onReturn(final DataInputStream stream) throws IOException {
			result = read(stream);
			
			synchronized (lock) {
				lock.notify();
			}
		}
		
		protected abstract T read(DataInputStream stream) throws IOException;
		
		protected abstract void write(DataOutputStream stream) throws IOException;
		
	}
	
	
	
	
	
	private class RemoteCallManager implements Runnable {
		
		private final HashMap<Integer, LinkedList<RemoteCall<?>>> calls =
				new HashMap<Integer, LinkedList<RemoteCall<?>>>();
		private final Object lock = new Object();
		
		private Link link;
		
		
		private RemoteCallManager(final Link link) {
			this.link = link;
		}
		
		
		
		private synchronized final void addCall(final int opcode, final RemoteCall<?> call) {
			final Integer key = Integer.valueOf(opcode);
			LinkedList<RemoteCall<?>> list = calls.get(key);
			if (list == null) {
				list = new LinkedList<RemoteCall<?>>();
				calls.put(key, list);
			}
			list.addLast(call);
		}
		
		public void disconnect() {
			try {
				getLink().close();
			} catch (final IOException e) {
				//	ignored
			}
		}
		
		private synchronized final RemoteCall<?> getCall(final int opcode) {
			try {
				return calls.get(Integer.valueOf(opcode)).removeFirst();
			} catch (final NoSuchElementException e) {
				return null;
			} catch (final NullPointerException e) {
				return null;
			}
		}
		
		private final DataInputStream getInput() {
			return getLink().getInput();
		}
		
		private final Link getLink() {
			return link;
		}
		
		private final DataOutputStream getOutput() {
			return getLink().getOutput();
		}
		
		private final void handle(final int opcode) throws IOException {
//			System.out.println("Reading response 0x" + Integer.toHexString(opcode));
			final RemoteCall<?> call = getCall(opcode);
			if (call != null) {
				call.onReturn(getInput());
			}
		}
		
		public void makeCall(final int opcode) {
//			System.out.println("Make call " + Integer.toHexString(opcode));
			makeCall(opcode, new RemoteCall<Void>() {
				
				protected Void read(final DataInputStream stream) throws IOException {
					return null;
				}
				
				protected void write(final DataOutputStream stream) throws IOException {
					//	no args
				}
				
			});
		}
		
		public <T> T makeCall(final int opcode, final RemoteCall<T> call) {
//			System.out.printf("Make call %s (%s)%n", Integer.toHexString(opcode), call);
			addCall(opcode, call);
			
			try {
				synchronized (lock) {
					writeOpcode(opcode);
					call.write(getOutput());
				}
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
			
			try {
				call.idle();
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			return call.getResult();
		}
		
		private final int readOpcode() throws IOException {
			return getInput().readUnsignedByte();
		}
		
		public void run() {
			for (;;) {
				try {
					handle(readOpcode());
				} catch (final IOException e) {
					System.out.println(getClass().getSimpleName() + " disconnected");
					e.printStackTrace();
					return;
				}
			}
		}
		
		private final void writeOpcode(final int opcode) throws IOException {
			getOutput().writeByte(opcode);
		}
		
	}
	
}
