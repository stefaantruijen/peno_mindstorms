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
	
	public int getValue() {
		return BarcodeValidator.validate(value);
	}
	
	public void reset() {
		bits = 0;
		value = 0;
	}
	
}