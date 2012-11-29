package bluebot.sound;


import lejos.nxt.Button;



/**
 * 
 * @author Ruben Feyen
 */
public class Music {
	
	public static void main(final String... args) {
		try {
			playSong(Instrument.createPiano());
			System.out.println("DONE");
			Button.waitForAnyPress();
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static void playSong(final Instrument instrument) {
		final int s = 250;
		final int l = s * 2;
		
		instrument.playNote(s, Note.C4);
		instrument.playNote(s, Note.D4);
		instrument.playNote(s, Note.E4);
		instrument.playNote(s, Note.C4);
		
		instrument.playNote(s, Note.C4);
		instrument.playNote(s, Note.D4);
		instrument.playNote(s, Note.E4);
		instrument.playNote(s, Note.C4);
		
		instrument.playNote(s, Note.E4);
		instrument.playNote(s, Note.F4);
		instrument.playNote(l, Note.G4);
		
		instrument.playNote(s, Note.E4);
		instrument.playNote(s, Note.F4);
		instrument.playNote(l, Note.G4);
	}
	
}