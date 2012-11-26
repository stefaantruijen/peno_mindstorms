package bluebot.maze;



/**
 * 
 * @author Ruben Feyen
 */
public class BarcodeValidator {
	
	private static final int[] VALID = {
		1,
		2,
		3,
		4,
		5,
		6,
		7,
		8,
		9,
		10,
		11,
		13,
		14,
		15,
		17,
		19,
		21,
		22,
		23,
		25,
		27,
		29,
		31,
		35,
		37,
		39,
		43,
		47,
		55
	};
	
	
	
	private static final int reverse(int barcode) {
		int result = 0;
		for (int i = 6; i > 0; i--) {
			result <<= 1;
			result |= (barcode & 0x1);
			barcode >>>= 1;
		}
		return result;
	}
	
	public static int validate(final int barcode) {
		for (final int valid : VALID) {
			if ((barcode == valid) || (reverse(barcode) == valid)) {
				return valid;
			}
		}
		return -1;
	}
	
}