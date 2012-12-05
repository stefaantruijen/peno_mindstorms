package algorithms;


import java.util.LinkedList;

import bluebot.BarcodeExecuter;
import bluebot.graph.Tile;
import bluebot.sensors.Brightness;
import bluebot.util.Barcode;
import bluebot.util.Orientation;
import bluebot.util.Threaded;



/**
 * 
 * @author Dieter Castel
 * @author Ruben Feyen
 */
public abstract class AbstractBarcodeScanner extends Threaded {
	
	private static final long SENSOR_READING_INTERVAL = 10;
	private static final float STRIP_LENGTH = 20F;
	
	private boolean ready;
	private LinkedList<Brightness> strips = new LinkedList<Brightness>();
	
	
	
	private final void checkCode() {
		print("#strips = " + strips.size());
		if ((strips.size() < 7)
				|| (strips.get(0) != Brightness.BLACK)) {
			print("strips.size() < 7");
			return;
		}
		
		final Barcode barcode = new Barcode();
		while (!strips.isEmpty()) {
			switch (strips.remove(0)) {
				case BLACK:
					if (ready) {
						print("BIT black");
						barcode.addBlack();
						if (checkCode(barcode)) {
							ready = false;
							return;
						}
					} else {
						print("First Black Read");
						ready = true;
					}
					break;
					
				case GRAY:
					print("RESET (gray strip)");
					barcode.reset();
					ready = false;
					break;
					
				case WHITE:
					if (ready) {
						print("BIT white");
						barcode.addWhite();
						if (checkCode(barcode)) {
							ready = false;
							return;
						}
					}
					break;
					
				default:
					throw new RuntimeException("Invalid enum value");
			}
		}	
	}
	
	private final boolean checkCode(final Barcode barcode) {
		print("#bits = " + barcode.bits());
		if (barcode.bits() == 6) {
			final int value = barcode.getValue();
			print("value = " + value);
			if (value > 0) {
				print("Found code!  " + BarcodeExecuter.convertIntToBinary(value));
				getTile().setBarCode(value);
				return true;
			}
			barcode.reset();
		}
		return false;
	}
	
	protected abstract Orientation getOrientation();
	
	private final float getPosition() {
		final Orientation pos = getOrientation();
		switch (bluebot.graph.Orientation.forHeading(pos.getHeadingBody())) {
			case NORTH:
			case SOUTH:
				return pos.getY();
			case EAST:
			case WEST:
				return pos.getX();
			default:
				throw new RuntimeException("The universe has collapsed!");
		}
	}
	
	protected abstract Tile getTile();
	
	protected abstract boolean isMoving();
	
	protected abstract void print(String msg);
	
	protected abstract Brightness readSensor();
	
	public void run() {
		Brightness lastColor = null, nextColor;
		float lastPos = 0F, nextPos;
		for (;;) {
			try {
				Thread.sleep(SENSOR_READING_INTERVAL);
			} catch (final InterruptedException e) {
				strips.clear();
				return;
			}
			
			nextColor = readSensor();
			nextPos = getPosition();
			if (lastColor == null) {
				lastColor = nextColor;
				lastPos = nextPos;
			} else {
				if (nextColor != lastColor) {
					final int length = Math.round(Math.abs(nextPos - lastPos) / STRIP_LENGTH);
					if (lastColor == Brightness.GRAY) {
						if (length > 0) {
							print("Clearing strips due to big gray blob");
							strips.clear();
						}
					} else {
						print("[A] Adding " + lastColor + " x" + length);
						for (int i = length; i > 0; i--) {
							strips.add(lastColor);
						}
					}
					lastColor = nextColor;
					lastPos = nextPos;
				} else if (!isMoving()) {
					if (!strips.isEmpty()) {
						if (nextColor == Brightness.GRAY) {
							checkCode();
						} else {
							final int length = (int)Math.ceil(Math.abs(nextPos - lastPos) / STRIP_LENGTH);
							print("[B] Adding " + lastColor + " x" + length);
							for (int i = length; i > 0; i--) {
								strips.add(nextColor);
							}
							checkCode();
						}
						strips.clear();
					}
				}
			}
		}
	}
	
}