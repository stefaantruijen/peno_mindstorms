package bluebot.tests;



import lejos.nxt.Button;
import lejos.nxt.LCD;



/**
 * 
 * @author Ruben Feyen
 */
public class MemTest {
	
//	private static final void display(final String str) {
//		LCD.drawString(str, 0, 0);
//	}
	
	public static void main(final String[] args) {
		try {
			final long mem = System.getRuntime().freeMemory();
//			display("mem:  " + mem);
			System.out.println("mem: " + mem);
			Button.waitForAnyPress();
		} catch (final Throwable e) {
//			display("ERROR");
		}
	}
	
}