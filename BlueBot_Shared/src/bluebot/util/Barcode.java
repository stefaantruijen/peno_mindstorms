package bluebot.util;


import bluebot.maze.BarcodeValidator;



/**
 * 
 * @author Ruben Feyen
 */
public class Barcode {
	
	private int bits;
	private int value;
	
	
	
	private final void add(final int bit) {
		bits++;
		value = ((value << 1) | bit);
	}
	
	public void addBlack() {
		add(0);
	}
	
	public void addWhite() {
		add(1);
	}
	
	public int bits() {
		return bits;
	}
	
	public static final String format(final int barcode) {
		final String binary = Integer.toBinaryString(barcode);
		final StringBuilder sb = new StringBuilder(6);
		for (int i = (6 - binary.length()); i > 0; i--) {
			sb.append('0');
		}
		return sb.append(binary).toString();
	}
	
	public int getValue() {
		return BarcodeValidator.validate(value);
	}
	
	public void reset() {
		bits = 0;
		value = 0;
	}
	
}